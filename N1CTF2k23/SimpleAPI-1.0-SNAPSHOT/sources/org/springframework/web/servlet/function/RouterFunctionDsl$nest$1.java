package org.springframework.web.servlet.function;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReference;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import org.jetbrains.annotations.NotNull;
/* compiled from: RouterFunctionDsl.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3, d1 = {"��\f\n��\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\u0010��\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "Lorg/springframework/web/servlet/function/RouterFunction;", "Lorg/springframework/web/servlet/function/ServerResponse;", "invoke"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctionDsl$nest$1.class */
final /* synthetic */ class RouterFunctionDsl$nest$1 extends FunctionReference implements Function0<RouterFunction<ServerResponse>> {
    public final KDeclarationContainer getOwner() {
        return Reflection.getOrCreateKotlinClass(RouterFunctionDsl.class);
    }

    public final String getName() {
        return JsonPOJOBuilder.DEFAULT_BUILD_METHOD;
    }

    public final String getSignature() {
        return "build$spring_webmvc()Lorg/springframework/web/servlet/function/RouterFunction;";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RouterFunctionDsl$nest$1(RouterFunctionDsl routerFunctionDsl) {
        super(0, routerFunctionDsl);
    }

    @NotNull
    public final RouterFunction<ServerResponse> invoke() {
        return ((RouterFunctionDsl) this.receiver).build$spring_webmvc();
    }
}
