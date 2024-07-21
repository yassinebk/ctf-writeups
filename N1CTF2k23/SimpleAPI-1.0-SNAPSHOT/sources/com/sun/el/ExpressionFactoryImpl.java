package com.sun.el;

import com.sun.el.lang.ELSupport;
import com.sun.el.lang.ExpressionBuilder;
import com.sun.el.stream.StreamELResolver;
import com.sun.el.util.MessageFactory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/ExpressionFactoryImpl.class */
public class ExpressionFactoryImpl extends ExpressionFactory {
    private Properties properties;
    private boolean isBackwardCompatible22;

    public ExpressionFactoryImpl() {
    }

    public ExpressionFactoryImpl(Properties properties) {
        this.properties = properties;
        this.isBackwardCompatible22 = "true".equals(getProperty("javax.el.bc2.2"));
    }

    @Override // javax.el.ExpressionFactory
    public Object coerceToType(Object obj, Class<?> type) {
        try {
            return ELSupport.coerceToType(obj, type, this.isBackwardCompatible22);
        } catch (IllegalArgumentException ex) {
            throw new ELException(ex);
        }
    }

    @Override // javax.el.ExpressionFactory
    public MethodExpression createMethodExpression(ELContext context, String expression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes) {
        MethodExpression methodExpression = new ExpressionBuilder(expression, context).createMethodExpression(expectedReturnType, expectedParamTypes);
        if (expectedParamTypes == null && !methodExpression.isParametersProvided()) {
            throw new NullPointerException(MessageFactory.get("error.method.nullParms"));
        }
        return methodExpression;
    }

    @Override // javax.el.ExpressionFactory
    public ValueExpression createValueExpression(ELContext context, String expression, Class<?> expectedType) {
        if (expectedType == null) {
            throw new NullPointerException(MessageFactory.get("error.value.expectedType"));
        }
        return new ExpressionBuilder(expression, context).createValueExpression(expectedType);
    }

    @Override // javax.el.ExpressionFactory
    public ValueExpression createValueExpression(Object instance, Class<?> expectedType) {
        if (expectedType == null) {
            throw new NullPointerException(MessageFactory.get("error.value.expectedType"));
        }
        return new ValueExpressionLiteral(instance, expectedType);
    }

    public String getProperty(String key) {
        if (this.properties == null) {
            return null;
        }
        return this.properties.getProperty(key);
    }

    @Override // javax.el.ExpressionFactory
    public ELResolver getStreamELResolver() {
        return new StreamELResolver();
    }

    @Override // javax.el.ExpressionFactory
    public Map<String, Method> getInitFunctionMap() {
        return new HashMap();
    }
}
