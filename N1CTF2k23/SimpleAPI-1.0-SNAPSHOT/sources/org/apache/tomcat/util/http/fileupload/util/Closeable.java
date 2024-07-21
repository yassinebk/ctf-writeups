package org.apache.tomcat.util.http.fileupload.util;

import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/fileupload/util/Closeable.class */
public interface Closeable {
    void close() throws IOException;

    boolean isClosed() throws IOException;
}
