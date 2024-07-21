package javax.el;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELContext.class */
public abstract class ELContext {
    private boolean resolved;
    private HashMap<Class<?>, Object> map = new HashMap<>();
    private transient List<EvaluationListener> listeners;
    private Stack<Map<String, Object>> lambdaArgs;
    private ImportHandler importHandler;
    private Locale locale;

    public abstract ELResolver getELResolver();

    public abstract FunctionMapper getFunctionMapper();

    public abstract VariableMapper getVariableMapper();

    public void setPropertyResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public void setPropertyResolved(Object base, Object property) {
        setPropertyResolved(true);
        notifyPropertyResolved(base, property);
    }

    public boolean isPropertyResolved() {
        return this.resolved;
    }

    public void putContext(Class key, Object contextObject) {
        if (key == null || contextObject == null) {
            throw new NullPointerException();
        }
        this.map.put(key, contextObject);
    }

    public Object getContext(Class key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return this.map.get(key);
    }

    public ImportHandler getImportHandler() {
        if (this.importHandler == null) {
            this.importHandler = new ImportHandler();
        }
        return this.importHandler;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void addEvaluationListener(EvaluationListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList();
        }
        this.listeners.add(listener);
    }

    public List<EvaluationListener> getEvaluationListeners() {
        return this.listeners;
    }

    public void notifyBeforeEvaluation(String expr) {
        if (getEvaluationListeners() == null) {
            return;
        }
        for (EvaluationListener listener : getEvaluationListeners()) {
            listener.beforeEvaluation(this, expr);
        }
    }

    public void notifyAfterEvaluation(String expr) {
        if (getEvaluationListeners() == null) {
            return;
        }
        for (EvaluationListener listener : getEvaluationListeners()) {
            listener.afterEvaluation(this, expr);
        }
    }

    public void notifyPropertyResolved(Object base, Object property) {
        if (getEvaluationListeners() == null) {
            return;
        }
        for (EvaluationListener listener : getEvaluationListeners()) {
            listener.propertyResolved(this, base, property);
        }
    }

    public boolean isLambdaArgument(String arg) {
        if (this.lambdaArgs == null) {
            return false;
        }
        for (int i = this.lambdaArgs.size() - 1; i >= 0; i--) {
            Map<String, Object> lmap = this.lambdaArgs.elementAt(i);
            if (lmap.containsKey(arg)) {
                return true;
            }
        }
        return false;
    }

    public Object getLambdaArgument(String arg) {
        if (this.lambdaArgs == null) {
            return null;
        }
        for (int i = this.lambdaArgs.size() - 1; i >= 0; i--) {
            Map<String, Object> lmap = this.lambdaArgs.elementAt(i);
            Object v = lmap.get(arg);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    public void enterLambdaScope(Map<String, Object> args) {
        if (this.lambdaArgs == null) {
            this.lambdaArgs = new Stack<>();
        }
        this.lambdaArgs.push(args);
    }

    public void exitLambdaScope() {
        if (this.lambdaArgs != null) {
            this.lambdaArgs.pop();
        }
    }

    public Object convertToType(Object obj, Class<?> targetType) {
        boolean propertyResolvedSave = isPropertyResolved();
        try {
            try {
                setPropertyResolved(false);
                ELResolver elResolver = getELResolver();
                if (elResolver != null) {
                    Object res = elResolver.convertToType(this, obj, targetType);
                    if (isPropertyResolved()) {
                        return res;
                    }
                }
                setPropertyResolved(propertyResolvedSave);
                ExpressionFactory exprFactory = (ExpressionFactory) getContext(ExpressionFactory.class);
                if (exprFactory == null) {
                    exprFactory = ELUtil.getExpressionFactory();
                }
                return exprFactory.coerceToType(obj, targetType);
            } catch (ELException ex) {
                throw ex;
            } catch (Exception ex2) {
                throw new ELException(ex2);
            }
        } finally {
            setPropertyResolved(propertyResolvedSave);
        }
    }
}
