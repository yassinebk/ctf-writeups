package org.springframework.scheduling.config;

import org.springframework.beans.factory.SmartInitializingSingleton;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/config/ContextLifecycleScheduledTaskRegistrar.class */
public class ContextLifecycleScheduledTaskRegistrar extends ScheduledTaskRegistrar implements SmartInitializingSingleton {
    @Override // org.springframework.scheduling.config.ScheduledTaskRegistrar, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
    }

    @Override // org.springframework.beans.factory.SmartInitializingSingleton
    public void afterSingletonsInstantiated() {
        scheduleTasks();
    }
}
