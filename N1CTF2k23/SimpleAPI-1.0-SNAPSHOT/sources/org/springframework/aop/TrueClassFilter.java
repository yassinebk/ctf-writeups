package org.springframework.aop;

import java.io.Serializable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/TrueClassFilter.class */
final class TrueClassFilter implements ClassFilter, Serializable {
    public static final TrueClassFilter INSTANCE = new TrueClassFilter();

    private TrueClassFilter() {
    }

    @Override // org.springframework.aop.ClassFilter
    public boolean matches(Class<?> clazz) {
        return true;
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public String toString() {
        return "ClassFilter.TRUE";
    }
}
