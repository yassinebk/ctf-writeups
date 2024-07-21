package org.springframework.expression;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/ConstructorExecutor.class */
public interface ConstructorExecutor {
    TypedValue execute(EvaluationContext evaluationContext, Object... objArr) throws AccessException;
}
