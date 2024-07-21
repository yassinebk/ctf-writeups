package com.google.gson;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/JsonIOException.class */
public final class JsonIOException extends JsonParseException {
    private static final long serialVersionUID = 1;

    public JsonIOException(String msg) {
        super(msg);
    }

    public JsonIOException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JsonIOException(Throwable cause) {
        super(cause);
    }
}
