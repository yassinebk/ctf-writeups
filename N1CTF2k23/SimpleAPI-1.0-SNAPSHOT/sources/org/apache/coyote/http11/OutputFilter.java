package org.apache.coyote.http11;

import org.apache.coyote.Response;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http11/OutputFilter.class */
public interface OutputFilter extends HttpOutputBuffer {
    void setResponse(Response response);

    void recycle();

    void setBuffer(HttpOutputBuffer httpOutputBuffer);
}
