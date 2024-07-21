package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import java.lang.reflect.Type;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/type/TypeModifier.class */
public abstract class TypeModifier {
    public abstract JavaType modifyType(JavaType javaType, Type type, TypeBindings typeBindings, TypeFactory typeFactory);
}
