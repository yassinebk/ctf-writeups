package org.springframework.web.servlet.function;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: RouterFunctionDsl.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctionDslKt$sam$org_springframework_web_servlet_function_HandlerFunction$0.class */
final class RouterFunctionDslKt$sam$org_springframework_web_servlet_function_HandlerFunction$0 implements HandlerFunction {
    private final /* synthetic */ Function1 function;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RouterFunctionDslKt$sam$org_springframework_web_servlet_function_HandlerFunction$0(Function1 function1) {
        this.function = function1;
    }

    @Override // org.springframework.web.servlet.function.HandlerFunction
    public final /* synthetic */ ServerResponse handle(@NotNull ServerRequest request) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        return (ServerResponse) this.function.invoke(request);
    }
}
