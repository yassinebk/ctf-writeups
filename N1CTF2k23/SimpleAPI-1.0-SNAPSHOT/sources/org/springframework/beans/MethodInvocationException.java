package org.springframework.beans;

import java.beans.PropertyChangeEvent;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/MethodInvocationException.class */
public class MethodInvocationException extends PropertyAccessException {
    public static final String ERROR_CODE = "methodInvocation";

    public MethodInvocationException(PropertyChangeEvent propertyChangeEvent, Throwable cause) {
        super(propertyChangeEvent, "Property '" + propertyChangeEvent.getPropertyName() + "' threw exception", cause);
    }

    @Override // org.springframework.beans.PropertyAccessException
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
