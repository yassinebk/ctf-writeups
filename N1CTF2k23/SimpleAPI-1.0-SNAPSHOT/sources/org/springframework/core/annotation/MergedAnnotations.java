package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/MergedAnnotations.class */
public interface MergedAnnotations extends Iterable<MergedAnnotation<Annotation>> {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/MergedAnnotations$SearchStrategy.class */
    public enum SearchStrategy {
        DIRECT,
        INHERITED_ANNOTATIONS,
        SUPERCLASS,
        TYPE_HIERARCHY,
        TYPE_HIERARCHY_AND_ENCLOSING_CLASSES
    }

    <A extends Annotation> boolean isPresent(Class<A> cls);

    boolean isPresent(String str);

    <A extends Annotation> boolean isDirectlyPresent(Class<A> cls);

    boolean isDirectlyPresent(String str);

    <A extends Annotation> MergedAnnotation<A> get(Class<A> cls);

    <A extends Annotation> MergedAnnotation<A> get(Class<A> cls, @Nullable Predicate<? super MergedAnnotation<A>> predicate);

    <A extends Annotation> MergedAnnotation<A> get(Class<A> cls, @Nullable Predicate<? super MergedAnnotation<A>> predicate, @Nullable MergedAnnotationSelector<A> mergedAnnotationSelector);

    <A extends Annotation> MergedAnnotation<A> get(String str);

    <A extends Annotation> MergedAnnotation<A> get(String str, @Nullable Predicate<? super MergedAnnotation<A>> predicate);

    <A extends Annotation> MergedAnnotation<A> get(String str, @Nullable Predicate<? super MergedAnnotation<A>> predicate, @Nullable MergedAnnotationSelector<A> mergedAnnotationSelector);

    <A extends Annotation> Stream<MergedAnnotation<A>> stream(Class<A> cls);

    <A extends Annotation> Stream<MergedAnnotation<A>> stream(String str);

    Stream<MergedAnnotation<Annotation>> stream();

    static MergedAnnotations from(AnnotatedElement element) {
        return from(element, SearchStrategy.DIRECT);
    }

    static MergedAnnotations from(AnnotatedElement element, SearchStrategy searchStrategy) {
        return from(element, searchStrategy, RepeatableContainers.standardRepeatables());
    }

    static MergedAnnotations from(AnnotatedElement element, SearchStrategy searchStrategy, RepeatableContainers repeatableContainers) {
        return TypeMappedAnnotations.from(element, searchStrategy, repeatableContainers, AnnotationFilter.PLAIN);
    }

    static MergedAnnotations from(AnnotatedElement element, SearchStrategy searchStrategy, RepeatableContainers repeatableContainers, AnnotationFilter annotationFilter) {
        return TypeMappedAnnotations.from(element, searchStrategy, repeatableContainers, annotationFilter);
    }

    static MergedAnnotations from(Annotation... annotations) {
        return from(annotations, annotations);
    }

    static MergedAnnotations from(Object source, Annotation... annotations) {
        return from(source, annotations, RepeatableContainers.standardRepeatables());
    }

    static MergedAnnotations from(Object source, Annotation[] annotations, RepeatableContainers repeatableContainers) {
        return TypeMappedAnnotations.from(source, annotations, repeatableContainers, AnnotationFilter.PLAIN);
    }

    static MergedAnnotations from(Object source, Annotation[] annotations, RepeatableContainers repeatableContainers, AnnotationFilter annotationFilter) {
        return TypeMappedAnnotations.from(source, annotations, repeatableContainers, annotationFilter);
    }

    static MergedAnnotations of(Collection<MergedAnnotation<?>> annotations) {
        return MergedAnnotationsCollection.of(annotations);
    }
}
