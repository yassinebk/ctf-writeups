package org.springframework.expression.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/CompiledExpression.class */
public abstract class CompiledExpression {
    public abstract Object getValue(@Nullable Object obj, @Nullable EvaluationContext evaluationContext) throws EvaluationException;
}
