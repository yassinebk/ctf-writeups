package org.springframework.boot.autoconfigure.batch;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.Ordered;
import org.springframework.core.log.LogMessage;
import org.springframework.util.Assert;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/batch/JobLauncherApplicationRunner.class */
public class JobLauncherApplicationRunner implements ApplicationRunner, Ordered, ApplicationEventPublisherAware {
    public static final int DEFAULT_ORDER = 0;
    private static final Log logger = LogFactory.getLog(JobLauncherApplicationRunner.class);
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final JobRepository jobRepository;
    private JobRegistry jobRegistry;
    private String jobNames;
    private ApplicationEventPublisher publisher;
    private JobParametersConverter converter = new DefaultJobParametersConverter();
    private Collection<Job> jobs = Collections.emptySet();
    private int order = 0;

    public JobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository) {
        Assert.notNull(jobLauncher, "JobLauncher must not be null");
        Assert.notNull(jobExplorer, "JobExplorer must not be null");
        Assert.notNull(jobRepository, "JobRepository must not be null");
        this.jobLauncher = jobLauncher;
        this.jobExplorer = jobExplorer;
        this.jobRepository = jobRepository;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.context.ApplicationEventPublisherAware
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Autowired(required = false)
    public void setJobRegistry(JobRegistry jobRegistry) {
        this.jobRegistry = jobRegistry;
    }

    public void setJobNames(String jobNames) {
        this.jobNames = jobNames;
    }

    @Autowired(required = false)
    public void setJobParametersConverter(JobParametersConverter converter) {
        this.converter = converter;
    }

    @Autowired(required = false)
    public void setJobs(Collection<Job> jobs) {
        this.jobs = jobs;
    }

    @Override // org.springframework.boot.ApplicationRunner
    public void run(ApplicationArguments args) throws Exception {
        String[] jobArguments = (String[]) args.getNonOptionArgs().toArray(new String[0]);
        run(jobArguments);
    }

    public void run(String... args) throws JobExecutionException {
        logger.info("Running default command line with: " + Arrays.asList(args));
        launchJobFromProperties(StringUtils.splitArrayElementsIntoProperties(args, "="));
    }

    protected void launchJobFromProperties(Properties properties) throws JobExecutionException {
        JobParameters jobParameters = this.converter.getJobParameters(properties);
        executeLocalJobs(jobParameters);
        executeRegisteredJobs(jobParameters);
    }

    private void executeLocalJobs(JobParameters jobParameters) throws JobExecutionException {
        for (Job job : this.jobs) {
            if (StringUtils.hasText(this.jobNames)) {
                String[] jobsToRun = this.jobNames.split(",");
                if (!PatternMatchUtils.simpleMatch(jobsToRun, job.getName())) {
                    logger.debug(LogMessage.format("Skipped job: %s", job.getName()));
                }
            }
            execute(job, jobParameters);
        }
    }

    private void executeRegisteredJobs(JobParameters jobParameters) throws JobExecutionException {
        if (this.jobRegistry != null && StringUtils.hasText(this.jobNames)) {
            String[] jobsToRun = this.jobNames.split(",");
            for (String jobName : jobsToRun) {
                try {
                    Job job = this.jobRegistry.getJob(jobName);
                    if (!this.jobs.contains(job)) {
                        execute(job, jobParameters);
                    }
                } catch (NoSuchJobException e) {
                    logger.debug(LogMessage.format("No job found in registry for job name: %s", jobName));
                }
            }
        }
    }

    protected void execute(Job job, JobParameters jobParameters) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobParametersNotFoundException {
        JobParameters parameters = getNextJobParameters(job, jobParameters);
        JobExecution execution = this.jobLauncher.run(job, parameters);
        if (this.publisher != null) {
            this.publisher.publishEvent((ApplicationEvent) new JobExecutionEvent(execution));
        }
    }

    private JobParameters getNextJobParameters(Job job, JobParameters jobParameters) {
        if (this.jobRepository != null && this.jobRepository.isJobInstanceExists(job.getName(), jobParameters)) {
            return getNextJobParametersForExisting(job, jobParameters);
        }
        if (job.getJobParametersIncrementer() == null) {
            return jobParameters;
        }
        JobParameters nextParameters = new JobParametersBuilder(jobParameters, this.jobExplorer).getNextJobParameters(job).toJobParameters();
        return merge(nextParameters, jobParameters);
    }

    private JobParameters getNextJobParametersForExisting(Job job, JobParameters jobParameters) {
        JobExecution lastExecution = this.jobRepository.getLastJobExecution(job.getName(), jobParameters);
        if (isStoppedOrFailed(lastExecution) && job.isRestartable()) {
            JobParameters previousIdentifyingParameters = getGetIdentifying(lastExecution.getJobParameters());
            return merge(previousIdentifyingParameters, jobParameters);
        }
        return jobParameters;
    }

    private boolean isStoppedOrFailed(JobExecution execution) {
        BatchStatus status = execution != null ? execution.getStatus() : null;
        return status == BatchStatus.STOPPED || status == BatchStatus.FAILED;
    }

    private JobParameters getGetIdentifying(JobParameters parameters) {
        HashMap<String, JobParameter> nonIdentifying = new LinkedHashMap<>(parameters.getParameters().size());
        parameters.getParameters().forEach(key, value -> {
            if (value.isIdentifying()) {
                nonIdentifying.put(key, value);
            }
        });
        return new JobParameters(nonIdentifying);
    }

    private JobParameters merge(JobParameters parameters, JobParameters additionals) {
        Map<String, JobParameter> merged = new LinkedHashMap<>();
        merged.putAll(parameters.getParameters());
        merged.putAll(additionals.getParameters());
        return new JobParameters(merged);
    }
}
