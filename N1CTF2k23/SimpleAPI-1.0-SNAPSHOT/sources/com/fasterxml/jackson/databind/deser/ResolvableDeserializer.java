package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/deser/ResolvableDeserializer.class */
public interface ResolvableDeserializer {
    void resolve(DeserializationContext deserializationContext) throws JsonMappingException;
}
