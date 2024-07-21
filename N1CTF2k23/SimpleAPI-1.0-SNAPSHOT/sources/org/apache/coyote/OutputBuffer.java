package org.apache.coyote;

import java.io.IOException;
import java.nio.ByteBuffer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/OutputBuffer.class */
public interface OutputBuffer {
    int doWrite(ByteBuffer byteBuffer) throws IOException;

    long getBytesWritten();
}
