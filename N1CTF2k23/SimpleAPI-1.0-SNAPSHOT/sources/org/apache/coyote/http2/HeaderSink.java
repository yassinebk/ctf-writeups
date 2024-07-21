package org.apache.coyote.http2;

import org.apache.coyote.http2.HpackDecoder;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http2/HeaderSink.class */
class HeaderSink implements HpackDecoder.HeaderEmitter {
    @Override // org.apache.coyote.http2.HpackDecoder.HeaderEmitter
    public void emitHeader(String name, String value) {
    }

    @Override // org.apache.coyote.http2.HpackDecoder.HeaderEmitter
    public void validateHeaders() throws StreamException {
    }

    @Override // org.apache.coyote.http2.HpackDecoder.HeaderEmitter
    public void setHeaderException(StreamException streamException) {
    }
}
