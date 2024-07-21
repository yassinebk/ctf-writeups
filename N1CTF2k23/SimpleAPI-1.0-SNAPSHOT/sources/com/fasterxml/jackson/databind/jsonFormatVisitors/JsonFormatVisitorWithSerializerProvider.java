package com.fasterxml.jackson.databind.jsonFormatVisitors;

import com.fasterxml.jackson.databind.SerializerProvider;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/jsonFormatVisitors/JsonFormatVisitorWithSerializerProvider.class */
public interface JsonFormatVisitorWithSerializerProvider {
    SerializerProvider getProvider();

    void setProvider(SerializerProvider serializerProvider);
}
