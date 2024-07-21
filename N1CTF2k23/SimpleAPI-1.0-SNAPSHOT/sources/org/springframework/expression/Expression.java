package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/Expression.class */
public interface Expression {
    String getExpressionString();

    @Nullable
    Object getValue() throws EvaluationException;

    @Nullable
    <T> T getValue(@Nullable Class<T> cls) throws EvaluationException;

    @Nullable
    Object getValue(@Nullable Object obj) throws EvaluationException;

    @Nullable
    <T> T getValue(@Nullable Object obj, @Nullable Class<T> cls) throws EvaluationException;

    @Nullable
    Object getValue(EvaluationContext evaluationContext) throws EvaluationException;

    @Nullable
    Object getValue(EvaluationContext evaluationContext, @Nullable Object obj) throws EvaluationException;

    @Nullable
    <T> T getValue(EvaluationContext evaluationContext, @Nullable Class<T> cls) throws EvaluationException;

    @Nullable
    <T> T getValue(EvaluationContext evaluationContext, @Nullable Object obj, @Nullable Class<T> cls) throws EvaluationException;

    @Nullable
    Class<?> getValueType() throws EvaluationException;

    @Nullable
    Class<?> getValueType(@Nullable Object obj) throws EvaluationException;

    @Nullable
    Class<?> getValueType(EvaluationContext evaluationContext) throws EvaluationException;

    @Nullable
    Class<?> getValueType(EvaluationContext evaluationContext, @Nullable Object obj) throws EvaluationException;

    @Nullable
    TypeDescriptor getValueTypeDescriptor() throws EvaluationException;

    @Nullable
    TypeDescriptor getValueTypeDescriptor(@Nullable Object obj) throws EvaluationException;

    @Nullable
    TypeDescriptor getValueTypeDescriptor(EvaluationContext evaluationContext) throws EvaluationException;

    @Nullable
    TypeDescriptor getValueTypeDescriptor(EvaluationContext evaluationContext, @Nullable Object obj) throws EvaluationException;

    boolean isWritable(@Nullable Object obj) throws EvaluationException;

    boolean isWritable(EvaluationContext evaluationContext) throws EvaluationException;

    boolean isWritable(EvaluationContext evaluationContext, @Nullable Object obj) throws EvaluationException;

    void setValue(@Nullable Object obj, @Nullable Object obj2) throws EvaluationException;

    void setValue(EvaluationContext evaluationContext, @Nullable Object obj) throws EvaluationException;

    void setValue(EvaluationContext evaluationContext, @Nullable Object obj, @Nullable Object obj2) throws EvaluationException;
}
