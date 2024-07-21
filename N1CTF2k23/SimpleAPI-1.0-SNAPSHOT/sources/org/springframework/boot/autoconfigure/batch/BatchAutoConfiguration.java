package org.springframework.boot.autoconfigure.batch;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.util.StringUtils;
@EnableConfigurationProperties({BatchProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({JobLauncher.class, DataSource.class})
@AutoConfigureAfter({HibernateJpaAutoConfiguration.class})
@ConditionalOnBean({JobLauncher.class})
@Import({BatchConfigurerConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/batch/BatchAutoConfiguration.class */
public class BatchAutoConfiguration {
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
    @Bean
    public JobLauncherApplicationRunner jobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository, BatchProperties properties) {
        JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
        String jobNames = properties.getJob().getNames();
        if (StringUtils.hasText(jobNames)) {
            runner.setJobNames(jobNames);
        }
        return runner;
    }

    @ConditionalOnMissingBean({ExitCodeGenerator.class})
    @Bean
    public JobExecutionExitCodeGenerator jobExecutionExitCodeGenerator() {
        return new JobExecutionExitCodeGenerator();
    }

    @ConditionalOnMissingBean({JobOperator.class})
    @Bean
    public SimpleJobOperator jobOperator(ObjectProvider<JobParametersConverter> jobParametersConverter, JobExplorer jobExplorer, JobLauncher jobLauncher, ListableJobLocator jobRegistry, JobRepository jobRepository) throws Exception {
        SimpleJobOperator factory = new SimpleJobOperator();
        factory.setJobExplorer(jobExplorer);
        factory.setJobLauncher(jobLauncher);
        factory.setJobRegistry(jobRegistry);
        factory.setJobRepository(jobRepository);
        factory.getClass();
        jobParametersConverter.ifAvailable(this::setJobParametersConverter);
        return factory;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean({DataSource.class})
    @ConditionalOnClass({DatabasePopulator.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/batch/BatchAutoConfiguration$DataSourceInitializerConfiguration.class */
    static class DataSourceInitializerConfiguration {
        DataSourceInitializerConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        BatchDataSourceInitializer batchDataSourceInitializer(DataSource dataSource, @BatchDataSource ObjectProvider<DataSource> batchDataSource, ResourceLoader resourceLoader, BatchProperties properties) {
            return new BatchDataSourceInitializer(batchDataSource.getIfAvailable(() -> {
                return dataSource;
            }), resourceLoader, properties);
        }
    }
}
