package org.springframework.context.annotation;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/ImportRegistry.class */
interface ImportRegistry {
    @Nullable
    AnnotationMetadata getImportingClassFor(String str);

    void removeImportingClass(String str);
}
