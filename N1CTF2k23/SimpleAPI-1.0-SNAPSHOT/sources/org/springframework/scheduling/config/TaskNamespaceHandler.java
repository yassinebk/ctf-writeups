package org.springframework.scheduling.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/config/TaskNamespaceHandler.class */
public class TaskNamespaceHandler extends NamespaceHandlerSupport {
    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
        registerBeanDefinitionParser("executor", new ExecutorBeanDefinitionParser());
        registerBeanDefinitionParser("scheduled-tasks", new ScheduledTasksBeanDefinitionParser());
        registerBeanDefinitionParser("scheduler", new SchedulerBeanDefinitionParser());
    }
}
