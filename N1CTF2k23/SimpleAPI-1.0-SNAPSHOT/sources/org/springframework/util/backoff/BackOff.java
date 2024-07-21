package org.springframework.util.backoff;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/backoff/BackOff.class */
public interface BackOff {
    BackOffExecution start();
}
