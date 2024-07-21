package org.springframework.core.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/IntrospectionFailureLogger.class */
enum IntrospectionFailureLogger {
    DEBUG { // from class: org.springframework.core.annotation.IntrospectionFailureLogger.1
        @Override // org.springframework.core.annotation.IntrospectionFailureLogger
        public boolean isEnabled() {
            return IntrospectionFailureLogger.access$100().isDebugEnabled();
        }

        @Override // org.springframework.core.annotation.IntrospectionFailureLogger
        public void log(String message) {
            IntrospectionFailureLogger.access$100().debug(message);
        }
    },
    INFO { // from class: org.springframework.core.annotation.IntrospectionFailureLogger.2
        @Override // org.springframework.core.annotation.IntrospectionFailureLogger
        public boolean isEnabled() {
            return IntrospectionFailureLogger.access$100().isInfoEnabled();
        }

        @Override // org.springframework.core.annotation.IntrospectionFailureLogger
        public void log(String message) {
            IntrospectionFailureLogger.access$100().info(message);
        }
    };
    
    @Nullable
    private static Log logger;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean isEnabled();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void log(String str);

    static /* synthetic */ Log access$100() {
        return getLogger();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void log(String message, @Nullable Object source, Exception ex) {
        String on = source != null ? " on " + source : "";
        log(message + on + ": " + ex);
    }

    private static Log getLogger() {
        Log logger2 = logger;
        if (logger2 == null) {
            logger2 = LogFactory.getLog(MergedAnnotation.class);
            logger = logger2;
        }
        return logger2;
    }
}
