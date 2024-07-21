package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/MergedAnnotationCollectors.class */
public abstract class MergedAnnotationCollectors {
    private static final Collector.Characteristics[] NO_CHARACTERISTICS = new Collector.Characteristics[0];
    private static final Collector.Characteristics[] IDENTITY_FINISH_CHARACTERISTICS = {Collector.Characteristics.IDENTITY_FINISH};

    private MergedAnnotationCollectors() {
    }

    public static <A extends Annotation> Collector<MergedAnnotation<A>, ?, Set<A>> toAnnotationSet() {
        return Collector.of(ArrayList::new, list, annotation -> {
            list.add(annotation.synthesize());
        }, (v0, v1) -> {
            return addAll(v0, v1);
        }, (v1) -> {
            return new LinkedHashSet(v1);
        }, new Collector.Characteristics[0]);
    }

    public static <A extends Annotation> Collector<MergedAnnotation<A>, ?, Annotation[]> toAnnotationArray() {
        return toAnnotationArray(x$0 -> {
            return new Annotation[x$0];
        });
    }

    public static <R extends Annotation, A extends R> Collector<MergedAnnotation<A>, ?, R[]> toAnnotationArray(IntFunction<R[]> generator) {
        return Collector.of(ArrayList::new, list, annotation -> {
            list.add(annotation.synthesize());
        }, (v0, v1) -> {
            return addAll(v0, v1);
        }, list2 -> {
            return (Annotation[]) list2.toArray((Object[]) generator.apply(list2.size()));
        }, new Collector.Characteristics[0]);
    }

    public static <A extends Annotation> Collector<MergedAnnotation<A>, ?, MultiValueMap<String, Object>> toMultiValueMap(MergedAnnotation.Adapt... adaptations) {
        return toMultiValueMap(Function.identity(), adaptations);
    }

    public static <A extends Annotation> Collector<MergedAnnotation<A>, ?, MultiValueMap<String, Object>> toMultiValueMap(Function<MultiValueMap<String, Object>, MultiValueMap<String, Object>> finisher, MergedAnnotation.Adapt... adaptations) {
        Collector.Characteristics[] characteristics = isSameInstance(finisher, Function.identity()) ? IDENTITY_FINISH_CHARACTERISTICS : NO_CHARACTERISTICS;
        return Collector.of(LinkedMultiValueMap::new, map, annotation -> {
            Map<String, Object> asMap = annotation.asMap(adaptations);
            map.getClass();
            asMap.forEach((v1, v2) -> {
                r1.add(v1, v2);
            });
        }, MergedAnnotationCollectors::merge, finisher, characteristics);
    }

    private static boolean isSameInstance(Object instance, Object candidate) {
        return instance == candidate;
    }

    private static <E, L extends List<E>> L addAll(L list, L additions) {
        list.addAll(additions);
        return list;
    }

    private static <K, V> MultiValueMap<K, V> merge(MultiValueMap<K, V> map, MultiValueMap<K, V> additions) {
        map.addAll(additions);
        return map;
    }
}
