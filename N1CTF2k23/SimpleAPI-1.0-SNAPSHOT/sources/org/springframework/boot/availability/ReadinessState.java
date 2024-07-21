package org.springframework.boot.availability;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/availability/ReadinessState.class */
public enum ReadinessState implements AvailabilityState {
    ACCEPTING_TRAFFIC,
    REFUSING_TRAFFIC
}
