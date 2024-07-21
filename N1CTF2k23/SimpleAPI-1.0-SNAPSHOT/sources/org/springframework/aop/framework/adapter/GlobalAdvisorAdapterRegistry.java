package org.springframework.aop.framework.adapter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/adapter/GlobalAdvisorAdapterRegistry.class */
public final class GlobalAdvisorAdapterRegistry {
    private static AdvisorAdapterRegistry instance = new DefaultAdvisorAdapterRegistry();

    private GlobalAdvisorAdapterRegistry() {
    }

    public static AdvisorAdapterRegistry getInstance() {
        return instance;
    }

    static void reset() {
        instance = new DefaultAdvisorAdapterRegistry();
    }
}
