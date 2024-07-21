package com.google.gson;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/ExclusionStrategy.class */
public interface ExclusionStrategy {
    boolean shouldSkipField(FieldAttributes fieldAttributes);

    boolean shouldSkipClass(Class<?> cls);
}
