package ch.qos.logback.core.util;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/util/InvocationGate.class */
public interface InvocationGate {
    public static final long TIME_UNAVAILABLE = -1;

    boolean isTooSoon(long j);
}
