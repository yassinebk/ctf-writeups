package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.spi.ContextAware;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-classic-1.2.3.jar:ch/qos/logback/classic/spi/LoggerContextAware.class */
public interface LoggerContextAware extends ContextAware {
    void setLoggerContext(LoggerContext loggerContext);
}
