package org.springframework.context.annotation;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/AnnotationConfigRegistry.class */
public interface AnnotationConfigRegistry {
    void register(Class<?>... clsArr);

    void scan(String... strArr);
}
