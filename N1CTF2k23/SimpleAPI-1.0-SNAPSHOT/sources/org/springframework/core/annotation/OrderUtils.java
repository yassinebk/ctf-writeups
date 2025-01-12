package org.springframework.core.annotation;

import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.lang.Nullable;
import org.springframework.util.ConcurrentReferenceHashMap;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/OrderUtils.class */
public abstract class OrderUtils {
    private static final String JAVAX_PRIORITY_ANNOTATION = "javax.annotation.Priority";
    private static final Object NOT_ANNOTATED = new Object();
    private static final Map<AnnotatedElement, Object> orderCache = new ConcurrentReferenceHashMap(64);

    public static int getOrder(Class<?> type, int defaultOrder) {
        Integer order = getOrder(type);
        return order != null ? order.intValue() : defaultOrder;
    }

    @Nullable
    public static Integer getOrder(Class<?> type, @Nullable Integer defaultOrder) {
        Integer order = getOrder(type);
        return order != null ? order : defaultOrder;
    }

    @Nullable
    public static Integer getOrder(Class<?> type) {
        return getOrderFromAnnotations(type, MergedAnnotations.from(type, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static Integer getOrderFromAnnotations(AnnotatedElement element, MergedAnnotations annotations) {
        if (!(element instanceof Class)) {
            return findOrder(annotations);
        }
        Object cached = orderCache.get(element);
        if (cached != null) {
            if (cached instanceof Integer) {
                return (Integer) cached;
            }
            return null;
        }
        Integer result = findOrder(annotations);
        orderCache.put(element, result != null ? result : NOT_ANNOTATED);
        return result;
    }

    @Nullable
    private static Integer findOrder(MergedAnnotations annotations) {
        MergedAnnotation<Order> orderAnnotation = annotations.get(Order.class);
        if (orderAnnotation.isPresent()) {
            return Integer.valueOf(orderAnnotation.getInt("value"));
        }
        MergedAnnotation<?> priorityAnnotation = annotations.get(JAVAX_PRIORITY_ANNOTATION);
        if (priorityAnnotation.isPresent()) {
            return Integer.valueOf(priorityAnnotation.getInt("value"));
        }
        return null;
    }

    @Nullable
    public static Integer getPriority(Class<?> type) {
        return (Integer) MergedAnnotations.from(type, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(JAVAX_PRIORITY_ANNOTATION).getValue("value", Integer.class).orElse(null);
    }
}
