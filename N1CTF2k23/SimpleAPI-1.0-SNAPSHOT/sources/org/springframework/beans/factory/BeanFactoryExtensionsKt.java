package org.springframework.beans.factory;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
/* compiled from: BeanFactoryExtensions.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 2, d1 = {"��&\n\u0002\b\u0002\n\u0002\u0010��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\u001a\u001e\u0010��\u001a\u0002H\u0001\"\n\b��\u0010\u0001\u0018\u0001*\u00020\u0002*\u00020\u0003H\u0086\b¢\u0006\u0002\u0010\u0004\u001a2\u0010��\u001a\u0002H\u0001\"\n\b��\u0010\u0001\u0018\u0001*\u00020\u0002*\u00020\u00032\u0012\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006\"\u00020\u0002H\u0086\b¢\u0006\u0002\u0010\u0007\u001a&\u0010��\u001a\u0002H\u0001\"\n\b��\u0010\u0001\u0018\u0001*\u00020\u0002*\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0086\b¢\u0006\u0002\u0010\n\u001a\u001f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00010\f\"\n\b��\u0010\u0001\u0018\u0001*\u00020\u0002*\u00020\u0003H\u0086\b¨\u0006\r"}, d2 = {"getBean", "T", "", "Lorg/springframework/beans/factory/BeanFactory;", "(Lorg/springframework/beans/factory/BeanFactory;)Ljava/lang/Object;", "args", "", "(Lorg/springframework/beans/factory/BeanFactory;[Ljava/lang/Object;)Ljava/lang/Object;", "name", "", "(Lorg/springframework/beans/factory/BeanFactory;Ljava/lang/String;)Ljava/lang/Object;", "getBeanProvider", "Lorg/springframework/beans/factory/ObjectProvider;", "spring-beans"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/BeanFactoryExtensionsKt.class */
public final class BeanFactoryExtensionsKt {
    @NotNull
    public static final /* synthetic */ <T> T getBean(@NotNull BeanFactory $this$getBean) {
        Intrinsics.checkParameterIsNotNull($this$getBean, "$this$getBean");
        Intrinsics.reifiedOperationMarker(4, "T");
        T t = (T) $this$getBean.getBean(Object.class);
        Intrinsics.checkExpressionValueIsNotNull(t, "getBean(T::class.java)");
        return t;
    }

    @NotNull
    public static final /* synthetic */ <T> T getBean(@NotNull BeanFactory $this$getBean, @NotNull String name) {
        Intrinsics.checkParameterIsNotNull($this$getBean, "$this$getBean");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.reifiedOperationMarker(4, "T");
        T t = (T) $this$getBean.getBean(name, Object.class);
        Intrinsics.checkExpressionValueIsNotNull(t, "getBean(name, T::class.java)");
        return t;
    }

    @NotNull
    public static final /* synthetic */ <T> T getBean(@NotNull BeanFactory $this$getBean, @NotNull Object... args) {
        Intrinsics.checkParameterIsNotNull($this$getBean, "$this$getBean");
        Intrinsics.checkParameterIsNotNull(args, "args");
        Intrinsics.reifiedOperationMarker(4, "T");
        T t = (T) $this$getBean.getBean(Object.class, Arrays.copyOf(args, args.length));
        Intrinsics.checkExpressionValueIsNotNull(t, "getBean(T::class.java, *args)");
        return t;
    }

    @NotNull
    public static final /* synthetic */ <T> ObjectProvider<T> getBeanProvider(@NotNull BeanFactory $this$getBeanProvider) {
        Intrinsics.checkParameterIsNotNull($this$getBeanProvider, "$this$getBeanProvider");
        Intrinsics.needClassReification();
        ObjectProvider<T> beanProvider = $this$getBeanProvider.getBeanProvider(ResolvableType.forType(new ParameterizedTypeReference<T>() { // from class: org.springframework.beans.factory.BeanFactoryExtensionsKt$getBeanProvider$1
        }.getType()));
        Intrinsics.checkExpressionValueIsNotNull(beanProvider, "getBeanProvider(Resolvab…Reference<T>() {}).type))");
        return beanProvider;
    }
}
