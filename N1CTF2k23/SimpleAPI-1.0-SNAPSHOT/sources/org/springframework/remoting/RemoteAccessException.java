package org.springframework.remoting;

import org.springframework.core.NestedRuntimeException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/remoting/RemoteAccessException.class */
public class RemoteAccessException extends NestedRuntimeException {
    private static final long serialVersionUID = -4906825139312227864L;

    public RemoteAccessException(String msg) {
        super(msg);
    }

    public RemoteAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
