package org.apache.tomcat.util.http.fileupload.impl;

import java.io.IOException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/fileupload/impl/FileUploadIOException.class */
public class FileUploadIOException extends IOException {
    private static final long serialVersionUID = -7047616958165584154L;
    private final FileUploadException cause;

    public FileUploadIOException(FileUploadException pCause) {
        this.cause = pCause;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
