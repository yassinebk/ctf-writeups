package org.springframework.aop.aspectj.annotation;

import org.springframework.aop.framework.AopConfigException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/aspectj/annotation/NotAnAtAspectException.class */
public class NotAnAtAspectException extends AopConfigException {
    private final Class<?> nonAspectClass;

    public NotAnAtAspectException(Class<?> nonAspectClass) {
        super(nonAspectClass.getName() + " is not an @AspectJ aspect");
        this.nonAspectClass = nonAspectClass;
    }

    public Class<?> getNonAspectClass() {
        return this.nonAspectClass;
    }
}
