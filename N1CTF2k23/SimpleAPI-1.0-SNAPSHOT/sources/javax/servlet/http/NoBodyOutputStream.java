package javax.servlet.http;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
/* compiled from: HttpServlet.java */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/http/NoBodyOutputStream.class */
class NoBodyOutputStream extends ServletOutputStream {
    private static final String LSTRING_FILE = "javax.servlet.http.LocalStrings";
    private static final ResourceBundle lStrings = ResourceBundle.getBundle(LSTRING_FILE);
    private final HttpServletResponse response;
    private boolean flushed = false;
    private int contentLength = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NoBodyOutputStream(HttpServletResponse response) {
        this.response = response;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getContentLength() {
        return this.contentLength;
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.contentLength++;
        checkCommit();
    }

    @Override // java.io.OutputStream
    public void write(byte[] buf, int offset, int len) throws IOException {
        if (buf == null) {
            throw new NullPointerException(lStrings.getString("err.io.nullArray"));
        }
        if (offset < 0 || len < 0 || offset + len > buf.length) {
            String msg = lStrings.getString("err.io.indexOutOfBounds");
            Object[] msgArgs = {Integer.valueOf(offset), Integer.valueOf(len), Integer.valueOf(buf.length)};
            throw new IndexOutOfBoundsException(MessageFormat.format(msg, msgArgs));
        }
        this.contentLength += len;
        checkCommit();
    }

    @Override // javax.servlet.ServletOutputStream
    public boolean isReady() {
        return false;
    }

    @Override // javax.servlet.ServletOutputStream
    public void setWriteListener(WriteListener listener) {
    }

    private void checkCommit() throws IOException {
        if (!this.flushed && this.contentLength > this.response.getBufferSize()) {
            this.response.flushBuffer();
            this.flushed = true;
        }
    }
}
