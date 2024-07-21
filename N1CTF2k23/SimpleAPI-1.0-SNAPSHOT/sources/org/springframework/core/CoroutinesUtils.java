package org.springframework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KFunction;
import kotlin.reflect.jvm.ReflectJvmMapping;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.reactor.MonoKt;
import kotlinx.coroutines.reactor.ReactorFlowKt;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/* compiled from: CoroutinesUtils.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 2, d1 = {"��2\n��\n\u0002\u0018\u0002\n��\n\u0002\u0010��\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a&\u0010��\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b��\u0010\u0002*\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H��\u001a9\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00032\u0016\u0010\u000b\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00030\f\"\u0004\u0018\u00010\u0003H��¢\u0006\u0002\u0010\r\u001a\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\b\u001a\u00020\tH��\u001a(\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0005\"\b\b��\u0010\u0002*\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001H��¨\u0006\u0011"}, d2 = {"deferredToMono", "Lreactor/core/publisher/Mono;", "T", "", "source", "Lkotlinx/coroutines/Deferred;", "invokeSuspendingFunction", "Lorg/reactivestreams/Publisher;", "method", "Ljava/lang/reflect/Method;", "bean", "args", "", "(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)Lorg/reactivestreams/Publisher;", "isSuspendingFunction", "", "monoToDeferred", "kotlin-coroutines"})
@JvmName(name = "CoroutinesUtils")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/CoroutinesUtils.class */
public final class CoroutinesUtils {
    @NotNull
    public static final <T> Mono<T> deferredToMono(@NotNull Deferred<? extends T> deferred) {
        Intrinsics.checkParameterIsNotNull(deferred, "source");
        return MonoKt.mono(Dispatchers.getUnconfined(), new CoroutinesUtils$deferredToMono$1(deferred, null));
    }

    @NotNull
    public static final <T> Deferred<T> monoToDeferred(@NotNull Mono<T> mono) {
        Intrinsics.checkParameterIsNotNull(mono, "source");
        return BuildersKt.async$default(GlobalScope.INSTANCE, Dispatchers.getUnconfined(), (CoroutineStart) null, new CoroutinesUtils$monoToDeferred$1(mono, null), 2, (Object) null);
    }

    public static final boolean isSuspendingFunction(@NotNull Method method) {
        Intrinsics.checkParameterIsNotNull(method, "method");
        KFunction kotlinFunction = ReflectJvmMapping.getKotlinFunction(method);
        if (kotlinFunction == null) {
            Intrinsics.throwNpe();
        }
        return kotlinFunction.isSuspend();
    }

    @NotNull
    public static final Publisher<?> invokeSuspendingFunction(@NotNull Method method, @NotNull Object bean, @NotNull Object... args) {
        Intrinsics.checkParameterIsNotNull(method, "method");
        Intrinsics.checkParameterIsNotNull(bean, "bean");
        Intrinsics.checkParameterIsNotNull(args, "args");
        KFunction function = ReflectJvmMapping.getKotlinFunction(method);
        if (function == null) {
            Intrinsics.throwNpe();
        }
        Publisher<?> onErrorMap = MonoKt.mono(Dispatchers.getUnconfined(), new CoroutinesUtils$invokeSuspendingFunction$mono$1(function, bean, args, null)).onErrorMap(InvocationTargetException.class, new Function<E, Throwable>() { // from class: org.springframework.core.CoroutinesUtils$invokeSuspendingFunction$mono$2
            @Override // java.util.function.Function
            public final Throwable apply(InvocationTargetException it) {
                Intrinsics.checkExpressionValueIsNotNull(it, "it");
                return it.getTargetException();
            }
        });
        Intrinsics.checkExpressionValueIsNotNull(onErrorMap, "mono(Dispatchers.Unconfi…a) { it.targetException }");
        if (Intrinsics.areEqual(function.getReturnType().getClassifier(), Reflection.getOrCreateKotlinClass(Flow.class))) {
            Publisher<?> flatMapMany = onErrorMap.flatMapMany(new Function<T, Publisher<? extends R>>() { // from class: org.springframework.core.CoroutinesUtils$invokeSuspendingFunction$1
                @Override // java.util.function.Function
                @NotNull
                public final Flux<Object> apply(Object it) {
                    if (it == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.flow.Flow<kotlin.Any>");
                    }
                    return ReactorFlowKt.asFlux((Flow) it);
                }
            });
            Intrinsics.checkExpressionValueIsNotNull(flatMapMany, "mono.flatMapMany { (it as Flow<Any>).asFlux() }");
            return flatMapMany;
        }
        return onErrorMap;
    }
}
