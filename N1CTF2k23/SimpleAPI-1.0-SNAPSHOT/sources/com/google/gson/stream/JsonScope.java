package com.google.gson.stream;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/stream/JsonScope.class */
final class JsonScope {
    static final int EMPTY_ARRAY = 1;
    static final int NONEMPTY_ARRAY = 2;
    static final int EMPTY_OBJECT = 3;
    static final int DANGLING_NAME = 4;
    static final int NONEMPTY_OBJECT = 5;
    static final int EMPTY_DOCUMENT = 6;
    static final int NONEMPTY_DOCUMENT = 7;
    static final int CLOSED = 8;

    JsonScope() {
    }
}
