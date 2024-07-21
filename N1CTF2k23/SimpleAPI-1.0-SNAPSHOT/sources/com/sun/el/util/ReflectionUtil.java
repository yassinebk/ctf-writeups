package com.sun.el.util;

import com.sun.el.lang.ELSupport;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodNotFoundException;
import javax.el.PropertyNotFoundException;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/util/ReflectionUtil.class */
public class ReflectionUtil {
    protected static final String[] EMPTY_STRING = new String[0];
    protected static final String[] PRIMITIVE_NAMES = {"boolean", "byte", "char", "double", "float", "int", "long", "short", "void"};
    protected static final Class[] PRIMITIVES = {Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE, Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE, Void.TYPE};

    private ReflectionUtil() {
    }

    public static Class forName(String name) throws ClassNotFoundException {
        if (null == name || "".equals(name)) {
            return null;
        }
        Class c = forNamePrimitive(name);
        if (c == null) {
            if (name.endsWith(ClassUtils.ARRAY_SUFFIX)) {
                String nc = name.substring(0, name.length() - 2);
                c = Array.newInstance(Class.forName(nc, true, Thread.currentThread().getContextClassLoader()), 0).getClass();
            } else {
                c = Class.forName(name, true, Thread.currentThread().getContextClassLoader());
            }
        }
        return c;
    }

    protected static Class forNamePrimitive(String name) {
        int p;
        if (name.length() <= 8 && (p = Arrays.binarySearch(PRIMITIVE_NAMES, name)) >= 0) {
            return PRIMITIVES[p];
        }
        return null;
    }

    public static Class[] toTypeArray(String[] s) throws ClassNotFoundException {
        if (s == null) {
            return null;
        }
        Class[] c = new Class[s.length];
        for (int i = 0; i < s.length; i++) {
            c[i] = forName(s[i]);
        }
        return c;
    }

    public static String[] toTypeNameArray(Class[] c) {
        if (c == null) {
            return null;
        }
        String[] s = new String[c.length];
        for (int i = 0; i < c.length; i++) {
            s[i] = c[i].getName();
        }
        return s;
    }

    public static PropertyDescriptor getPropertyDescriptor(Object base, Object property) throws ELException, PropertyNotFoundException {
        String name = ELSupport.coerceToString(property);
        try {
            PropertyDescriptor[] desc = Introspector.getBeanInfo(base.getClass()).getPropertyDescriptors();
            for (int i = 0; i < desc.length; i++) {
                if (desc[i].getName().equals(name)) {
                    return desc[i];
                }
            }
            throw new PropertyNotFoundException(MessageFactory.get("error.property.notfound", base, name));
        } catch (IntrospectionException ie) {
            throw new ELException((Throwable) ie);
        }
    }

    public static Object invokeMethod(ELContext context, Method m, Object base, Object[] params) {
        Object[] parameters = buildParameters(context, m.getParameterTypes(), m.isVarArgs(), params);
        try {
            return m.invoke(base, parameters);
        } catch (IllegalAccessException iae) {
            throw new ELException(iae);
        } catch (IllegalArgumentException iae2) {
            throw new ELException(iae2);
        } catch (InvocationTargetException ite) {
            throw new ELException(ite.getCause());
        }
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes, Object[] paramValues) {
        if (clazz == null || methodName == null) {
            throw new MethodNotFoundException(MessageFactory.get("error.method.notfound", clazz, methodName, paramString(paramTypes)));
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
        String errorMsg = MessageFactory.get("error.method.ambiguous", clazz, name, paramString(paramTypes));
        if (!assignableCandidates.isEmpty()) {
            return findMostSpecificWrapper(assignableCandidates, paramTypes, false, errorMsg);
        }
        if (!coercibleCandidates.isEmpty()) {
            return findMostSpecificWrapper(coercibleCandidates, paramTypes, true, errorMsg);
        }
        if (!varArgsCandidates.isEmpty()) {
            return findMostSpecificWrapper(varArgsCandidates, paramTypes, true, errorMsg);
        }
        throw new MethodNotFoundException(MessageFactory.get("error.method.notfound", clazz, name, paramString(paramTypes)));
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
            ELSupport.coerceToType(src, target);
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

    static Method getMethod(Class<?> type, Method m) {
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
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/util/ReflectionUtil$Wrapper.class */
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
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/util/ReflectionUtil$MethodWrapper.class */
    public static class MethodWrapper extends Wrapper {
        private final Method m;

        public MethodWrapper(Method m) {
            super();
            this.m = m;
        }

        @Override // com.sun.el.util.ReflectionUtil.Wrapper
        public Object unWrap() {
            return this.m;
        }

        @Override // com.sun.el.util.ReflectionUtil.Wrapper
        public Class<?>[] getParameterTypes() {
            return this.m.getParameterTypes();
        }

        @Override // com.sun.el.util.ReflectionUtil.Wrapper
        public boolean isVarArgs() {
            return this.m.isVarArgs();
        }

        @Override // com.sun.el.util.ReflectionUtil.Wrapper
        public boolean isBridge() {
            return this.m.isBridge();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/util/ReflectionUtil$ConstructorWrapper.class */
    private static class ConstructorWrapper extends Wrapper {
        private final Constructor<?> c;

        public ConstructorWrapper(Constructor<?> c) {
            super();
            this.c = c;
        }

        @Override // com.sun.el.util.ReflectionUtil.Wrapper
        public Object unWrap() {
            return this.c;
        }

        @Override // com.sun.el.util.ReflectionUtil.Wrapper
        public Class<?>[] getParameterTypes() {
            return this.c.getParameterTypes();
        }

        @Override // com.sun.el.util.ReflectionUtil.Wrapper
        public boolean isVarArgs() {
            return this.c.isVarArgs();
        }

        @Override // com.sun.el.util.ReflectionUtil.Wrapper
        public boolean isBridge() {
            return false;
        }
    }
}
