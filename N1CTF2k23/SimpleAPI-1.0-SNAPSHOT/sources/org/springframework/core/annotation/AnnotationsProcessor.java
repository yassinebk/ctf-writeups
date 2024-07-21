package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/AnnotationsProcessor.class */
interface AnnotationsProcessor<C, R> {
    @Nullable
    R doWithAnnotations(C c, int i, @Nullable Object obj, Annotation[] annotationArr);

    @Nullable
    default R doWithAggregate(C context, int aggregateIndex) {
        return null;
    }

    @Nullable
    default R finish(@Nullable R result) {
        return result;
    }
}
