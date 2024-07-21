package org.springframework.validation;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/validation/Validator.class */
public interface Validator {
    boolean supports(Class<?> cls);

    void validate(Object obj, Errors errors);
}
