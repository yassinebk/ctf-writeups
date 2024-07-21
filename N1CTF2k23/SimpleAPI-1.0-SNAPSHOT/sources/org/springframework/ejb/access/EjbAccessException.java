package org.springframework.ejb.access;

import org.springframework.core.NestedRuntimeException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/ejb/access/EjbAccessException.class */
public class EjbAccessException extends NestedRuntimeException {
    public EjbAccessException(String msg) {
        super(msg);
    }

    public EjbAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
