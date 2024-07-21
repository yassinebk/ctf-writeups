package javax.el;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ExpressionFactory.class */
public abstract class ExpressionFactory {
    public abstract ValueExpression createValueExpression(ELContext eLContext, String str, Class<?> cls);

    public abstract ValueExpression createValueExpression(Object obj, Class<?> cls);

    public abstract MethodExpression createMethodExpression(ELContext eLContext, String str, Class<?> cls, Class<?>[] clsArr);

    public abstract Object coerceToType(Object obj, Class<?> cls);

    public static ExpressionFactory newInstance() {
        return newInstance(null);
    }

    public static ExpressionFactory newInstance(Properties properties) {
        return (ExpressionFactory) FactoryFinder.find("javax.el.ExpressionFactory", "com.sun.el.ExpressionFactoryImpl", properties);
    }

    public ELResolver getStreamELResolver() {
        return null;
    }

    public Map<String, Method> getInitFunctionMap() {
        return null;
    }
}
