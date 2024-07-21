package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/spi/LocationAwareLogger.class */
public interface LocationAwareLogger {
    void logMessage(Level level, Marker marker, String str, StackTraceElement stackTraceElement, Message message, Throwable th);
}
