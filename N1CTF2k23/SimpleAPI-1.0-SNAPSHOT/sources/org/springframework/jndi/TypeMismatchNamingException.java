package org.springframework.jndi;

import javax.naming.NamingException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jndi/TypeMismatchNamingException.class */
public class TypeMismatchNamingException extends NamingException {
    private final Class<?> requiredType;
    private final Class<?> actualType;

    public TypeMismatchNamingException(String jndiName, Class<?> requiredType, Class<?> actualType) {
        super("Object of type [" + actualType + "] available at JNDI location [" + jndiName + "] is not assignable to [" + requiredType.getName() + "]");
        this.requiredType = requiredType;
        this.actualType = actualType;
    }

    public final Class<?> getRequiredType() {
        return this.requiredType;
    }

    public final Class<?> getActualType() {
        return this.actualType;
    }
}
