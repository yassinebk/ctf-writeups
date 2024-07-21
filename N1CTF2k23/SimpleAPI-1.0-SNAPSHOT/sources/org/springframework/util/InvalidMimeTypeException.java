package org.springframework.util;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/InvalidMimeTypeException.class */
public class InvalidMimeTypeException extends IllegalArgumentException {
    private final String mimeType;

    public InvalidMimeTypeException(String mimeType, String message) {
        super("Invalid mime type \"" + mimeType + "\": " + message);
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return this.mimeType;
    }
}
