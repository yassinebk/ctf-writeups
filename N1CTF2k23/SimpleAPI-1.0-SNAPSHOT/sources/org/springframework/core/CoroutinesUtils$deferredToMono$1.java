package org.springframework.core;

import com.sun.el.parser.ELParserConstants;
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
import kotlinx.coroutines.Deferred;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: CoroutinesUtils.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3, d1 = {"��\u0010\n\u0002\b\u0002\n\u0002\u0010��\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010��\u001a\u0002H\u0001\"\b\b��\u0010\u0001*\u00020\u0002*\u00020\u0003H\u008a@¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "T", "", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"})
@DebugMetadata(f = "CoroutinesUtils.kt", l = {ELParserConstants.OR0}, i = {0}, s = {"L$0"}, n = {"$this$mono"}, m = "invokeSuspend", c = "org.springframework.core.CoroutinesUtils$deferredToMono$1")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/CoroutinesUtils$deferredToMono$1.class */
final class CoroutinesUtils$deferredToMono$1<T> extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super T>, Object> {
    private CoroutineScope p$;
    Object L$0;
    int label;
    final /* synthetic */ Deferred $source;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CoroutinesUtils$deferredToMono$1(Deferred deferred, Continuation continuation) {
        super(2, continuation);
        this.$source = deferred;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        CoroutinesUtils$deferredToMono$1 coroutinesUtils$deferredToMono$1 = new CoroutinesUtils$deferredToMono$1(this.$source, continuation);
        CoroutineScope coroutineScope = (CoroutineScope) value;
        coroutinesUtils$deferredToMono$1.p$ = (CoroutineScope) value;
        return coroutinesUtils$deferredToMono$1;
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
                CoroutineScope $this$mono = this.p$;
                Deferred deferred = this.$source;
                this.L$0 = $this$mono;
                this.label = 1;
                Object await = deferred.await(this);
                return await == coroutine_suspended ? coroutine_suspended : await;
            case 1:
                CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
                ResultKt.throwOnFailure($result);
                return $result;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
