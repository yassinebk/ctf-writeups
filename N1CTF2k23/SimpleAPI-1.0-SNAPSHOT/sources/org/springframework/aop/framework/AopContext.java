package org.springframework.aop.framework;

import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/AopContext.class */
public final class AopContext {
    private static final ThreadLocal<Object> currentProxy = new NamedThreadLocal("Current AOP proxy");

    private AopContext() {
    }

    public static Object currentProxy() throws IllegalStateException {
        Object proxy = currentProxy.get();
        if (proxy == null) {
            throw new IllegalStateException("Cannot find current proxy: Set 'exposeProxy' property on Advised to 'true' to make it available, and ensure that AopContext.currentProxy() is invoked in the same thread as the AOP invocation context.");
        }
        return proxy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static Object setCurrentProxy(@Nullable Object proxy) {
        Object old = currentProxy.get();
        if (proxy != null) {
            currentProxy.set(proxy);
        } else {
            currentProxy.remove();
        }
        return old;
    }
}
