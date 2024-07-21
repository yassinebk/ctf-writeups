package org.apache.tomcat.util;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/IntrospectionUtils.class */
public final class IntrospectionUtils {
    private static final Log log = LogFactory.getLog(IntrospectionUtils.class);
    private static final StringManager sm = StringManager.getManager(IntrospectionUtils.class);
    private static final Hashtable<Class<?>, Method[]> objectMethods = new Hashtable<>();

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/IntrospectionUtils$PropertySource.class */
    public interface PropertySource {
        String getProperty(String str);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/IntrospectionUtils$SecurePropertySource.class */
    public interface SecurePropertySource extends PropertySource {
        String getProperty(String str, ClassLoader classLoader);
    }

    public static boolean setProperty(Object o, String name, String value) {
        return setProperty(o, name, value, true);
    }

    public static boolean setProperty(Object o, String name, String value, boolean invokeSetProperty) {
        if (log.isDebugEnabled()) {
            log.debug("IntrospectionUtils: setProperty(" + o.getClass() + " " + name + "=" + value + ")");
        }
        String setter = "set" + capitalize(name);
        try {
            Method[] methods = findMethods(o.getClass());
            Method setPropertyMethodVoid = null;
            Method setPropertyMethodBool = null;
            for (Method item : methods) {
                Class<?>[] paramT = item.getParameterTypes();
                if (setter.equals(item.getName()) && paramT.length == 1 && "java.lang.String".equals(paramT[0].getName())) {
                    item.invoke(o, value);
                    return true;
                }
            }
            for (Method method : methods) {
                boolean ok = true;
                if (setter.equals(method.getName()) && method.getParameterTypes().length == 1) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    Object[] params = new Object[1];
                    if ("java.lang.Integer".equals(paramType.getName()) || "int".equals(paramType.getName())) {
                        try {
                            params[0] = Integer.valueOf(value);
                        } catch (NumberFormatException e) {
                            ok = false;
                        }
                    } else if ("java.lang.Long".equals(paramType.getName()) || "long".equals(paramType.getName())) {
                        try {
                            params[0] = Long.valueOf(value);
                        } catch (NumberFormatException e2) {
                            ok = false;
                        }
                    } else if ("java.lang.Boolean".equals(paramType.getName()) || "boolean".equals(paramType.getName())) {
                        params[0] = Boolean.valueOf(value);
                    } else if ("java.net.InetAddress".equals(paramType.getName())) {
                        try {
                            params[0] = InetAddress.getByName(value);
                        } catch (UnknownHostException e3) {
                            if (log.isDebugEnabled()) {
                                log.debug("IntrospectionUtils: Unable to resolve host name:" + value);
                            }
                            ok = false;
                        }
                    } else if (log.isDebugEnabled()) {
                        log.debug("IntrospectionUtils: Unknown type " + paramType.getName());
                    }
                    if (ok) {
                        method.invoke(o, params);
                        return true;
                    }
                }
                if ("setProperty".equals(method.getName())) {
                    if (method.getReturnType() == Boolean.TYPE) {
                        setPropertyMethodBool = method;
                    } else {
                        setPropertyMethodVoid = method;
                    }
                }
            }
            if (invokeSetProperty) {
                if (setPropertyMethodBool != null || setPropertyMethodVoid != null) {
                    Object[] params2 = {name, value};
                    if (setPropertyMethodBool != null) {
                        try {
                            return ((Boolean) setPropertyMethodBool.invoke(o, params2)).booleanValue();
                        } catch (IllegalArgumentException biae) {
                            if (setPropertyMethodVoid != null) {
                                setPropertyMethodVoid.invoke(o, params2);
                                return true;
                            }
                            throw biae;
                        }
                    }
                    setPropertyMethodVoid.invoke(o, params2);
                    return true;
                }
                return false;
            }
            return false;
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException e4) {
            log.warn(sm.getString("introspectionUtils.setPropertyError", name, value, o.getClass()), e4);
            return false;
        } catch (InvocationTargetException e5) {
            ExceptionUtils.handleThrowable(e5.getCause());
            log.warn(sm.getString("introspectionUtils.setPropertyError", name, value, o.getClass()), e5);
            return false;
        }
    }

    public static Object getProperty(Object o, String name) {
        String getter = BeanUtil.PREFIX_GETTER_GET + capitalize(name);
        String isGetter = BeanUtil.PREFIX_GETTER_IS + capitalize(name);
        try {
            Method[] methods = findMethods(o.getClass());
            Method getPropertyMethod = null;
            for (Method method : methods) {
                Class<?>[] paramT = method.getParameterTypes();
                if (getter.equals(method.getName()) && paramT.length == 0) {
                    return method.invoke(o, null);
                }
                if (isGetter.equals(method.getName()) && paramT.length == 0) {
                    return method.invoke(o, null);
                }
                if ("getProperty".equals(method.getName())) {
                    getPropertyMethod = method;
                }
            }
            if (getPropertyMethod != null) {
                Object[] params = {name};
                return getPropertyMethod.invoke(o, params);
            }
            return null;
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
            log.warn(sm.getString("introspectionUtils.getPropertyError", name, o.getClass()), e);
            return null;
        } catch (InvocationTargetException e2) {
            if (e2.getCause() instanceof NullPointerException) {
                return null;
            }
            ExceptionUtils.handleThrowable(e2.getCause());
            log.warn(sm.getString("introspectionUtils.getPropertyError", name, o.getClass()), e2);
            return null;
        }
    }

    @Deprecated
    public static String replaceProperties(String value, Hashtable<Object, Object> staticProp, PropertySource[] dynamicProp) {
        return replaceProperties(value, staticProp, dynamicProp, null);
    }

    public static String replaceProperties(String value, Hashtable<Object, Object> staticProp, PropertySource[] dynamicProp, ClassLoader classLoader) {
        int prev;
        if (value.indexOf(36) < 0) {
            return value;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (true) {
            prev = i;
            int pos = value.indexOf(36, prev);
            if (pos < 0) {
                break;
            }
            if (pos > 0) {
                sb.append(value.substring(prev, pos));
            }
            if (pos == value.length() - 1) {
                sb.append('$');
                i = pos + 1;
            } else if (value.charAt(pos + 1) != '{') {
                sb.append('$');
                i = pos + 1;
            } else {
                int endName = value.indexOf(125, pos);
                if (endName < 0) {
                    sb.append(value.substring(pos));
                    i = value.length();
                } else {
                    String n = value.substring(pos + 2, endName);
                    String v = getProperty(n, staticProp, dynamicProp, classLoader);
                    if (v == null) {
                        int col = n.indexOf(CoreConstants.DEFAULT_VALUE_SEPARATOR);
                        if (col != -1) {
                            String dV = n.substring(col + 2);
                            v = getProperty(n.substring(0, col), staticProp, dynamicProp, classLoader);
                            if (v == null) {
                                v = dV;
                            }
                        } else {
                            v = "${" + n + "}";
                        }
                    }
                    sb.append(v);
                    i = endName + 1;
                }
            }
        }
        if (prev < value.length()) {
            sb.append(value.substring(prev));
        }
        return sb.toString();
    }

    private static String getProperty(String name, Hashtable<Object, Object> staticProp, PropertySource[] dynamicProp, ClassLoader classLoader) {
        String v = null;
        if (staticProp != null) {
            v = (String) staticProp.get(name);
        }
        if (v == null && dynamicProp != null) {
            for (PropertySource propertySource : dynamicProp) {
                if (propertySource instanceof SecurePropertySource) {
                    v = ((SecurePropertySource) propertySource).getProperty(name, classLoader);
                } else {
                    v = propertySource.getProperty(name);
                }
                if (v != null) {
                    break;
                }
            }
        }
        return v;
    }

    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static void clear() {
        objectMethods.clear();
    }

    public static Method[] findMethods(Class<?> c) {
        Method[] methods = objectMethods.get(c);
        if (methods != null) {
            return methods;
        }
        Method[] methods2 = c.getMethods();
        objectMethods.put(c, methods2);
        return methods2;
    }

    public static Method findMethod(Class<?> c, String name, Class<?>[] params) {
        Method[] methods = findMethods(c);
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                Class<?>[] methodParams = method.getParameterTypes();
                if (params == null && methodParams.length == 0) {
                    return method;
                }
                if (params.length != methodParams.length) {
                    continue;
                } else {
                    boolean found = true;
                    int j = 0;
                    while (true) {
                        if (j < params.length) {
                            if (params[j] == methodParams[j]) {
                                j++;
                            } else {
                                found = false;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (found) {
                        return method;
                    }
                }
            }
        }
        return null;
    }

    public static Object callMethod1(Object target, String methodN, Object param1, String typeParam1, ClassLoader cl) throws Exception {
        if (target == null || methodN == null || param1 == null) {
            throw new IllegalArgumentException(sm.getString("introspectionUtils.nullParameter"));
        }
        if (log.isDebugEnabled()) {
            log.debug("IntrospectionUtils: callMethod1 " + target.getClass().getName() + " " + param1.getClass().getName() + " " + typeParam1);
        }
        Class<?>[] params = new Class[1];
        if (typeParam1 == null) {
            params[0] = param1.getClass();
        } else {
            params[0] = cl.loadClass(typeParam1);
        }
        Method m = findMethod(target.getClass(), methodN, params);
        if (m == null) {
            throw new NoSuchMethodException(target.getClass().getName() + " " + methodN);
        }
        try {
            return m.invoke(target, param1);
        } catch (InvocationTargetException ie) {
            ExceptionUtils.handleThrowable(ie.getCause());
            throw ie;
        }
    }

    public static Object callMethodN(Object target, String methodN, Object[] params, Class<?>[] typeParams) throws Exception {
        Method m = findMethod(target.getClass(), methodN, typeParams);
        if (m == null) {
            if (log.isDebugEnabled()) {
                log.debug("IntrospectionUtils: Can't find method " + methodN + " in " + target + " CLASS " + target.getClass());
                return null;
            }
            return null;
        }
        try {
            Object o = m.invoke(target, params);
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName()).append('.').append(methodN).append("( ");
                for (int i = 0; i < params.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(params[i]);
                }
                sb.append(")");
                log.debug("IntrospectionUtils:" + sb.toString());
            }
            return o;
        } catch (InvocationTargetException ie) {
            ExceptionUtils.handleThrowable(ie.getCause());
            throw ie;
        }
    }

    public static Object convert(String object, Class<?> paramType) {
        Object result = null;
        if ("java.lang.String".equals(paramType.getName())) {
            result = object;
        } else if ("java.lang.Integer".equals(paramType.getName()) || "int".equals(paramType.getName())) {
            try {
                result = Integer.valueOf(object);
            } catch (NumberFormatException e) {
            }
        } else if ("java.lang.Boolean".equals(paramType.getName()) || "boolean".equals(paramType.getName())) {
            result = Boolean.valueOf(object);
        } else if ("java.net.InetAddress".equals(paramType.getName())) {
            try {
                result = InetAddress.getByName(object);
            } catch (UnknownHostException e2) {
                if (log.isDebugEnabled()) {
                    log.debug("IntrospectionUtils: Unable to resolve host name:" + object);
                }
            }
        } else if (log.isDebugEnabled()) {
            log.debug("IntrospectionUtils: Unknown type " + paramType.getName());
        }
        if (result == null) {
            throw new IllegalArgumentException(sm.getString("introspectionUtils.conversionError", object, paramType.getName()));
        }
        return result;
    }

    public static boolean isInstance(Class<?> clazz, String type) {
        if (type.equals(clazz.getName())) {
            return true;
        }
        Class<?>[] ifaces = clazz.getInterfaces();
        for (Class<?> iface : ifaces) {
            if (isInstance(iface, type)) {
                return true;
            }
        }
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz == null) {
            return false;
        }
        return isInstance(superClazz, type);
    }
}
