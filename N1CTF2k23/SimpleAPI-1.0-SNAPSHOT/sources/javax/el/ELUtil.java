package javax.el;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.springframework.cglib.core.Constants;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELUtil.class */
public class ELUtil {
    public static ExpressionFactory exprFactory = ExpressionFactory.newInstance();
    private static ThreadLocal<Map<String, ResourceBundle>> instance = new ThreadLocal<Map<String, ResourceBundle>>() { // from class: javax.el.ELUtil.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Map<String, ResourceBundle> initialValue() {
            return null;
        }
    };

    private ELUtil() {
    }

    private static Map<String, ResourceBundle> getCurrentInstance() {
        Map<String, ResourceBundle> result = instance.get();
        if (result == null) {
            result = new HashMap<>();
            setCurrentInstance(result);
        }
        return result;
    }

    private static void setCurrentInstance(Map<String, ResourceBundle> context) {
        instance.set(context);
    }

    public static String getExceptionMessageString(ELContext context, String messageId) {
        return getExceptionMessageString(context, messageId, null);
    }

    public static String getExceptionMessageString(ELContext context, String messageId, Object[] params) {
        String result = "";
        if (null == context || null == messageId) {
            return result;
        }
        Locale locale = context.getLocale();
        Locale locale2 = locale;
        if (null == locale) {
            locale2 = Locale.getDefault();
        }
        if (locale2 != null) {
            Map<String, ResourceBundle> threadMap = getCurrentInstance();
            ResourceBundle resourceBundle = threadMap.get(locale2.toString());
            ResourceBundle resourceBundle2 = resourceBundle;
            if (null == resourceBundle) {
                resourceBundle2 = ResourceBundle.getBundle("javax.el.PrivateMessages", locale2);
                threadMap.put(locale2.toString(), resourceBundle2);
            }
            if (null != resourceBundle2) {
                try {
                    result = resourceBundle2.getString(messageId);
                    if (null != params) {
                        result = MessageFormat.format(result, params);
                    }
                } catch (IllegalArgumentException e) {
                    result = "Can't get localized message: parameters to message appear to be incorrect.  Message to format: " + messageId;
                } catch (MissingResourceException e2) {
                    result = "Missing Resource in Jakarta Expression Language implementation: ???" + messageId + "???";
                } catch (Exception e3) {
                    result = "Exception resolving message in Jakarta Expression Language implementation: ???" + messageId + "???";
                }
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ExpressionFactory getExpressionFactory() {
        return exprFactory;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Constructor<?> findConstructor(Class<?> klass, Class<?>[] paramTypes, Object[] params) {
        if (klass == null) {
            throw new MethodNotFoundException("Method not found: " + klass + "." + Constants.CONSTRUCTOR_NAME + "(" + paramString(paramTypes) + ")");
        }
        if (paramTypes == null) {
            paramTypes = getTypesFromValues(params);
        }
        Constructor<?>[] constructors = klass.getConstructors();
        List<Wrapper> wrappers = Wrapper.wrap(constructors);
        Wrapper result = findWrapper(klass, wrappers, Constants.CONSTRUCTOR_NAME, paramTypes, params);
        if (result == null) {
            return null;
        }
        return getConstructor(klass, (Constructor) result.unWrap());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object invokeConstructor(ELContext context, Constructor<?> constructor, Object[] params) {
        Object[] parameters = buildParameters(context, constructor.getParameterTypes(), constructor.isVarArgs(), params);
        try {
            return constructor.newInstance(parameters);
        } catch (IllegalAccessException iae) {
            throw new ELException(iae);
        } catch (IllegalArgumentException iae2) {
            throw new ELException(iae2);
        } catch (InstantiationException ie) {
            throw new ELException(ie.getCause());
        } catch (InvocationTargetException ite) {
            throw new ELException(ite.getCause());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Method findMethod(Class<?> klass, String methodName, Class<?>[] paramTypes, Object[] params, boolean staticOnly) {
        Method method = findMethod(klass, methodName, paramTypes, params);
        if (staticOnly && !Modifier.isStatic(method.getModifiers())) {
            throw new MethodNotFoundException("Method " + methodName + "for class " + klass + " not found or accessible");
        }
        return method;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object invokeMethod(ELContext context, Method method, Object base, Object[] params) {
        Object[] parameters = buildParameters(context, method.getParameterTypes(), method.isVarArgs(), params);
        try {
            return method.invoke(base, parameters);
        } catch (IllegalAccessException iae) {
            throw new ELException(iae);
        } catch (IllegalArgumentException iae2) {
            throw new ELException(iae2);
        } catch (InvocationTargetException ite) {
            throw new ELException(ite.getCause());
        }
    }

    static Method findMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes, Object[] paramValues) {
        if (clazz == null || methodName == null) {
            throw new MethodNotFoundException("Method not found: " + clazz + "." + methodName + "(" + paramString(paramTypes) + ")");
        }
        if (paramTypes == null) {
            paramTypes = getTypesFromValues(paramValues);
        }
        Method[] methods = clazz.getMethods();
        List<Wrapper> wrappers = Wrapper.wrap(methods, methodName);
        Wrapper result = findWrapper(clazz, wrappers, methodName, paramTypes, paramValues);
        if (result == null) {
            return null;
        }
        return getMethod(clazz, (Method) result.unWrap());
    }

    private static Wrapper findWrapper(Class<?> clazz, List<Wrapper> wrappers, String name, Class<?>[] paramTypes, Object[] paramValues) {
        int paramCount;
        int mParamCount;
        List<Wrapper> assignableCandidates = new ArrayList<>();
        List<Wrapper> coercibleCandidates = new ArrayList<>();
        List<Wrapper> varArgsCandidates = new ArrayList<>();
        if (paramTypes == null) {
            paramCount = 0;
        } else {
            paramCount = paramTypes.length;
        }
        for (Wrapper w : wrappers) {
            Class<?>[] mParamTypes = w.getParameterTypes();
            if (mParamTypes == null) {
                mParamCount = 0;
            } else {
                mParamCount = mParamTypes.length;
            }
            if (paramCount == mParamCount || (w.isVarArgs() && paramCount >= mParamCount - 1)) {
                boolean assignable = false;
                boolean coercible = false;
                boolean varArgs = false;
                boolean noMatch = false;
                int i = 0;
                while (true) {
                    if (i >= mParamCount) {
                        break;
                    }
                    if (i == mParamCount - 1 && w.isVarArgs()) {
                        varArgs = true;
                        if (mParamCount != paramCount || mParamTypes[i] != paramTypes[i]) {
                            Class<?> varType = mParamTypes[i].getComponentType();
                            for (int j = i; j < paramCount; j++) {
                                if (!isAssignableFrom(paramTypes[j], varType) && (paramValues == null || j >= paramValues.length || !isCoercibleFrom(paramValues[j], varType))) {
                                    noMatch = true;
                                    break;
                                }
                            }
                        }
                    } else if (mParamTypes[i].equals(paramTypes[i])) {
                        continue;
                    } else if (isAssignableFrom(paramTypes[i], mParamTypes[i])) {
                        assignable = true;
                    } else if (paramValues == null || i >= paramValues.length) {
                        break;
                    } else if (isCoercibleFrom(paramValues[i], mParamTypes[i])) {
                        coercible = true;
                    } else {
                        noMatch = true;
                        break;
                    }
                    i++;
                }
                noMatch = true;
                if (noMatch) {
                    continue;
                } else if (varArgs) {
                    varArgsCandidates.add(w);
                } else if (coercible) {
                    coercibleCandidates.add(w);
                } else if (assignable) {
                    assignableCandidates.add(w);
                } else {
                    return w;
                }
            }
        }
        String errorMsg = "Unable to find unambiguous method: " + clazz + "." + name + "(" + paramString(paramTypes) + ")";
        if (!assignableCandidates.isEmpty()) {
            return findMostSpecificWrapper(assignableCandidates, paramTypes, false, errorMsg);
        }
        if (!coercibleCandidates.isEmpty()) {
            return findMostSpecificWrapper(coercibleCandidates, paramTypes, true, errorMsg);
        }
        if (!varArgsCandidates.isEmpty()) {
            return findMostSpecificWrapper(varArgsCandidates, paramTypes, true, errorMsg);
        }
        throw new MethodNotFoundException("Method not found: " + clazz + "." + name + "(" + paramString(paramTypes) + ")");
    }

    private static Wrapper findMostSpecificWrapper(List<Wrapper> candidates, Class<?>[] matchingTypes, boolean elSpecific, String errorMsg) {
        List<Wrapper> ambiguouses = new ArrayList<>();
        for (Wrapper candidate : candidates) {
            boolean lessSpecific = false;
            Iterator<Wrapper> it = ambiguouses.iterator();
            while (it.hasNext()) {
                int result = isMoreSpecific(candidate, it.next(), matchingTypes, elSpecific);
                if (result == 1) {
                    it.remove();
                } else if (result == -1) {
                    lessSpecific = true;
                }
            }
            if (!lessSpecific) {
                ambiguouses.add(candidate);
            }
        }
        if (ambiguouses.size() > 1) {
            throw new MethodNotFoundException(errorMsg);
        }
        return ambiguouses.get(0);
    }

    private static int isMoreSpecific(Wrapper wrapper1, Wrapper wrapper2, Class<?>[] matchingTypes, boolean elSpecific) {
        Class<?>[] paramTypes1 = wrapper1.getParameterTypes();
        Class<?>[] paramTypes2 = wrapper2.getParameterTypes();
        if (wrapper1.isVarArgs()) {
            int length = Math.max(Math.max(paramTypes1.length, paramTypes2.length), matchingTypes.length);
            paramTypes1 = getComparingParamTypesForVarArgsMethod(paramTypes1, length);
            paramTypes2 = getComparingParamTypesForVarArgsMethod(paramTypes2, length);
            if (length > matchingTypes.length) {
                Class<?>[] matchingTypes2 = new Class[length];
                System.arraycopy(matchingTypes, 0, matchingTypes2, 0, matchingTypes.length);
                matchingTypes = matchingTypes2;
            }
        }
        int result = 0;
        for (int i = 0; i < paramTypes1.length; i++) {
            if (paramTypes1[i] != paramTypes2[i]) {
                int r2 = isMoreSpecific(paramTypes1[i], paramTypes2[i], matchingTypes[i], elSpecific);
                if (r2 == 1) {
                    if (result == -1) {
                        return 0;
                    }
                    result = 1;
                } else if (r2 != -1 || result == 1) {
                    return 0;
                } else {
                    result = -1;
                }
            }
        }
        if (result == 0) {
            result = Boolean.compare(wrapper1.isBridge(), wrapper2.isBridge());
        }
        return result;
    }

    private static int isMoreSpecific(Class<?> type1, Class<?> type2, Class<?> matchingType, boolean elSpecific) {
        Class<?> type12 = getBoxingTypeIfPrimitive(type1);
        Class<?> type22 = getBoxingTypeIfPrimitive(type2);
        if (type22.isAssignableFrom(type12)) {
            return 1;
        }
        if (type12.isAssignableFrom(type22)) {
            return -1;
        }
        if (elSpecific && matchingType != null && Number.class.isAssignableFrom(matchingType)) {
            boolean b1 = Number.class.isAssignableFrom(type12) || type12.isPrimitive();
            boolean b2 = Number.class.isAssignableFrom(type22) || type22.isPrimitive();
            if (b1 && !b2) {
                return 1;
            }
            if (b2 && !b1) {
                return -1;
            }
            return 0;
        }
        return 0;
    }

    private static Class<?> getBoxingTypeIfPrimitive(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == Boolean.TYPE) {
                return Boolean.class;
            }
            if (clazz == Character.TYPE) {
                return Character.class;
            }
            if (clazz == Byte.TYPE) {
                return Byte.class;
            }
            if (clazz == Short.TYPE) {
                return Short.class;
            }
            if (clazz == Integer.TYPE) {
                return Integer.class;
            }
            if (clazz == Long.TYPE) {
                return Long.class;
            }
            if (clazz == Float.TYPE) {
                return Float.class;
            }
            return Double.class;
        }
        return clazz;
    }

    private static Class<?>[] getComparingParamTypesForVarArgsMethod(Class<?>[] paramTypes, int length) {
        Class<?>[] result = new Class[length];
        System.arraycopy(paramTypes, 0, result, 0, paramTypes.length - 1);
        Class<?> type = paramTypes[paramTypes.length - 1].getComponentType();
        for (int i = paramTypes.length - 1; i < length; i++) {
            result[i] = type;
        }
        return result;
    }

    private static final String paramString(Class<?>[] types) {
        if (types != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < types.length; i++) {
                if (types[i] == null) {
                    sb.append("null, ");
                } else {
                    sb.append(types[i].getName()).append(", ");
                }
            }
            if (sb.length() > 2) {
                sb.setLength(sb.length() - 2);
            }
            return sb.toString();
        }
        return null;
    }

    static boolean isAssignableFrom(Class<?> src, Class<?> target) {
        if (src == null) {
            return true;
        }
        return getBoxingTypeIfPrimitive(target).isAssignableFrom(src);
    }

    private static boolean isCoercibleFrom(Object src, Class<?> target) {
        try {
            getExpressionFactory().coerceToType(src, target);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static Class<?>[] getTypesFromValues(Object[] values) {
        if (values == null) {
            return null;
        }
        Class<?>[] result = new Class[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                result[i] = null;
            } else {
                result[i] = values[i].getClass();
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Method getMethod(Class<?> type, Method m) {
        Method mp;
        if (m == null || Modifier.isPublic(type.getModifiers())) {
            return m;
        }
        Class<?>[] inf = type.getInterfaces();
        for (Class<?> cls : inf) {
            try {
                Method mp2 = cls.getMethod(m.getName(), m.getParameterTypes());
                mp = getMethod(mp2.getDeclaringClass(), mp2);
            } catch (NoSuchMethodException e) {
            }
            if (mp != null) {
                return mp;
            }
        }
        Class<?> sup = type.getSuperclass();
        if (sup != null) {
            try {
                Method mp3 = sup.getMethod(m.getName(), m.getParameterTypes());
                Method mp4 = getMethod(mp3.getDeclaringClass(), mp3);
                if (mp4 != null) {
                    return mp4;
                }
                return null;
            } catch (NoSuchMethodException e2) {
                return null;
            }
        }
        return null;
    }

    static Constructor<?> getConstructor(Class<?> type, Constructor<?> c) {
        if (c == null || Modifier.isPublic(type.getModifiers())) {
            return c;
        }
        Class<?> sup = type.getSuperclass();
        if (sup != null) {
            try {
                Constructor<?> cp = sup.getConstructor(c.getParameterTypes());
                Constructor<?> cp2 = getConstructor(cp.getDeclaringClass(), cp);
                if (cp2 != null) {
                    return cp2;
                }
                return null;
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
        return null;
    }

    static Object[] buildParameters(ELContext context, Class<?>[] parameterTypes, boolean isVarArgs, Object[] params) {
        Object[] parameters = null;
        if (parameterTypes.length > 0) {
            parameters = new Object[parameterTypes.length];
            int paramCount = params == null ? 0 : params.length;
            if (isVarArgs) {
                int varArgIndex = parameterTypes.length - 1;
                for (int i = 0; i < varArgIndex && i < paramCount; i++) {
                    parameters[i] = context.convertToType(params[i], parameterTypes[i]);
                }
                if (parameterTypes.length == paramCount && parameterTypes[varArgIndex] == params[varArgIndex].getClass()) {
                    parameters[varArgIndex] = params[varArgIndex];
                } else {
                    Class<?> varArgClass = parameterTypes[varArgIndex].getComponentType();
                    Object varargs = Array.newInstance(varArgClass, paramCount - varArgIndex);
                    for (int i2 = varArgIndex; i2 < paramCount; i2++) {
                        Array.set(varargs, i2 - varArgIndex, context.convertToType(params[i2], varArgClass));
                    }
                    parameters[varArgIndex] = varargs;
                }
            } else {
                for (int i3 = 0; i3 < parameterTypes.length && i3 < paramCount; i3++) {
                    parameters[i3] = context.convertToType(params[i3], parameterTypes[i3]);
                }
            }
        }
        return parameters;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELUtil$Wrapper.class */
    public static abstract class Wrapper {
        public abstract Object unWrap();

        public abstract Class<?>[] getParameterTypes();

        public abstract boolean isVarArgs();

        public abstract boolean isBridge();

        private Wrapper() {
        }

        public static List<Wrapper> wrap(Method[] methods, String name) {
            List<Wrapper> result = new ArrayList<>();
            for (Method method : methods) {
                if (method.getName().equals(name)) {
                    result.add(new MethodWrapper(method));
                }
            }
            return result;
        }

        public static List<Wrapper> wrap(Constructor<?>[] constructors) {
            List<Wrapper> result = new ArrayList<>();
            for (Constructor<?> constructor : constructors) {
                result.add(new ConstructorWrapper(constructor));
            }
            return result;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELUtil$MethodWrapper.class */
    public static class MethodWrapper extends Wrapper {
        private final Method m;

        public MethodWrapper(Method m) {
            super();
            this.m = m;
        }

        @Override // javax.el.ELUtil.Wrapper
        public Object unWrap() {
            return this.m;
        }

        @Override // javax.el.ELUtil.Wrapper
        public Class<?>[] getParameterTypes() {
            return this.m.getParameterTypes();
        }

        @Override // javax.el.ELUtil.Wrapper
        public boolean isVarArgs() {
            return this.m.isVarArgs();
        }

        @Override // javax.el.ELUtil.Wrapper
        public boolean isBridge() {
            return this.m.isBridge();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELUtil$ConstructorWrapper.class */
    public static class ConstructorWrapper extends Wrapper {
        private final Constructor<?> c;

        public ConstructorWrapper(Constructor<?> c) {
            super();
            this.c = c;
        }

        @Override // javax.el.ELUtil.Wrapper
        public Object unWrap() {
            return this.c;
        }

        @Override // javax.el.ELUtil.Wrapper
        public Class<?>[] getParameterTypes() {
            return this.c.getParameterTypes();
        }

        @Override // javax.el.ELUtil.Wrapper
        public boolean isVarArgs() {
            return this.c.isVarArgs();
        }

        @Override // javax.el.ELUtil.Wrapper
        public boolean isBridge() {
            return false;
        }
    }
}
