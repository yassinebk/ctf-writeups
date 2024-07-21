package org.apache.tomcat.util.http.fileupload;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/fileupload/FileUploadException.class */
public class FileUploadException extends Exception {
    private static final long serialVersionUID = -4222909057964038517L;

    public FileUploadException() {
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(Throwable cause) {
        super(cause);
    }
}
