package org.springframework.aop.framework.autoproxy;

import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/autoproxy/ProxyCreationContext.class */
public final class ProxyCreationContext {
    private static final ThreadLocal<String> currentProxiedBeanName = new NamedThreadLocal("Name of currently proxied bean");

    private ProxyCreationContext() {
    }

    @Nullable
    public static String getCurrentProxiedBeanName() {
        return currentProxiedBeanName.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setCurrentProxiedBeanName(@Nullable String beanName) {
        if (beanName != null) {
            currentProxiedBeanName.set(beanName);
        } else {
            currentProxiedBeanName.remove();
        }
    }
}
