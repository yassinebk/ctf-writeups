package org.springframework.aop;

import org.springframework.core.NestedRuntimeException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/AopInvocationException.class */
public class AopInvocationException extends NestedRuntimeException {
    public AopInvocationException(String msg) {
        super(msg);
    }

    public AopInvocationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
