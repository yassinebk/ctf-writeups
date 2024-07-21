package com.google.gson.internal;
/* renamed from: com.google.gson.internal.$Gson$Preconditions  reason: invalid class name */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/$Gson$Preconditions.class */
public final class C$Gson$Preconditions {
    private C$Gson$Preconditions() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }
}
