package org.apache.coyote.http2;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http2/ConnectionException.class */
class ConnectionException extends Http2Exception {
    private static final long serialVersionUID = 1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConnectionException(String msg, Http2Error error) {
        super(msg, error);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConnectionException(String msg, Http2Error error, Throwable cause) {
        super(msg, error, cause);
    }
}