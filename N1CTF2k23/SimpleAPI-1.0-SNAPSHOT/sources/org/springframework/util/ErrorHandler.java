package org.springframework.util;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/ErrorHandler.class */
public interface ErrorHandler {
    void handleError(Throwable th);
}
