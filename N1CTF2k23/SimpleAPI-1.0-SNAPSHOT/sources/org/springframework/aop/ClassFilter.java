package org.springframework.aop;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/ClassFilter.class */
public interface ClassFilter {
    public static final ClassFilter TRUE = TrueClassFilter.INSTANCE;

    boolean matches(Class<?> cls);
}
