package org.apache.tomcat.util.net;

import java.nio.ByteBuffer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/ApplicationBufferHandler.class */
public interface ApplicationBufferHandler {
    void setByteBuffer(ByteBuffer byteBuffer);

    ByteBuffer getByteBuffer();

    void expand(int i);
}
