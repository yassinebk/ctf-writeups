package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
@JacksonStdImpl
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/ser/std/ToStringSerializer.class */
public class ToStringSerializer extends ToStringSerializerBase {
    public static final ToStringSerializer instance = new ToStringSerializer();

    public ToStringSerializer() {
        super(Object.class);
    }

    public ToStringSerializer(Class<?> handledType) {
        super(handledType);
    }

    @Override // com.fasterxml.jackson.databind.ser.std.ToStringSerializerBase
    public final String valueToString(Object value) {
        return value.toString();
    }
}
