package org.springframework.context.annotation;

import java.util.function.Predicate;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/ImportSelector.class */
public interface ImportSelector {
    String[] selectImports(AnnotationMetadata annotationMetadata);

    @Nullable
    default Predicate<String> getExclusionFilter() {
        return null;
    }
}
