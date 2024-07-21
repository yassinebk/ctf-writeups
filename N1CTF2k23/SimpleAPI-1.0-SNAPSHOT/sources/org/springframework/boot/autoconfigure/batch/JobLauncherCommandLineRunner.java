package org.springframework.boot.autoconfigure.batch;

import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/batch/JobLauncherCommandLineRunner.class */
public class JobLauncherCommandLineRunner extends JobLauncherApplicationRunner {
    public JobLauncherCommandLineRunner(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository) {
        super(jobLauncher, jobExplorer, jobRepository);
    }
}
