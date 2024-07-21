package org.springframework.core.annotation;

import org.springframework.core.NestedRuntimeException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/AnnotationConfigurationException.class */
public class AnnotationConfigurationException extends NestedRuntimeException {
    public AnnotationConfigurationException(String message) {
        super(message);
    }

    public AnnotationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
