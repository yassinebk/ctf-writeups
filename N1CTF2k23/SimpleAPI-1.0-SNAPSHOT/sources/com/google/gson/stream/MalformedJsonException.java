package com.google.gson.stream;

import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/stream/MalformedJsonException.class */
public final class MalformedJsonException extends IOException {
    private static final long serialVersionUID = 1;

    public MalformedJsonException(String msg) {
        super(msg);
    }

    public MalformedJsonException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public MalformedJsonException(Throwable throwable) {
        super(throwable);
    }
}
