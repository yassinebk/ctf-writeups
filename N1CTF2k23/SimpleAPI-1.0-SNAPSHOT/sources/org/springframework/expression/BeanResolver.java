package org.springframework.expression;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/BeanResolver.class */
public interface BeanResolver {
    Object resolve(EvaluationContext evaluationContext, String str) throws AccessException;
}
