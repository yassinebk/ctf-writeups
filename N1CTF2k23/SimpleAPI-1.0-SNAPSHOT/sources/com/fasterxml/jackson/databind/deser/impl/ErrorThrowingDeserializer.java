package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/deser/impl/ErrorThrowingDeserializer.class */
public class ErrorThrowingDeserializer extends JsonDeserializer<Object> {
    private final Error _cause;

    public ErrorThrowingDeserializer(NoClassDefFoundError cause) {
        this._cause = cause;
    }

    @Override // com.fasterxml.jackson.databind.JsonDeserializer
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        throw this._cause;
    }
}
