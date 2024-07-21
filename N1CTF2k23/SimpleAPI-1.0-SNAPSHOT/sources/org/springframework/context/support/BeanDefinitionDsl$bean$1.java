package org.springframework.context.support;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.util.function.Supplier;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.BeanDefinitionDsl;
/* compiled from: BeanDefinitionDsl.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3, d1 = {"��\f\n\u0002\b\u0002\n\u0002\u0010��\n\u0002\b\u0002\u0010��\u001a\u0002H\u0001\"\n\b��\u0010\u0001\u0018\u0001*\u00020\u0002H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"<anonymous>", "T", "", BeanUtil.PREFIX_GETTER_GET, "()Ljava/lang/Object;"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/support/BeanDefinitionDsl$bean$1.class */
public final class BeanDefinitionDsl$bean$1<T> implements Supplier<T> {
    final /* synthetic */ BeanDefinitionDsl this$0;
    final /* synthetic */ Function1 $function;

    public BeanDefinitionDsl$bean$1(BeanDefinitionDsl beanDefinitionDsl, Function1 function1) {
        this.this$0 = beanDefinitionDsl;
        this.$function = function1;
    }

    @Override // java.util.function.Supplier
    @NotNull
    public final T get() {
        return (T) this.$function.invoke(new BeanDefinitionDsl.BeanSupplierContext(this.this$0.getContext()));
    }
}
