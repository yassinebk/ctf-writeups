package org.apache.tomcat.util.http.fileupload.impl;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/fileupload/impl/SizeException.class */
public abstract class SizeException extends FileUploadException {
    private static final long serialVersionUID = -8776225574705254126L;
    private final long actual;
    private final long permitted;

    /* JADX INFO: Access modifiers changed from: protected */
    public SizeException(String message, long actual, long permitted) {
        super(message);
        this.actual = actual;
        this.permitted = permitted;
    }

    public long getActualSize() {
        return this.actual;
    }

    public long getPermittedSize() {
        return this.permitted;
    }
}
