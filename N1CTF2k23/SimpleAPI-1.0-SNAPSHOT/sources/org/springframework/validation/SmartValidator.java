package org.springframework.validation;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/validation/SmartValidator.class */
public interface SmartValidator extends Validator {
    void validate(Object obj, Errors errors, Object... objArr);

    default void validateValue(Class<?> targetType, String fieldName, @Nullable Object value, Errors errors, Object... validationHints) {
        throw new IllegalArgumentException("Cannot validate individual value for " + targetType);
    }
}
