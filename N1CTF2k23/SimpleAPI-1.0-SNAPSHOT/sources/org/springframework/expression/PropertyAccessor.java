package org.springframework.expression;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/PropertyAccessor.class */
public interface PropertyAccessor {
    @Nullable
    Class<?>[] getSpecificTargetClasses();

    boolean canRead(EvaluationContext evaluationContext, @Nullable Object obj, String str) throws AccessException;

    TypedValue read(EvaluationContext evaluationContext, @Nullable Object obj, String str) throws AccessException;

    boolean canWrite(EvaluationContext evaluationContext, @Nullable Object obj, String str) throws AccessException;

    void write(EvaluationContext evaluationContext, @Nullable Object obj, String str, @Nullable Object obj2) throws AccessException;
}
