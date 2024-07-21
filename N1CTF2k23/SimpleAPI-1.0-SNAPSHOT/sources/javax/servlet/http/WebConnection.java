package javax.servlet.http;

import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/http/WebConnection.class */
public interface WebConnection extends AutoCloseable {
    ServletInputStream getInputStream() throws IOException;

    ServletOutputStream getOutputStream() throws IOException;
}
