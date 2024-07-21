package org.springframework.remoting;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/remoting/RemoteTimeoutException.class */
public class RemoteTimeoutException extends RemoteAccessException {
    public RemoteTimeoutException(String msg) {
        super(msg);
    }

    public RemoteTimeoutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
