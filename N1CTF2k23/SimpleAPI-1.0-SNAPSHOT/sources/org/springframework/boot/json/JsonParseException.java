package org.springframework.boot.json;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/json/JsonParseException.class */
public class JsonParseException extends IllegalArgumentException {
    public JsonParseException() {
        this(null);
    }

    public JsonParseException(Throwable cause) {
        super("Cannot parse JSON", cause);
    }
}
