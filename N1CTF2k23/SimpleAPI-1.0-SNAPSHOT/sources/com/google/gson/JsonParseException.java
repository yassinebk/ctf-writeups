package com.google.gson;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/JsonParseException.class */
public class JsonParseException extends RuntimeException {
    static final long serialVersionUID = -4086729973971783390L;

    public JsonParseException(String msg) {
        super(msg);
    }

    public JsonParseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JsonParseException(Throwable cause) {
        super(cause);
    }
}
