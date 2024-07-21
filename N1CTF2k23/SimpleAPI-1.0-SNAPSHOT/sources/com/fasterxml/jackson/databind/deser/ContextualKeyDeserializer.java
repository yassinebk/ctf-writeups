package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/deser/ContextualKeyDeserializer.class */
public interface ContextualKeyDeserializer {
    KeyDeserializer createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException;
}
