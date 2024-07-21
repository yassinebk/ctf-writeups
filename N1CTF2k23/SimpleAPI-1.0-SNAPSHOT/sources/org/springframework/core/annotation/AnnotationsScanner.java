package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiPredicate;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.lang.Nullable;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/AnnotationsScanner.class */
public abstract class AnnotationsScanner {
    private static final Annotation[] NO_ANNOTATIONS = new Annotation[0];
    private static final Method[] NO_METHODS = new Method[0];
    private static final Map<AnnotatedElement, Annotation[]> declaredAnnotationCache = new ConcurrentReferenceHashMap(256);
    private static final Map<Class<?>, Method[]> baseTypeMethodsCache = new ConcurrentReferenceHashMap(256);

    private AnnotationsScanner() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static <C, R> R scan(C context, AnnotatedElement source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor) {
        return (R) scan(context, source, searchStrategy, processor, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Nullable
    static <C, R> R scan(C context, AnnotatedElement source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter) {
        return (R) processor.finish(process(context, source, searchStrategy, processor, classFilter));
    }

    @Nullable
    private static <C, R> R process(C context, AnnotatedElement source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter) {
        if (source instanceof Class) {
            return (R) processClass(context, (Class) source, searchStrategy, processor, classFilter);
        }
        if (source instanceof Method) {
            return (R) processMethod(context, (Method) source, searchStrategy, processor, classFilter);
        }
        return (R) processElement(context, source, processor, classFilter);
    }

    @Nullable
    private static <C, R> R processClass(C context, Class<?> source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter) {
        switch (searchStrategy) {
            case DIRECT:
                return (R) processElement(context, source, processor, classFilter);
            case INHERITED_ANNOTATIONS:
                return (R) processClassInheritedAnnotations(context, source, searchStrategy, processor, classFilter);
            case SUPERCLASS:
                return (R) processClassHierarchy(context, source, processor, classFilter, false, false);
            case TYPE_HIERARCHY:
                return (R) processClassHierarchy(context, source, processor, classFilter, true, false);
            case TYPE_HIERARCHY_AND_ENCLOSING_CLASSES:
                return (R) processClassHierarchy(context, source, processor, classFilter, true, true);
            default:
                throw new IllegalStateException("Unsupported search strategy " + searchStrategy);
        }
    }

    @Nullable
    private static <C, R> R processClassInheritedAnnotations(C context, Class<?> source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter) {
        try {
            if (isWithoutHierarchy(source, searchStrategy)) {
                return (R) processElement(context, source, processor, classFilter);
            }
            Annotation[] relevant = null;
            int remaining = Integer.MAX_VALUE;
            int aggregateIndex = 0;
            while (source != null && source != Object.class && remaining > 0 && !hasPlainJavaAnnotationsOnly(source)) {
                R result = processor.doWithAggregate(context, aggregateIndex);
                if (result != null) {
                    return result;
                }
                if (!isFiltered(source, context, classFilter)) {
                    Annotation[] declaredAnnotations = getDeclaredAnnotations(context, source, classFilter, true);
                    if (relevant == null && declaredAnnotations.length > 0) {
                        relevant = source.getAnnotations();
                        remaining = relevant.length;
                    }
                    for (int i = 0; i < declaredAnnotations.length; i++) {
                        if (declaredAnnotations[i] != null) {
                            boolean isRelevant = false;
                            int relevantIndex = 0;
                            while (true) {
                                if (relevantIndex >= relevant.length) {
                                    break;
                                } else if (relevant[relevantIndex] == null || declaredAnnotations[i].annotationType() != relevant[relevantIndex].annotationType()) {
                                    relevantIndex++;
                                } else {
                                    isRelevant = true;
                                    relevant[relevantIndex] = null;
                                    remaining--;
                                    break;
                                }
                            }
                            if (!isRelevant) {
                                declaredAnnotations[i] = null;
                            }
                        }
                    }
                    R result2 = processor.doWithAnnotations(context, aggregateIndex, source, declaredAnnotations);
                    if (result2 != null) {
                        return result2;
                    }
                    source = source.getSuperclass();
                    aggregateIndex++;
                }
            }
            return null;
        } catch (Throwable ex) {
            AnnotationUtils.handleIntrospectionFailure(source, ex);
            return null;
        }
    }

    @Nullable
    private static <C, R> R processClassHierarchy(C context, Class<?> source, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter, boolean includeInterfaces, boolean includeEnclosing) {
        return (R) processClassHierarchy(context, new int[]{0}, source, processor, classFilter, includeInterfaces, includeEnclosing);
    }

    @Nullable
    private static <C, R> R processClassHierarchy(C context, int[] aggregateIndex, Class<?> source, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter, boolean includeInterfaces, boolean includeEnclosing) {
        R superclassResult;
        Class<?>[] interfaces;
        try {
            R result = processor.doWithAggregate(context, aggregateIndex[0]);
            if (result != null) {
                return result;
            }
            if (hasPlainJavaAnnotationsOnly(source)) {
                return null;
            }
            Annotation[] annotations = getDeclaredAnnotations(context, source, classFilter, false);
            R result2 = processor.doWithAnnotations(context, aggregateIndex[0], source, annotations);
            if (result2 != null) {
                return result2;
            }
            aggregateIndex[0] = aggregateIndex[0] + 1;
            if (includeInterfaces) {
                for (Class<?> interfaceType : source.getInterfaces()) {
                    R interfacesResult = (R) processClassHierarchy(context, aggregateIndex, interfaceType, processor, classFilter, true, includeEnclosing);
                    if (interfacesResult != null) {
                        return interfacesResult;
                    }
                }
            }
            Class<?> superclass = source.getSuperclass();
            if (superclass != Object.class && superclass != null && (superclassResult = (R) processClassHierarchy(context, aggregateIndex, superclass, processor, classFilter, includeInterfaces, includeEnclosing)) != null) {
                return superclassResult;
            }
            if (includeEnclosing) {
                Class<?> enclosingClass = source.getEnclosingClass();
                if (enclosingClass != null) {
                    R enclosingResult = (R) processClassHierarchy(context, aggregateIndex, enclosingClass, processor, classFilter, includeInterfaces, true);
                    if (enclosingResult != null) {
                        return enclosingResult;
                    }
                }
            }
            return null;
        } catch (Throwable ex) {
            AnnotationUtils.handleIntrospectionFailure(source, ex);
            return null;
        }
    }

    @Nullable
    private static <C, R> R processMethod(C context, Method source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter) {
        switch (searchStrategy) {
            case DIRECT:
            case INHERITED_ANNOTATIONS:
                return (R) processMethodInheritedAnnotations(context, source, processor, classFilter);
            case SUPERCLASS:
                return (R) processMethodHierarchy(context, new int[]{0}, source.getDeclaringClass(), processor, classFilter, source, false);
            case TYPE_HIERARCHY:
            case TYPE_HIERARCHY_AND_ENCLOSING_CLASSES:
                return (R) processMethodHierarchy(context, new int[]{0}, source.getDeclaringClass(), processor, classFilter, source, true);
            default:
                throw new IllegalStateException("Unsupported search strategy " + searchStrategy);
        }
    }

    @Nullable
    private static <C, R> R processMethodInheritedAnnotations(C context, Method source, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter) {
        try {
            R result = processor.doWithAggregate(context, 0);
            return result != null ? result : (R) processMethodAnnotations(context, 0, source, processor, classFilter);
        } catch (Throwable ex) {
            AnnotationUtils.handleIntrospectionFailure(source, ex);
            return null;
        }
    }

    @Nullable
    private static <C, R> R processMethodHierarchy(C context, int[] aggregateIndex, Class<?> sourceClass, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter, Method rootMethod, boolean includeInterfaces) {
        Method[] baseTypeMethods;
        Class<?>[] interfaces;
        try {
            R result = processor.doWithAggregate(context, aggregateIndex[0]);
            if (result != null) {
                return result;
            }
            if (hasPlainJavaAnnotationsOnly(sourceClass)) {
                return null;
            }
            boolean calledProcessor = false;
            if (sourceClass == rootMethod.getDeclaringClass()) {
                R result2 = (R) processMethodAnnotations(context, aggregateIndex[0], rootMethod, processor, classFilter);
                calledProcessor = true;
                if (result2 != null) {
                    return result2;
                }
            } else {
                for (Method candidateMethod : getBaseTypeMethods(context, sourceClass, classFilter)) {
                    if (candidateMethod != null && isOverride(rootMethod, candidateMethod)) {
                        R result3 = (R) processMethodAnnotations(context, aggregateIndex[0], candidateMethod, processor, classFilter);
                        calledProcessor = true;
                        if (result3 != null) {
                            return result3;
                        }
                    }
                }
            }
            if (Modifier.isPrivate(rootMethod.getModifiers())) {
                return null;
            }
            if (calledProcessor) {
                aggregateIndex[0] = aggregateIndex[0] + 1;
            }
            if (includeInterfaces) {
                for (Class<?> interfaceType : sourceClass.getInterfaces()) {
                    R interfacesResult = (R) processMethodHierarchy(context, aggregateIndex, interfaceType, processor, classFilter, rootMethod, true);
                    if (interfacesResult != null) {
                        return interfacesResult;
                    }
                }
            }
            Class<?> superclass = sourceClass.getSuperclass();
            if (superclass != Object.class && superclass != null) {
                R superclassResult = (R) processMethodHierarchy(context, aggregateIndex, superclass, processor, classFilter, rootMethod, includeInterfaces);
                if (superclassResult != null) {
                    return superclassResult;
                }
                return null;
            }
            return null;
        } catch (Throwable ex) {
            AnnotationUtils.handleIntrospectionFailure(rootMethod, ex);
            return null;
        }
    }

    private static <C> Method[] getBaseTypeMethods(C context, Class<?> baseType, @Nullable BiPredicate<C, Class<?>> classFilter) {
        if (baseType == Object.class || hasPlainJavaAnnotationsOnly(baseType) || isFiltered(baseType, context, classFilter)) {
            return NO_METHODS;
        }
        Method[] methods = baseTypeMethodsCache.get(baseType);
        if (methods == null) {
            boolean isInterface = baseType.isInterface();
            methods = isInterface ? baseType.getMethods() : ReflectionUtils.getDeclaredMethods(baseType);
            int cleared = 0;
            for (int i = 0; i < methods.length; i++) {
                if ((!isInterface && Modifier.isPrivate(methods[i].getModifiers())) || hasPlainJavaAnnotationsOnly(methods[i]) || getDeclaredAnnotations(methods[i], false).length == 0) {
                    methods[i] = null;
                    cleared++;
                }
            }
            if (cleared == methods.length) {
                methods = NO_METHODS;
            }
            baseTypeMethodsCache.put(baseType, methods);
        }
        return methods;
    }

    private static boolean isOverride(Method rootMethod, Method candidateMethod) {
        return !Modifier.isPrivate(candidateMethod.getModifiers()) && candidateMethod.getName().equals(rootMethod.getName()) && hasSameParameterTypes(rootMethod, candidateMethod);
    }

    private static boolean hasSameParameterTypes(Method rootMethod, Method candidateMethod) {
        if (candidateMethod.getParameterCount() != rootMethod.getParameterCount()) {
            return false;
        }
        Class<?>[] rootParameterTypes = rootMethod.getParameterTypes();
        Class<?>[] candidateParameterTypes = candidateMethod.getParameterTypes();
        if (Arrays.equals(candidateParameterTypes, rootParameterTypes)) {
            return true;
        }
        return hasSameGenericTypeParameters(rootMethod, candidateMethod, rootParameterTypes);
    }

    private static boolean hasSameGenericTypeParameters(Method rootMethod, Method candidateMethod, Class<?>[] rootParameterTypes) {
        Class<?> sourceDeclaringClass = rootMethod.getDeclaringClass();
        Class<?> candidateDeclaringClass = candidateMethod.getDeclaringClass();
        if (!candidateDeclaringClass.isAssignableFrom(sourceDeclaringClass)) {
            return false;
        }
        for (int i = 0; i < rootParameterTypes.length; i++) {
            Class<?> resolvedParameterType = ResolvableType.forMethodParameter(candidateMethod, i, sourceDeclaringClass).resolve();
            if (rootParameterTypes[i] != resolvedParameterType) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static <C, R> R processMethodAnnotations(C context, int aggregateIndex, Method source, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter) {
        Annotation[] annotations = getDeclaredAnnotations(context, source, classFilter, false);
        R result = processor.doWithAnnotations(context, aggregateIndex, source, annotations);
        if (result != null) {
            return result;
        }
        Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(source);
        if (bridgedMethod != source) {
            Annotation[] bridgedAnnotations = getDeclaredAnnotations(context, bridgedMethod, classFilter, true);
            for (int i = 0; i < bridgedAnnotations.length; i++) {
                if (ObjectUtils.containsElement(annotations, bridgedAnnotations[i])) {
                    bridgedAnnotations[i] = null;
                }
            }
            return processor.doWithAnnotations(context, aggregateIndex, source, bridgedAnnotations);
        }
        return null;
    }

    @Nullable
    private static <C, R> R processElement(C context, AnnotatedElement source, AnnotationsProcessor<C, R> processor, @Nullable BiPredicate<C, Class<?>> classFilter) {
        try {
            R result = processor.doWithAggregate(context, 0);
            return result != null ? result : processor.doWithAnnotations(context, 0, source, getDeclaredAnnotations(context, source, classFilter, false));
        } catch (Throwable ex) {
            AnnotationUtils.handleIntrospectionFailure(source, ex);
            return null;
        }
    }

    private static <C, R> Annotation[] getDeclaredAnnotations(C context, AnnotatedElement source, @Nullable BiPredicate<C, Class<?>> classFilter, boolean copy) {
        if ((source instanceof Class) && isFiltered((Class) source, context, classFilter)) {
            return NO_ANNOTATIONS;
        }
        if ((source instanceof Method) && isFiltered(((Method) source).getDeclaringClass(), context, classFilter)) {
            return NO_ANNOTATIONS;
        }
        return getDeclaredAnnotations(source, copy);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static <A extends Annotation> A getDeclaredAnnotation(AnnotatedElement source, Class<A> annotationType) {
        Annotation[] annotations = getDeclaredAnnotations(source, false);
        for (Annotation annotation : annotations) {
            A a = (A) annotation;
            if (a != null && annotationType == a.annotationType()) {
                return a;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Annotation[] getDeclaredAnnotations(AnnotatedElement source, boolean defensive) {
        boolean cached = false;
        Annotation[] annotations = declaredAnnotationCache.get(source);
        if (annotations != null) {
            cached = true;
        } else {
            annotations = source.getDeclaredAnnotations();
            if (annotations.length != 0) {
                boolean allIgnored = true;
                for (int i = 0; i < annotations.length; i++) {
                    Annotation annotation = annotations[i];
                    if (isIgnorable(annotation.annotationType()) || !AttributeMethods.forAnnotationType(annotation.annotationType()).isValid(annotation)) {
                        annotations[i] = null;
                    } else {
                        allIgnored = false;
                    }
                }
                annotations = allIgnored ? NO_ANNOTATIONS : annotations;
                if ((source instanceof Class) || (source instanceof Member)) {
                    declaredAnnotationCache.put(source, annotations);
                    cached = true;
                }
            }
        }
        if (!defensive || annotations.length == 0 || !cached) {
            return annotations;
        }
        return (Annotation[]) annotations.clone();
    }

    private static <C> boolean isFiltered(Class<?> sourceClass, C context, @Nullable BiPredicate<C, Class<?>> classFilter) {
        return classFilter != null && classFilter.test(context, sourceClass);
    }

    private static boolean isIgnorable(Class<?> annotationType) {
        return AnnotationFilter.PLAIN.matches(annotationType);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isKnownEmpty(AnnotatedElement source, MergedAnnotations.SearchStrategy searchStrategy) {
        if (hasPlainJavaAnnotationsOnly(source)) {
            return true;
        }
        if (searchStrategy == MergedAnnotations.SearchStrategy.DIRECT || isWithoutHierarchy(source, searchStrategy)) {
            return !((source instanceof Method) && ((Method) source).isBridge()) && getDeclaredAnnotations(source, false).length == 0;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasPlainJavaAnnotationsOnly(@Nullable Object annotatedElement) {
        if (annotatedElement instanceof Class) {
            return hasPlainJavaAnnotationsOnly((Class<?>) annotatedElement);
        }
        if (annotatedElement instanceof Member) {
            return hasPlainJavaAnnotationsOnly(((Member) annotatedElement).getDeclaringClass());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasPlainJavaAnnotationsOnly(Class<?> type) {
        return type.getName().startsWith("java.") || type == Ordered.class;
    }

    private static boolean isWithoutHierarchy(AnnotatedElement source, MergedAnnotations.SearchStrategy searchStrategy) {
        if (source == Object.class) {
            return true;
        }
        if (source instanceof Class) {
            Class<?> sourceClass = (Class) source;
            boolean noSuperTypes = sourceClass.getSuperclass() == Object.class && sourceClass.getInterfaces().length == 0;
            return searchStrategy == MergedAnnotations.SearchStrategy.TYPE_HIERARCHY_AND_ENCLOSING_CLASSES ? noSuperTypes && sourceClass.getEnclosingClass() == null : noSuperTypes;
        } else if (source instanceof Method) {
            Method sourceMethod = (Method) source;
            return Modifier.isPrivate(sourceMethod.getModifiers()) || isWithoutHierarchy(sourceMethod.getDeclaringClass(), searchStrategy);
        } else {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void clearCache() {
        declaredAnnotationCache.clear();
        baseTypeMethodsCache.clear();
    }
}
