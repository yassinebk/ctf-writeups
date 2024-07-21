package org.springframework.validation;

import org.springframework.beans.PropertyAccessException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/validation/BindingErrorProcessor.class */
public interface BindingErrorProcessor {
    void processMissingFieldError(String str, BindingResult bindingResult);

    void processPropertyAccessException(PropertyAccessException propertyAccessException, BindingResult bindingResult);
}
