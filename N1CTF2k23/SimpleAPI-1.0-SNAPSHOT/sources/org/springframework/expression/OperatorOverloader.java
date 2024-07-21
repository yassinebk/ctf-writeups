package org.springframework.expression;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/OperatorOverloader.class */
public interface OperatorOverloader {
    boolean overridesOperation(Operation operation, @Nullable Object obj, @Nullable Object obj2) throws EvaluationException;

    Object operate(Operation operation, @Nullable Object obj, @Nullable Object obj2) throws EvaluationException;
}
