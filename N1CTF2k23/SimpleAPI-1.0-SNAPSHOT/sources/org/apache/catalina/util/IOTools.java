package org.apache.catalina.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/util/IOTools.class */
public class IOTools {
    protected static final int DEFAULT_BUFFER_SIZE = 4096;

    private IOTools() {
    }

    public static void flow(Reader reader, Writer writer, char[] buf) throws IOException {
        while (true) {
            int numRead = reader.read(buf);
            if (numRead >= 0) {
                writer.write(buf, 0, numRead);
            } else {
                return;
            }
        }
    }

    public static void flow(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[4096];
        flow(reader, writer, buf);
    }

    public static void flow(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[4096];
        while (true) {
            int numRead = is.read(buf);
            if (numRead >= 0) {
                if (os != null) {
                    os.write(buf, 0, numRead);
                }
            } else {
                return;
            }
        }
    }

    public static int readFully(InputStream is, byte[] buf) throws IOException {
        int bytesRead;
        int read;
        int i = 0;
        while (true) {
            bytesRead = i;
            if (bytesRead >= buf.length || (read = is.read(buf, bytesRead, buf.length - bytesRead)) < 0) {
                break;
            }
            i = bytesRead + read;
        }
        return bytesRead;
    }
}
