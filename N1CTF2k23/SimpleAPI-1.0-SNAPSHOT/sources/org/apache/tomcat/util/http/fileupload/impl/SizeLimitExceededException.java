package org.apache.tomcat.util.http.fileupload.impl;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/fileupload/impl/SizeLimitExceededException.class */
public class SizeLimitExceededException extends SizeException {
    private static final long serialVersionUID = -2474893167098052828L;

    public SizeLimitExceededException(String message, long actual, long permitted) {
        super(message, actual, permitted);
    }
}
