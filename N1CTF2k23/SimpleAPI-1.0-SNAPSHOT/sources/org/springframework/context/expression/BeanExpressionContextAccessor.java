package org.springframework.context.expression;

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/expression/BeanExpressionContextAccessor.class */
public class BeanExpressionContextAccessor implements PropertyAccessor {
    @Override // org.springframework.expression.PropertyAccessor
    public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
        return (target instanceof BeanExpressionContext) && ((BeanExpressionContext) target).containsObject(name);
    }

    @Override // org.springframework.expression.PropertyAccessor
    public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
        Assert.state(target instanceof BeanExpressionContext, "Target must be of type BeanExpressionContext");
        return new TypedValue(((BeanExpressionContext) target).getObject(name));
    }

    @Override // org.springframework.expression.PropertyAccessor
    public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
        return false;
    }

    @Override // org.springframework.expression.PropertyAccessor
    public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) throws AccessException {
        throw new AccessException("Beans in a BeanFactory are read-only");
    }

    @Override // org.springframework.expression.PropertyAccessor
    public Class<?>[] getSpecificTargetClasses() {
        return new Class[]{BeanExpressionContext.class};
    }
}
