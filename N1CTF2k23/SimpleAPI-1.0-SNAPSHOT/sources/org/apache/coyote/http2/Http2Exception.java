package org.apache.coyote.http2;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http2/Http2Exception.class */
abstract class Http2Exception extends Exception {
    private static final long serialVersionUID = 1;
    private final Http2Error error;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Http2Exception(String msg, Http2Error error) {
        super(msg);
        this.error = error;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Http2Exception(String msg, Http2Error error, Throwable cause) {
        super(msg, cause);
        this.error = error;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Http2Error getError() {
        return this.error;
    }
}
