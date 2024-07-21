package com.google.gson;

import java.lang.reflect.Type;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/JsonSerializer.class */
public interface JsonSerializer<T> {
    JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext);
}
