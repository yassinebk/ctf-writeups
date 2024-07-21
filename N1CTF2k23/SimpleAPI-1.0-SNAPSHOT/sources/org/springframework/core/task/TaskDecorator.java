package org.springframework.core.task;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/task/TaskDecorator.class */
public interface TaskDecorator {
    Runnable decorate(Runnable runnable);
}