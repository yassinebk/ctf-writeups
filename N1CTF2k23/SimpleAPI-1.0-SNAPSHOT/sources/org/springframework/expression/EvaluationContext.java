package org.springframework.expression;

import java.util.List;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/EvaluationContext.class */
public interface EvaluationContext {
    TypedValue getRootObject();

    List<PropertyAccessor> getPropertyAccessors();

    List<ConstructorResolver> getConstructorResolvers();

    List<MethodResolver> getMethodResolvers();

    @Nullable
    BeanResolver getBeanResolver();

    TypeLocator getTypeLocator();

    TypeConverter getTypeConverter();

    TypeComparator getTypeComparator();

    OperatorOverloader getOperatorOverloader();

    void setVariable(String str, @Nullable Object obj);

    @Nullable
    Object lookupVariable(String str);
}
