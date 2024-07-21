package org.springframework.boot.autoconfigure.context;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.DefaultLifecycleProcessor;
@EnableConfigurationProperties({LifecycleProperties.class})
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/context/LifecycleAutoConfiguration.class */
public class LifecycleAutoConfiguration {
    @ConditionalOnMissingBean(name = {AbstractApplicationContext.LIFECYCLE_PROCESSOR_BEAN_NAME})
    @Bean(name = {AbstractApplicationContext.LIFECYCLE_PROCESSOR_BEAN_NAME})
    public DefaultLifecycleProcessor defaultLifecycleProcessor(LifecycleProperties properties) {
        DefaultLifecycleProcessor lifecycleProcessor = new DefaultLifecycleProcessor();
        lifecycleProcessor.setTimeoutPerShutdownPhase(properties.getTimeoutPerShutdownPhase().toMillis());
        return lifecycleProcessor;
    }
}
