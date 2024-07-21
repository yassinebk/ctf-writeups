package org.springframework.boot.autoconfigure.task;

import java.util.concurrent.Executor;
import java.util.stream.Stream;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
@EnableConfigurationProperties({TaskExecutionProperties.class})
@ConditionalOnClass({ThreadPoolTaskExecutor.class})
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/task/TaskExecutionAutoConfiguration.class */
public class TaskExecutionAutoConfiguration {
    public static final String APPLICATION_TASK_EXECUTOR_BEAN_NAME = "applicationTaskExecutor";

    @ConditionalOnMissingBean
    @Bean
    public TaskExecutorBuilder taskExecutorBuilder(TaskExecutionProperties properties, ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers, ObjectProvider<TaskDecorator> taskDecorator) {
        TaskExecutionProperties.Pool pool = properties.getPool();
        TaskExecutorBuilder builder = new TaskExecutorBuilder();
        TaskExecutorBuilder builder2 = builder.queueCapacity(pool.getQueueCapacity()).corePoolSize(pool.getCoreSize()).maxPoolSize(pool.getMaxSize()).allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout()).keepAlive(pool.getKeepAlive());
        TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
        TaskExecutorBuilder builder3 = builder2.awaitTermination(shutdown.isAwaitTermination()).awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod()).threadNamePrefix(properties.getThreadNamePrefix());
        Stream<TaskExecutorCustomizer> orderedStream = taskExecutorCustomizers.orderedStream();
        orderedStream.getClass();
        return builder3.customizers(this::iterator).taskDecorator(taskDecorator.getIfUnique());
    }

    @ConditionalOnMissingBean({Executor.class})
    @Lazy
    @Bean(name = {APPLICATION_TASK_EXECUTOR_BEAN_NAME, "taskExecutor"})
    public ThreadPoolTaskExecutor applicationTaskExecutor(TaskExecutorBuilder builder) {
        return builder.build();
    }
}
