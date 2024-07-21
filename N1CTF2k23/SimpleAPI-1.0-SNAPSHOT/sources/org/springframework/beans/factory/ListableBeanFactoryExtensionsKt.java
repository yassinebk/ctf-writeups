package org.springframework.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ListableBeanFactoryExtensions.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 2, d1 = {"��2\n��\n\u0002\u0010\u001b\n��\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n��\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010��\n��\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0002\u001a#\u0010��\u001a\u0004\u0018\u00010\u0001\"\n\b��\u0010\u0002\u0018\u0001*\u00020\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0086\b\u001a&\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u0007\"\n\b��\u0010\u0002\u0018\u0001*\u00020\u0001*\u00020\u0003H\u0086\b¢\u0006\u0002\u0010\b\u001a:\u0010\t\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u0007\"\n\b��\u0010\u0002\u0018\u0001*\u00020\n*\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\fH\u0086\b¢\u0006\u0002\u0010\u000e\u001a9\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u00020\u0010\"\n\b��\u0010\u0002\u0018\u0001*\u00020\n*\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\fH\u0086\b\u001a%\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\u0010\"\n\b��\u0010\u0002\u0018\u0001*\u00020\u0001*\u00020\u0003H\u0086\b¨\u0006\u0012"}, d2 = {"findAnnotationOnBean", "", "T", "Lorg/springframework/beans/factory/ListableBeanFactory;", "beanName", "", "getBeanNamesForAnnotation", "", "(Lorg/springframework/beans/factory/ListableBeanFactory;)[Ljava/lang/String;", "getBeanNamesForType", "", "includeNonSingletons", "", "allowEagerInit", "(Lorg/springframework/beans/factory/ListableBeanFactory;ZZ)[Ljava/lang/String;", "getBeansOfType", "", "getBeansWithAnnotation", "spring-beans"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/ListableBeanFactoryExtensionsKt.class */
public final class ListableBeanFactoryExtensionsKt {
    public static /* synthetic */ String[] getBeanNamesForType$default(ListableBeanFactory $this$getBeanNamesForType, boolean includeNonSingletons, boolean allowEagerInit, int i, Object obj) {
        if ((i & 1) != 0) {
            includeNonSingletons = true;
        }
        if ((i & 2) != 0) {
            allowEagerInit = true;
        }
        Intrinsics.checkParameterIsNotNull($this$getBeanNamesForType, "$this$getBeanNamesForType");
        Intrinsics.reifiedOperationMarker(4, "T");
        String[] beanNamesForType = $this$getBeanNamesForType.getBeanNamesForType(Object.class, includeNonSingletons, allowEagerInit);
        Intrinsics.checkExpressionValueIsNotNull(beanNamesForType, "getBeanNamesForType(T::c…ngletons, allowEagerInit)");
        return beanNamesForType;
    }

    @NotNull
    public static final /* synthetic */ <T> String[] getBeanNamesForType(@NotNull ListableBeanFactory $this$getBeanNamesForType, boolean includeNonSingletons, boolean allowEagerInit) {
        Intrinsics.checkParameterIsNotNull($this$getBeanNamesForType, "$this$getBeanNamesForType");
        Intrinsics.reifiedOperationMarker(4, "T");
        String[] beanNamesForType = $this$getBeanNamesForType.getBeanNamesForType(Object.class, includeNonSingletons, allowEagerInit);
        Intrinsics.checkExpressionValueIsNotNull(beanNamesForType, "getBeanNamesForType(T::c…ngletons, allowEagerInit)");
        return beanNamesForType;
    }

    public static /* synthetic */ Map getBeansOfType$default(ListableBeanFactory $this$getBeansOfType, boolean includeNonSingletons, boolean allowEagerInit, int i, Object obj) {
        if ((i & 1) != 0) {
            includeNonSingletons = true;
        }
        if ((i & 2) != 0) {
            allowEagerInit = true;
        }
        Intrinsics.checkParameterIsNotNull($this$getBeansOfType, "$this$getBeansOfType");
        Intrinsics.reifiedOperationMarker(4, "T");
        Map beansOfType = $this$getBeansOfType.getBeansOfType(Object.class, includeNonSingletons, allowEagerInit);
        Intrinsics.checkExpressionValueIsNotNull(beansOfType, "getBeansOfType(T::class.…ngletons, allowEagerInit)");
        return beansOfType;
    }

    @NotNull
    public static final /* synthetic */ <T> Map<String, T> getBeansOfType(@NotNull ListableBeanFactory $this$getBeansOfType, boolean includeNonSingletons, boolean allowEagerInit) {
        Intrinsics.checkParameterIsNotNull($this$getBeansOfType, "$this$getBeansOfType");
        Intrinsics.reifiedOperationMarker(4, "T");
        Map<String, T> beansOfType = $this$getBeansOfType.getBeansOfType(Object.class, includeNonSingletons, allowEagerInit);
        Intrinsics.checkExpressionValueIsNotNull(beansOfType, "getBeansOfType(T::class.…ngletons, allowEagerInit)");
        return beansOfType;
    }

    @NotNull
    public static final /* synthetic */ <T extends Annotation> String[] getBeanNamesForAnnotation(@NotNull ListableBeanFactory $this$getBeanNamesForAnnotation) {
        Intrinsics.checkParameterIsNotNull($this$getBeanNamesForAnnotation, "$this$getBeanNamesForAnnotation");
        Intrinsics.reifiedOperationMarker(4, "T");
        String[] beanNamesForAnnotation = $this$getBeanNamesForAnnotation.getBeanNamesForAnnotation(Annotation.class);
        Intrinsics.checkExpressionValueIsNotNull(beanNamesForAnnotation, "getBeanNamesForAnnotation(T::class.java)");
        return beanNamesForAnnotation;
    }

    @NotNull
    public static final /* synthetic */ <T extends Annotation> Map<String, Object> getBeansWithAnnotation(@NotNull ListableBeanFactory $this$getBeansWithAnnotation) {
        Intrinsics.checkParameterIsNotNull($this$getBeansWithAnnotation, "$this$getBeansWithAnnotation");
        Intrinsics.reifiedOperationMarker(4, "T");
        Map<String, Object> beansWithAnnotation = $this$getBeansWithAnnotation.getBeansWithAnnotation(Annotation.class);
        Intrinsics.checkExpressionValueIsNotNull(beansWithAnnotation, "getBeansWithAnnotation(T::class.java)");
        return beansWithAnnotation;
    }

    @Nullable
    public static final /* synthetic */ <T extends Annotation> Annotation findAnnotationOnBean(@NotNull ListableBeanFactory $this$findAnnotationOnBean, @NotNull String beanName) {
        Intrinsics.checkParameterIsNotNull($this$findAnnotationOnBean, "$this$findAnnotationOnBean");
        Intrinsics.checkParameterIsNotNull(beanName, "beanName");
        Intrinsics.reifiedOperationMarker(4, "T");
        return $this$findAnnotationOnBean.findAnnotationOnBean(beanName, Annotation.class);
    }
}
