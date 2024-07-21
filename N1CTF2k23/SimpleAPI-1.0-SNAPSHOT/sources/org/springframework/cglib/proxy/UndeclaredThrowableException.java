package org.springframework.cglib.proxy;

import org.springframework.cglib.core.CodeGenerationException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/proxy/UndeclaredThrowableException.class */
public class UndeclaredThrowableException extends CodeGenerationException {
    public UndeclaredThrowableException(Throwable t) {
        super(t);
    }

    public Throwable getUndeclaredThrowable() {
        return getCause();
    }
}
