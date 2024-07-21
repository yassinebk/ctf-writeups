package org.apache.tomcat.util.http.fileupload;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/fileupload/RequestContext.class */
public interface RequestContext {
    String getCharacterEncoding();

    String getContentType();

    InputStream getInputStream() throws IOException;
}
