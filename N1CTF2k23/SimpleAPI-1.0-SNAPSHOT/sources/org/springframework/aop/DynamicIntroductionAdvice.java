package org.springframework.aop;

import org.aopalliance.aop.Advice;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/DynamicIntroductionAdvice.class */
public interface DynamicIntroductionAdvice extends Advice {
    boolean implementsInterface(Class<?> cls);
}