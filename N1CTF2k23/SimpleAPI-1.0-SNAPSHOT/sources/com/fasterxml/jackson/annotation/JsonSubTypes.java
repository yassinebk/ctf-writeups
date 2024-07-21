package com.fasterxml.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-annotations-2.11.0.jar:com/fasterxml/jackson/annotation/JsonSubTypes.class */
public @interface JsonSubTypes {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-annotations-2.11.0.jar:com/fasterxml/jackson/annotation/JsonSubTypes$Type.class */
    public @interface Type {
        Class<?> value();

        String name() default "";
    }

    Type[] value();
}