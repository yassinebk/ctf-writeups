package org.apache.logging.log4j.message;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/message/LoggerNameAwareMessage.class */
public interface LoggerNameAwareMessage {
    void setLoggerName(String str);

    String getLoggerName();
}
