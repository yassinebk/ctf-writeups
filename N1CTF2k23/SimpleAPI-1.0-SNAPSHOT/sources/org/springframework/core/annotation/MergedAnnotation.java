package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/MergedAnnotation.class */
public interface MergedAnnotation<A extends Annotation> {
    public static final String VALUE = "value";

    Class<A> getType();

    boolean isPresent();

    boolean isDirectlyPresent();

    boolean isMetaPresent();

    int getDistance();

    int getAggregateIndex();

    @Nullable
    Object getSource();

    @Nullable
    MergedAnnotation<?> getMetaSource();

    MergedAnnotation<?> getRoot();

    List<Class<? extends Annotation>> getMetaTypes();

    boolean hasNonDefaultValue(String str);

    boolean hasDefaultValue(String str) throws NoSuchElementException;

    byte getByte(String str) throws NoSuchElementException;

    byte[] getByteArray(String str) throws NoSuchElementException;

    boolean getBoolean(String str) throws NoSuchElementException;

    boolean[] getBooleanArray(String str) throws NoSuchElementException;

    char getChar(String str) throws NoSuchElementException;

    char[] getCharArray(String str) throws NoSuchElementException;

    short getShort(String str) throws NoSuchElementException;

    short[] getShortArray(String str) throws NoSuchElementException;

    int getInt(String str) throws NoSuchElementException;

    int[] getIntArray(String str) throws NoSuchElementException;

    long getLong(String str) throws NoSuchElementException;

    long[] getLongArray(String str) throws NoSuchElementException;

    double getDouble(String str) throws NoSuchElementException;

    double[] getDoubleArray(String str) throws NoSuchElementException;

    float getFloat(String str) throws NoSuchElementException;

    float[] getFloatArray(String str) throws NoSuchElementException;

    String getString(String str) throws NoSuchElementException;

    String[] getStringArray(String str) throws NoSuchElementException;

    Class<?> getClass(String str) throws NoSuchElementException;

    Class<?>[] getClassArray(String str) throws NoSuchElementException;

    <E extends Enum<E>> E getEnum(String str, Class<E> cls) throws NoSuchElementException;

    <E extends Enum<E>> E[] getEnumArray(String str, Class<E> cls) throws NoSuchElementException;

    <T extends Annotation> MergedAnnotation<T> getAnnotation(String str, Class<T> cls) throws NoSuchElementException;

    <T extends Annotation> MergedAnnotation<T>[] getAnnotationArray(String str, Class<T> cls) throws NoSuchElementException;

    Optional<Object> getValue(String str);

    <T> Optional<T> getValue(String str, Class<T> cls);

    Optional<Object> getDefaultValue(String str);

    <T> Optional<T> getDefaultValue(String str, Class<T> cls);

    MergedAnnotation<A> filterDefaultValues();

    MergedAnnotation<A> filterAttributes(Predicate<String> predicate);

    MergedAnnotation<A> withNonMergedAttributes();

    AnnotationAttributes asAnnotationAttributes(Adapt... adaptArr);

    Map<String, Object> asMap(Adapt... adaptArr);

    <T extends Map<String, Object>> T asMap(Function<MergedAnnotation<?>, T> function, Adapt... adaptArr);

    A synthesize() throws NoSuchElementException;

    Optional<A> synthesize(Predicate<? super MergedAnnotation<A>> predicate) throws NoSuchElementException;

    static <A extends Annotation> MergedAnnotation<A> missing() {
        return MissingMergedAnnotation.getInstance();
    }

    static <A extends Annotation> MergedAnnotation<A> from(A annotation) {
        return from(null, annotation);
    }

    static <A extends Annotation> MergedAnnotation<A> from(@Nullable Object source, A annotation) {
        return TypeMappedAnnotation.from(source, annotation);
    }

    static <A extends Annotation> MergedAnnotation<A> of(Class<A> annotationType) {
        return of(null, annotationType, null);
    }

    static <A extends Annotation> MergedAnnotation<A> of(Class<A> annotationType, @Nullable Map<String, ?> attributes) {
        return of(null, annotationType, attributes);
    }

    static <A extends Annotation> MergedAnnotation<A> of(@Nullable AnnotatedElement source, Class<A> annotationType, @Nullable Map<String, ?> attributes) {
        return of(null, source, annotationType, attributes);
    }

    static <A extends Annotation> MergedAnnotation<A> of(@Nullable ClassLoader classLoader, @Nullable Object source, Class<A> annotationType, @Nullable Map<String, ?> attributes) {
        return TypeMappedAnnotation.of(classLoader, source, annotationType, attributes);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/MergedAnnotation$Adapt.class */
    public enum Adapt {
        CLASS_TO_STRING,
        ANNOTATION_TO_MAP;

        /* JADX INFO: Access modifiers changed from: protected */
        public final boolean isIn(Adapt... adaptations) {
            for (Adapt candidate : adaptations) {
                if (candidate == this) {
                    return true;
                }
            }
            return false;
        }

        public static Adapt[] values(boolean classToString, boolean annotationsToMap) {
            EnumSet<Adapt> result = EnumSet.noneOf(Adapt.class);
            addIfTrue(result, CLASS_TO_STRING, classToString);
            addIfTrue(result, ANNOTATION_TO_MAP, annotationsToMap);
            return (Adapt[]) result.toArray(new Adapt[0]);
        }

        private static <T> void addIfTrue(Set<T> result, T value, boolean test) {
            if (test) {
                result.add(value);
            }
        }
    }
}
