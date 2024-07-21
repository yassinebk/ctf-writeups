package org.springframework.core.type.classreading;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/classreading/MetadataReader.class */
public interface MetadataReader {
    Resource getResource();

    ClassMetadata getClassMetadata();

    AnnotationMetadata getAnnotationMetadata();
}
