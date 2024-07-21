package javax.el;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELProcessor.class */
public class ELProcessor {
    private ELManager elManager = new ELManager();
    private ExpressionFactory factory = ELManager.getExpressionFactory();

    public ELManager getELManager() {
        return this.elManager;
    }

    public Object eval(String expression) {
        return getValue(expression, Object.class);
    }

    public Object getValue(String expression, Class<?> expectedType) {
        ValueExpression exp = this.factory.createValueExpression(this.elManager.getELContext(), bracket(expression), expectedType);
        return exp.getValue(this.elManager.getELContext());
    }

    public void setValue(String expression, Object value) {
        ValueExpression exp = this.factory.createValueExpression(this.elManager.getELContext(), bracket(expression), Object.class);
        exp.setValue(this.elManager.getELContext(), value);
    }

    public void setVariable(String var, String expression) {
        ValueExpression exp = this.factory.createValueExpression(this.elManager.getELContext(), bracket(expression), Object.class);
        this.elManager.setVariable(var, exp);
    }

    public void defineFunction(String prefix, String function, String className, String method) throws ClassNotFoundException, NoSuchMethodException {
        Method[] declaredMethods;
        if (prefix == null || function == null || className == null || method == null) {
            throw new NullPointerException("Null argument for defineFunction");
        }
        Method meth = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = getClass().getClassLoader();
        }
        Class<?> klass = Class.forName(className, false, loader);
        int j = method.indexOf(40);
        if (j < 0) {
            for (Method m : klass.getDeclaredMethods()) {
                if (m.getName().equals(method)) {
                    meth = m;
                }
            }
            if (meth == null) {
                throw new NoSuchMethodException("Bad method name: " + method);
            }
        } else {
            int p = method.indexOf(32);
            if (p < 0) {
                throw new NoSuchMethodException("Bad method signature: " + method);
            }
            String methodName = method.substring(p + 1, j).trim();
            int p2 = method.indexOf(41, j + 1);
            if (p2 < 0) {
                throw new NoSuchMethodException("Bad method signature: " + method);
            }
            String[] params = method.substring(j + 1, p2).split(",");
            Class<?>[] paramTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = toClass(params[i], loader);
            }
            meth = klass.getDeclaredMethod(methodName, paramTypes);
        }
        if (!Modifier.isStatic(meth.getModifiers())) {
            throw new NoSuchMethodException("The method specified in defineFunction must be static: " + meth);
        }
        if (function.equals("")) {
            function = method;
        }
        this.elManager.mapFunction(prefix, function, meth);
    }

    public void defineFunction(String prefix, String function, Method method) throws NoSuchMethodException {
        if (prefix == null || function == null || method == null) {
            throw new NullPointerException("Null argument for defineFunction");
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new NoSuchMethodException("The method specified in defineFunction must be static: " + method);
        }
        if (function.equals("")) {
            function = method.getName();
        }
        this.elManager.mapFunction(prefix, function, method);
    }

    public void defineBean(String name, Object bean) {
        this.elManager.defineBean(name, bean);
    }

    private static Class<?> toClass(String type, ClassLoader loader) throws ClassNotFoundException {
        Class<?> c;
        int i0 = type.indexOf(91);
        int dims = 0;
        if (i0 > 0) {
            for (int i = 0; i < type.length(); i++) {
                if (type.charAt(i) == '[') {
                    dims++;
                }
            }
            type = type.substring(0, i0);
        }
        if ("boolean".equals(type)) {
            c = Boolean.TYPE;
        } else if ("char".equals(type)) {
            c = Character.TYPE;
        } else if ("byte".equals(type)) {
            c = Byte.TYPE;
        } else if ("short".equals(type)) {
            c = Short.TYPE;
        } else if ("int".equals(type)) {
            c = Integer.TYPE;
        } else if ("long".equals(type)) {
            c = Long.TYPE;
        } else if ("float".equals(type)) {
            c = Float.TYPE;
        } else if ("double".equals(type)) {
            c = Double.TYPE;
        } else {
            c = loader.loadClass(type);
        }
        if (dims == 0) {
            return c;
        }
        if (dims == 1) {
            return Array.newInstance(c, 1).getClass();
        }
        return Array.newInstance(c, new int[dims]).getClass();
    }

    private String bracket(String expression) {
        return "${" + expression + '}';
    }
}
