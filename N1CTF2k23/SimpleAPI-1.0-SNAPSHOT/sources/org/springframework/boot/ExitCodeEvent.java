package org.springframework.boot;

import org.springframework.context.ApplicationEvent;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/ExitCodeEvent.class */
public class ExitCodeEvent extends ApplicationEvent {
    private final int exitCode;

    public ExitCodeEvent(Object source, int exitCode) {
        super(source);
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return this.exitCode;
    }
}
