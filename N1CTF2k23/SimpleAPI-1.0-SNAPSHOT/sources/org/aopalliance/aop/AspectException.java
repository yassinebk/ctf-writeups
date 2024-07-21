package org.aopalliance.aop;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/aopalliance/aop/AspectException.class */
public class AspectException extends RuntimeException {
    public AspectException(String message) {
        super(message);
    }

    public AspectException(String message, Throwable cause) {
        super(message, cause);
    }
}
