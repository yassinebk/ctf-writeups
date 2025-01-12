package org.apache.logging.log4j.util;

import java.lang.reflect.Method;
import java.util.Stack;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/util/StackLocator.class */
public final class StackLocator {
    static final int JDK_7u25_OFFSET;
    private static final Method GET_CALLER_CLASS;
    private static final StackLocator INSTANCE;

    static {
        Method getCallerClass;
        int java7u25CompensationOffset = 0;
        try {
            Class<?> sunReflectionClass = LoaderUtil.loadClass("sun.reflect.Reflection");
            getCallerClass = sunReflectionClass.getDeclaredMethod("getCallerClass", Integer.TYPE);
            Object o = getCallerClass.invoke(null, 0);
            getCallerClass.invoke(null, 0);
            if (o == null || o != sunReflectionClass) {
                getCallerClass = null;
                java7u25CompensationOffset = -1;
            } else if (getCallerClass.invoke(null, 1) == sunReflectionClass) {
                System.out.println("WARNING: Java 1.7.0_25 is in use which has a broken implementation of Reflection.getCallerClass().  Please consider upgrading to Java 1.7.0_40 or later.");
                java7u25CompensationOffset = 1;
            }
        } catch (Exception | LinkageError e) {
            System.out.println("WARNING: sun.reflect.Reflection.getCallerClass is not supported. This will impact performance.");
            getCallerClass = null;
            java7u25CompensationOffset = -1;
        }
        GET_CALLER_CLASS = getCallerClass;
        JDK_7u25_OFFSET = java7u25CompensationOffset;
        INSTANCE = new StackLocator();
    }

    public static StackLocator getInstance() {
        return INSTANCE;
    }

    private StackLocator() {
    }

    @PerformanceSensitive
    public Class<?> getCallerClass(int depth) {
        if (depth < 0) {
            throw new IndexOutOfBoundsException(Integer.toString(depth));
        }
        if (GET_CALLER_CLASS == null) {
            return null;
        }
        try {
            return (Class) GET_CALLER_CLASS.invoke(null, Integer.valueOf(depth + 1 + JDK_7u25_OFFSET));
        } catch (Exception e) {
            return null;
        }
    }

    @PerformanceSensitive
    public Class<?> getCallerClass(String fqcn, String pkg) {
        boolean next = false;
        int i = 2;
        while (true) {
            Class<?> clazz = getCallerClass(i);
            if (null != clazz) {
                if (fqcn.equals(clazz.getName())) {
                    next = true;
                } else if (next && clazz.getName().startsWith(pkg)) {
                    return clazz;
                }
                i++;
            } else {
                return null;
            }
        }
    }

    @PerformanceSensitive
    public Class<?> getCallerClass(Class<?> anchor) {
        boolean next = false;
        int i = 2;
        while (true) {
            Class<?> clazz = getCallerClass(i);
            if (null != clazz) {
                if (anchor.equals(clazz)) {
                    next = true;
                } else if (next) {
                    return clazz;
                }
                i++;
            } else {
                return Object.class;
            }
        }
    }

    @PerformanceSensitive
    public Stack<Class<?>> getCurrentStackTrace() {
        if (PrivateSecurityManagerStackTraceUtil.isEnabled()) {
            return PrivateSecurityManagerStackTraceUtil.getCurrentStackTrace();
        }
        Stack<Class<?>> classes = new Stack<>();
        int i = 1;
        while (true) {
            Class<?> clazz = getCallerClass(i);
            if (null != clazz) {
                classes.push(clazz);
                i++;
            } else {
                return classes;
            }
        }
    }

    public StackTraceElement calcLocation(String fqcnOfLogger) {
        if (fqcnOfLogger == null) {
            return null;
        }
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        boolean found = false;
        for (int i = 0; i < stackTrace.length; i++) {
            String className = stackTrace[i].getClassName();
            if (fqcnOfLogger.equals(className)) {
                found = true;
            } else if (found && !fqcnOfLogger.equals(className)) {
                return stackTrace[i];
            }
        }
        return null;
    }

    public StackTraceElement getStackTraceElement(int depth) {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        int i = 0;
        for (StackTraceElement element : elements) {
            if (isValid(element)) {
                if (i == depth) {
                    return element;
                }
                i++;
            }
        }
        throw new IndexOutOfBoundsException(Integer.toString(depth));
    }

    private boolean isValid(StackTraceElement element) {
        if (element.isNativeMethod()) {
            return false;
        }
        String cn = element.getClassName();
        if (cn.startsWith("sun.reflect.")) {
            return false;
        }
        String mn = element.getMethodName();
        if ((cn.startsWith("java.lang.reflect.") && (mn.equals("invoke") || mn.equals("newInstance"))) || cn.startsWith("jdk.internal.reflect.")) {
            return false;
        }
        if (cn.equals("java.lang.Class") && mn.equals("newInstance")) {
            return false;
        }
        if (cn.equals("java.lang.invoke.MethodHandle") && mn.startsWith("invoke")) {
            return false;
        }
        return true;
    }
}
