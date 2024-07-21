package com.fasterxml.jackson.databind.jsontype;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import java.util.Collection;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/jsontype/TypeResolverBuilder.class */
public interface TypeResolverBuilder<T extends TypeResolverBuilder<T>> {
    Class<?> getDefaultImpl();

    TypeSerializer buildTypeSerializer(SerializationConfig serializationConfig, JavaType javaType, Collection<NamedType> collection);

    TypeDeserializer buildTypeDeserializer(DeserializationConfig deserializationConfig, JavaType javaType, Collection<NamedType> collection);

    T init(JsonTypeInfo.Id id, TypeIdResolver typeIdResolver);

    T inclusion(JsonTypeInfo.As as);

    T typeProperty(String str);

    T defaultImpl(Class<?> cls);

    T typeIdVisibility(boolean z);
}
