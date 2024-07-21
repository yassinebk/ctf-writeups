package org.apache.coyote;

import java.io.IOException;
import org.apache.tomcat.util.net.ApplicationBufferHandler;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/InputBuffer.class */
public interface InputBuffer {
    int doRead(ApplicationBufferHandler applicationBufferHandler) throws IOException;
}
