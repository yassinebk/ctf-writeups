package org.springframework.core;

import java.lang.annotation.Annotation;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/KotlinDetector.class */
public abstract class KotlinDetector {
    @Nullable
    private static final Class<? extends Annotation> kotlinMetadata;
    private static final boolean kotlinReflectPresent;

    /* JADX WARN: Multi-variable type inference failed */
    static {
        Class<?> metadata;
        ClassLoader classLoader = KotlinDetector.class.getClassLoader();
        try {
            metadata = ClassUtils.forName("kotlin.Metadata", classLoader);
        } catch (ClassNotFoundException e) {
            metadata = null;
        }
        kotlinMetadata = metadata;
        kotlinReflectPresent = ClassUtils.isPresent("kotlin.reflect.full.KClasses", classLoader);
    }

    public static boolean isKotlinPresent() {
        return kotlinMetadata != null;
    }

    public static boolean isKotlinReflectPresent() {
        return kotlinReflectPresent;
    }

    public static boolean isKotlinType(Class<?> clazz) {
        return (kotlinMetadata == null || clazz.getDeclaredAnnotation(kotlinMetadata) == null) ? false : true;
    }
}
