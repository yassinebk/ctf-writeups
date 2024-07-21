package org.apache.logging.log4j.spi;

import java.net.URI;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/spi/LoggerContextFactory.class */
public interface LoggerContextFactory {
    LoggerContext getContext(String str, ClassLoader classLoader, Object obj, boolean z);

    LoggerContext getContext(String str, ClassLoader classLoader, Object obj, boolean z, URI uri, String str2);

    void removeContext(LoggerContext loggerContext);

    default void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
        if (hasContext(fqcn, loader, currentContext)) {
            LoggerContext ctx = getContext(fqcn, loader, null, currentContext);
            if (ctx instanceof Terminable) {
                ((Terminable) ctx).terminate();
            }
        }
    }

    default boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
        return false;
    }
}
