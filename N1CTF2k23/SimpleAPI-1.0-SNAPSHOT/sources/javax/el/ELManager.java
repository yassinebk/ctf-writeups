package javax.el;

import java.lang.reflect.Method;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELManager.class */
public class ELManager {
    private StandardELContext elContext;

    public static ExpressionFactory getExpressionFactory() {
        return ELUtil.getExpressionFactory();
    }

    public StandardELContext getELContext() {
        if (this.elContext == null) {
            this.elContext = new StandardELContext(getExpressionFactory());
        }
        return this.elContext;
    }

    public ELContext setELContext(ELContext context) {
        ELContext prevELContext = this.elContext;
        this.elContext = new StandardELContext(context);
        return prevELContext;
    }

    public void addBeanNameResolver(BeanNameResolver beanNameResolver) {
        getELContext().addELResolver(new BeanNameELResolver(beanNameResolver));
    }

    public void addELResolver(ELResolver elResolver) {
        getELContext().addELResolver(elResolver);
    }

    public void mapFunction(String prefix, String function, Method method) {
        getELContext().getFunctionMapper().mapFunction(prefix, function, method);
    }

    public void setVariable(String variable, ValueExpression expression) {
        getELContext().getVariableMapper().setVariable(variable, expression);
    }

    public void importStatic(String staticMemberName) throws ELException {
        getELContext().getImportHandler().importStatic(staticMemberName);
    }

    public void importClass(String className) throws ELException {
        getELContext().getImportHandler().importClass(className);
    }

    public void importPackage(String packageName) {
        getELContext().getImportHandler().importPackage(packageName);
    }

    public Object defineBean(String name, Object bean) {
        Object previousBean = getELContext().getBeans().get(name);
        getELContext().getBeans().put(name, bean);
        return previousBean;
    }

    public void addEvaluationListener(EvaluationListener listener) {
        getELContext().addEvaluationListener(listener);
    }
}
