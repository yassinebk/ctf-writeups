package org.springframework.boot.autoconfigure.quartz;

import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.quartz.Calendar;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;
@EnableConfigurationProperties({QuartzProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class, PlatformTransactionManager.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, LiquibaseAutoConfiguration.class, FlywayAutoConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/quartz/QuartzAutoConfiguration.class */
public class QuartzAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public SchedulerFactoryBean quartzScheduler(QuartzProperties properties, ObjectProvider<SchedulerFactoryBeanCustomizer> customizers, ObjectProvider<JobDetail> jobDetails, Map<String, Calendar> calendars, ObjectProvider<Trigger> triggers, ApplicationContext applicationContext) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);
        if (properties.getSchedulerName() != null) {
            schedulerFactoryBean.setSchedulerName(properties.getSchedulerName());
        }
        schedulerFactoryBean.setAutoStartup(properties.isAutoStartup());
        schedulerFactoryBean.setStartupDelay((int) properties.getStartupDelay().getSeconds());
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(properties.isWaitForJobsToCompleteOnShutdown());
        schedulerFactoryBean.setOverwriteExistingJobs(properties.isOverwriteExistingJobs());
        if (!properties.getProperties().isEmpty()) {
            schedulerFactoryBean.setQuartzProperties(asProperties(properties.getProperties()));
        }
        schedulerFactoryBean.setJobDetails((JobDetail[]) jobDetails.orderedStream().toArray(x$0 -> {
            return new JobDetail[x$0];
        }));
        schedulerFactoryBean.setCalendars(calendars);
        schedulerFactoryBean.setTriggers((Trigger[]) triggers.orderedStream().toArray(x$02 -> {
            return new Trigger[x$02];
        }));
        customizers.orderedStream().forEach(customizer -> {
            customizer.customize(schedulerFactoryBean);
        });
        return schedulerFactoryBean;
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = "spring.quartz", name = {"job-store-type"}, havingValue = "jdbc")
    @ConditionalOnSingleCandidate(DataSource.class)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/quartz/QuartzAutoConfiguration$JdbcStoreTypeConfiguration.class */
    protected static class JdbcStoreTypeConfiguration {
        protected JdbcStoreTypeConfiguration() {
        }

        @Bean
        @Order(0)
        public SchedulerFactoryBeanCustomizer dataSourceCustomizer(QuartzProperties properties, DataSource dataSource, @QuartzDataSource ObjectProvider<DataSource> quartzDataSource, ObjectProvider<PlatformTransactionManager> transactionManager) {
            return schedulerFactoryBean -> {
                DataSource dataSourceToUse = getDataSource(dataSource, quartzDataSource);
                schedulerFactoryBean.setDataSource(dataSourceToUse);
                PlatformTransactionManager txManager = (PlatformTransactionManager) transactionManager.getIfUnique();
                if (txManager != null) {
                    schedulerFactoryBean.setTransactionManager(txManager);
                }
            };
        }

        private DataSource getDataSource(DataSource dataSource, ObjectProvider<DataSource> quartzDataSource) {
            DataSource dataSourceIfAvailable = quartzDataSource.getIfAvailable();
            return dataSourceIfAvailable != null ? dataSourceIfAvailable : dataSource;
        }

        @ConditionalOnMissingBean
        @Bean
        public QuartzDataSourceInitializer quartzDataSourceInitializer(DataSource dataSource, @QuartzDataSource ObjectProvider<DataSource> quartzDataSource, ResourceLoader resourceLoader, QuartzProperties properties) {
            DataSource dataSourceToUse = getDataSource(dataSource, quartzDataSource);
            return new QuartzDataSourceInitializer(dataSourceToUse, resourceLoader, properties);
        }

        @Configuration(proxyBeanMethods = false)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/quartz/QuartzAutoConfiguration$JdbcStoreTypeConfiguration$QuartzSchedulerDependencyConfiguration.class */
        static class QuartzSchedulerDependencyConfiguration {
            QuartzSchedulerDependencyConfiguration() {
            }

            @Bean
            static SchedulerDependsOnBeanFactoryPostProcessor quartzSchedulerDataSourceInitializerDependsOnBeanFactoryPostProcessor() {
                return new SchedulerDependsOnBeanFactoryPostProcessor(QuartzDataSourceInitializer.class);
            }

            @ConditionalOnBean({FlywayMigrationInitializer.class})
            @Bean
            static SchedulerDependsOnBeanFactoryPostProcessor quartzSchedulerFlywayDependsOnBeanFactoryPostProcessor() {
                return new SchedulerDependsOnBeanFactoryPostProcessor(FlywayMigrationInitializer.class);
            }

            @Configuration(proxyBeanMethods = false)
            @ConditionalOnClass({SpringLiquibase.class})
            /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/quartz/QuartzAutoConfiguration$JdbcStoreTypeConfiguration$QuartzSchedulerDependencyConfiguration$LiquibaseQuartzSchedulerDependencyConfiguration.class */
            static class LiquibaseQuartzSchedulerDependencyConfiguration {
                LiquibaseQuartzSchedulerDependencyConfiguration() {
                }

                @ConditionalOnBean({SpringLiquibase.class})
                @Bean
                static SchedulerDependsOnBeanFactoryPostProcessor quartzSchedulerLiquibaseDependsOnBeanFactoryPostProcessor() {
                    return new SchedulerDependsOnBeanFactoryPostProcessor(SpringLiquibase.class);
                }
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/quartz/QuartzAutoConfiguration$SchedulerDependsOnBeanFactoryPostProcessor.class */
    private static class SchedulerDependsOnBeanFactoryPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
        SchedulerDependsOnBeanFactoryPostProcessor(Class<?>... dependencyTypes) {
            super(Scheduler.class, SchedulerFactoryBean.class, dependencyTypes);
        }
    }
}
