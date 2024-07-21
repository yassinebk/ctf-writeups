package org.springframework.remoting;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/remoting/RemoteLookupFailureException.class */
public class RemoteLookupFailureException extends RemoteAccessException {
    public RemoteLookupFailureException(String msg) {
        super(msg);
    }

    public RemoteLookupFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
