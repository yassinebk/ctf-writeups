package org.springframework.boot.autoconfigure.quartz;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/quartz/SchedulerFactoryBeanCustomizer.class */
public interface SchedulerFactoryBeanCustomizer {
    void customize(SchedulerFactoryBean schedulerFactoryBean);
}
