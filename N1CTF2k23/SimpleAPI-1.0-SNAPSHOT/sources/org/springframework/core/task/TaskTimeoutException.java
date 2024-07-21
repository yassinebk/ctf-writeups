package org.springframework.core.task;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/task/TaskTimeoutException.class */
public class TaskTimeoutException extends TaskRejectedException {
    public TaskTimeoutException(String msg) {
        super(msg);
    }

    public TaskTimeoutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
