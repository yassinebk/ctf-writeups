package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/MergedAnnotationSelector.class */
public interface MergedAnnotationSelector<A extends Annotation> {
    MergedAnnotation<A> select(MergedAnnotation<A> mergedAnnotation, MergedAnnotation<A> mergedAnnotation2);

    default boolean isBestCandidate(MergedAnnotation<A> annotation) {
        return false;
    }
}
