package com.fasterxml.jackson.annotation;

import java.lang.annotation.Annotation;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-annotations-2.11.0.jar:com/fasterxml/jackson/annotation/JacksonAnnotationValue.class */
public interface JacksonAnnotationValue<A extends Annotation> {
    Class<A> valueFor();
}
