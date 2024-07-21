package org.springframework.core;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.reactive.AwaitKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;
/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: CoroutinesUtils.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3, d1 = {"��\u0010\n\u0002\b\u0002\n\u0002\u0010��\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010��\u001a\u0004\u0018\u0001H\u0001\"\b\b��\u0010\u0001*\u00020\u0002*\u00020\u0003H\u008a@¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "T", "", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"})
@DebugMetadata(f = "CoroutinesUtils.kt", l = {52}, i = {0}, s = {"L$0"}, n = {"$this$async"}, m = "invokeSuspend", c = "org.springframework.core.CoroutinesUtils$monoToDeferred$1")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/CoroutinesUtils$monoToDeferred$1.class */
final class CoroutinesUtils$monoToDeferred$1<T> extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super T>, Object> {
    private CoroutineScope p$;
    Object L$0;
    int label;
    final /* synthetic */ Mono $source;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CoroutinesUtils$monoToDeferred$1(Mono mono, Continuation continuation) {
        super(2, continuation);
        this.$source = mono;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        CoroutinesUtils$monoToDeferred$1 coroutinesUtils$monoToDeferred$1 = new CoroutinesUtils$monoToDeferred$1(this.$source, continuation);
        CoroutineScope coroutineScope = (CoroutineScope) value;
        coroutinesUtils$monoToDeferred$1.p$ = (CoroutineScope) value;
        return coroutinesUtils$monoToDeferred$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return create(obj, (Continuation) obj2).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object $result) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                CoroutineScope $this$async = this.p$;
                this.L$0 = $this$async;
                this.label = 1;
                Object awaitFirstOrNull = AwaitKt.awaitFirstOrNull(this.$source, this);
                return awaitFirstOrNull == coroutine_suspended ? coroutine_suspended : awaitFirstOrNull;
            case 1:
                CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
                ResultKt.throwOnFailure($result);
                return $result;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
