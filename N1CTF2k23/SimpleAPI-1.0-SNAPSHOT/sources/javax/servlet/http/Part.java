package javax.servlet.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/http/Part.class */
public interface Part {
    InputStream getInputStream() throws IOException;

    String getContentType();

    String getName();

    String getSubmittedFileName();

    long getSize();

    void write(String str) throws IOException;

    void delete() throws IOException;

    String getHeader(String str);

    Collection<String> getHeaders(String str);

    Collection<String> getHeaderNames();
}
