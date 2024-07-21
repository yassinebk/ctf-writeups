package org.springframework.core;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SpreadBuilder;
import kotlin.ranges.IntRange;
import kotlin.reflect.KFunction;
import kotlin.reflect.full.KCallables;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: CoroutinesUtils.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3, d1 = {"��\u000e\n��\n\u0002\u0010��\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010��\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u008a@¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"})
@DebugMetadata(f = "CoroutinesUtils.kt", l = {72}, i = {0}, s = {"L$0"}, n = {"$this$mono"}, m = "invokeSuspend", c = "org.springframework.core.CoroutinesUtils$invokeSuspendingFunction$mono$1")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/CoroutinesUtils$invokeSuspendingFunction$mono$1.class */
final class CoroutinesUtils$invokeSuspendingFunction$mono$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Object>, Object> {
    private CoroutineScope p$;
    Object L$0;
    int label;
    final /* synthetic */ KFunction $function;
    final /* synthetic */ Object $bean;
    final /* synthetic */ Object[] $args;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CoroutinesUtils$invokeSuspendingFunction$mono$1(KFunction kFunction, Object obj, Object[] objArr, Continuation continuation) {
        super(2, continuation);
        this.$function = kFunction;
        this.$bean = obj;
        this.$args = objArr;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        CoroutinesUtils$invokeSuspendingFunction$mono$1 coroutinesUtils$invokeSuspendingFunction$mono$1 = new CoroutinesUtils$invokeSuspendingFunction$mono$1(this.$function, this.$bean, this.$args, continuation);
        CoroutineScope coroutineScope = (CoroutineScope) value;
        coroutinesUtils$invokeSuspendingFunction$mono$1.p$ = (CoroutineScope) value;
        return coroutinesUtils$invokeSuspendingFunction$mono$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return create(obj, (Continuation) obj2).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object $result) {
        Object obj;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                CoroutineScope $this$mono = this.p$;
                SpreadBuilder spreadBuilder = new SpreadBuilder(2);
                spreadBuilder.add(this.$bean);
                spreadBuilder.addSpread(ArraysKt.sliceArray(this.$args, new IntRange(0, this.$args.length - 2)));
                Object[] array = spreadBuilder.toArray(new Object[spreadBuilder.size()]);
                this.L$0 = $this$mono;
                this.label = 1;
                obj = KCallables.callSuspend(this.$function, array, this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
                ResultKt.throwOnFailure($result);
                obj = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Object it = obj;
        if (Intrinsics.areEqual(it, Unit.INSTANCE)) {
            return null;
        }
        return it;
    }
}
