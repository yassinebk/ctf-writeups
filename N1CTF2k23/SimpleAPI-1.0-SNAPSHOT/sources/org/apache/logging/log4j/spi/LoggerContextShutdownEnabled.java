package org.apache.logging.log4j.spi;

import java.util.List;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/spi/LoggerContextShutdownEnabled.class */
public interface LoggerContextShutdownEnabled {
    void addShutdownListener(LoggerContextShutdownAware loggerContextShutdownAware);

    List<LoggerContextShutdownAware> getListeners();
}
