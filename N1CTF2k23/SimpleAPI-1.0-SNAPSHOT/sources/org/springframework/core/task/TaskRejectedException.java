package org.springframework.core.task;

import java.util.concurrent.RejectedExecutionException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/task/TaskRejectedException.class */
public class TaskRejectedException extends RejectedExecutionException {
    public TaskRejectedException(String msg) {
        super(msg);
    }

    public TaskRejectedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
