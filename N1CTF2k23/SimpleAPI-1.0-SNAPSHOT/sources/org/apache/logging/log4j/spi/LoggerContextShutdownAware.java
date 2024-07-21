package org.apache.logging.log4j.spi;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/spi/LoggerContextShutdownAware.class */
public interface LoggerContextShutdownAware {
    void contextShutdown(LoggerContext loggerContext);
}
