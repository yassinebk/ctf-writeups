package org.springframework.context.annotation;

import org.springframework.beans.factory.Aware;
import org.springframework.core.type.AnnotationMetadata;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/ImportAware.class */
public interface ImportAware extends Aware {
    void setImportMetadata(AnnotationMetadata annotationMetadata);
}
