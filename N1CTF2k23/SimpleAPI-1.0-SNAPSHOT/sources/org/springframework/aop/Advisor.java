package org.springframework.aop;

import org.aopalliance.aop.Advice;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/Advisor.class */
public interface Advisor {
    public static final Advice EMPTY_ADVICE = new Advice() { // from class: org.springframework.aop.Advisor.1
    };

    Advice getAdvice();

    boolean isPerInstance();
}
