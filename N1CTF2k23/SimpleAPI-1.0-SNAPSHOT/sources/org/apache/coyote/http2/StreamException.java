package org.apache.coyote.http2;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http2/StreamException.class */
public class StreamException extends Http2Exception {
    private static final long serialVersionUID = 1;
    private final int streamId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StreamException(String msg, Http2Error error, int streamId) {
        super(msg, error);
        this.streamId = streamId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getStreamId() {
        return this.streamId;
    }
}
