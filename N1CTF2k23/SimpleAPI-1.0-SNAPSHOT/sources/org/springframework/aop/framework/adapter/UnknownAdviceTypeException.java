package org.springframework.aop.framework.adapter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/adapter/UnknownAdviceTypeException.class */
public class UnknownAdviceTypeException extends IllegalArgumentException {
    public UnknownAdviceTypeException(Object advice) {
        super("Advice object [" + advice + "] is neither a supported subinterface of [org.aopalliance.aop.Advice] nor an [org.springframework.aop.Advisor]");
    }

    public UnknownAdviceTypeException(String message) {
        super(message);
    }
}
