package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.MessageFactory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/spi/LoggerContext.class */
public interface LoggerContext {
    Object getExternalContext();

    ExtendedLogger getLogger(String str);

    ExtendedLogger getLogger(String str, MessageFactory messageFactory);

    boolean hasLogger(String str);

    boolean hasLogger(String str, MessageFactory messageFactory);

    boolean hasLogger(String str, Class<? extends MessageFactory> cls);

    default Object getObject(String key) {
        return null;
    }

    default Object putObject(String key, Object value) {
        return null;
    }

    default Object putObjectIfAbsent(String key, Object value) {
        return null;
    }

    default Object removeObject(String key) {
        return null;
    }

    default boolean removeObject(String key, Object value) {
        return false;
    }
}
