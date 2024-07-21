package org.springframework.boot;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/SpringBootExceptionReporter.class */
public interface SpringBootExceptionReporter {
    boolean reportException(Throwable failure);
}
