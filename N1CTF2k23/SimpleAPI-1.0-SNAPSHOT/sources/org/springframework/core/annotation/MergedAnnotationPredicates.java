package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/MergedAnnotationPredicates.class */
public abstract class MergedAnnotationPredicates {
    private MergedAnnotationPredicates() {
    }

    public static <A extends Annotation> Predicate<MergedAnnotation<? extends A>> typeIn(String... typeNames) {
        return annotation -> {
            return ObjectUtils.containsElement(typeNames, annotation.getType().getName());
        };
    }

    public static <A extends Annotation> Predicate<MergedAnnotation<? extends A>> typeIn(Class<?>... types) {
        return annotation -> {
            return ObjectUtils.containsElement(types, annotation.getType());
        };
    }

    public static <A extends Annotation> Predicate<MergedAnnotation<? extends A>> typeIn(Collection<?> types) {
        return annotation -> {
            return types.stream().map(type -> {
                return type instanceof Class ? ((Class) type).getName() : type.toString();
            }).anyMatch(typeName -> {
                return typeName.equals(annotation.getType().getName());
            });
        };
    }

    public static <A extends Annotation> Predicate<MergedAnnotation<A>> firstRunOf(Function<? super MergedAnnotation<A>, ?> valueExtractor) {
        return new FirstRunOfPredicate(valueExtractor);
    }

    public static <A extends Annotation, K> Predicate<MergedAnnotation<A>> unique(Function<? super MergedAnnotation<A>, K> keyExtractor) {
        return new UniquePredicate(keyExtractor);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/MergedAnnotationPredicates$FirstRunOfPredicate.class */
    private static class FirstRunOfPredicate<A extends Annotation> implements Predicate<MergedAnnotation<A>> {
        private final Function<? super MergedAnnotation<A>, ?> valueExtractor;
        private boolean hasLastValue;
        @Nullable
        private Object lastValue;

        @Override // java.util.function.Predicate
        public /* bridge */ /* synthetic */ boolean test(@Nullable Object obj) {
            return test((MergedAnnotation) ((MergedAnnotation) obj));
        }

        FirstRunOfPredicate(Function<? super MergedAnnotation<A>, ?> valueExtractor) {
            Assert.notNull(valueExtractor, "Value extractor must not be null");
            this.valueExtractor = valueExtractor;
        }

        public boolean test(@Nullable MergedAnnotation<A> annotation) {
            if (!this.hasLastValue) {
                this.hasLastValue = true;
                this.lastValue = this.valueExtractor.apply(annotation);
            }
            Object value = this.valueExtractor.apply(annotation);
            return ObjectUtils.nullSafeEquals(value, this.lastValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/MergedAnnotationPredicates$UniquePredicate.class */
    public static class UniquePredicate<A extends Annotation, K> implements Predicate<MergedAnnotation<A>> {
        private final Function<? super MergedAnnotation<A>, K> keyExtractor;
        private final Set<K> seen = new HashSet();

        @Override // java.util.function.Predicate
        public /* bridge */ /* synthetic */ boolean test(@Nullable Object obj) {
            return test((MergedAnnotation) ((MergedAnnotation) obj));
        }

        UniquePredicate(Function<? super MergedAnnotation<A>, K> keyExtractor) {
            Assert.notNull(keyExtractor, "Key extractor must not be null");
            this.keyExtractor = keyExtractor;
        }

        public boolean test(@Nullable MergedAnnotation<A> annotation) {
            K key = this.keyExtractor.apply(annotation);
            return this.seen.add(key);
        }
    }
}
