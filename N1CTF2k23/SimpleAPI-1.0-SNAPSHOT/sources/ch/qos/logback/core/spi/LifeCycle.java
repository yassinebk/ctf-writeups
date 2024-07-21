package ch.qos.logback.core.spi;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/spi/LifeCycle.class */
public interface LifeCycle {
    void start();

    void stop();

    boolean isStarted();
}