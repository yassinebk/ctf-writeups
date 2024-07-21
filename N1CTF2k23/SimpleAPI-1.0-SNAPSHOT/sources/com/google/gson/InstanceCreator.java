package com.google.gson;

import java.lang.reflect.Type;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/InstanceCreator.class */
public interface InstanceCreator<T> {
    T createInstance(Type type);
}
