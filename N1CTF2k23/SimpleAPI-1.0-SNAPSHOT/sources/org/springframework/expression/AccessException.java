package org.springframework.expression;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/AccessException.class */
public class AccessException extends Exception {
    public AccessException(String message) {
        super(message);
    }

    public AccessException(String message, Exception cause) {
        super(message, cause);
    }
}
