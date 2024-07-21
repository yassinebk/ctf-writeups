package org.apache.logging.log4j.util;

import java.util.NoSuchElementException;
import java.util.Stack;
import org.apache.logging.log4j.status.StatusLogger;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/util/StackLocatorUtil.class */
public final class StackLocatorUtil {
    private static StackLocator stackLocator;
    private static volatile boolean errorLogged = false;

    static {
        stackLocator = null;
        stackLocator = StackLocator.getInstance();
    }

    private StackLocatorUtil() {
    }

    @PerformanceSensitive
    public static Class<?> getCallerClass(int depth) {
        return stackLocator.getCallerClass(depth + 1);
    }

    public static StackTraceElement getStackTraceElement(int depth) {
        return stackLocator.getStackTraceElement(depth + 1);
    }

    @PerformanceSensitive
    public static Class<?> getCallerClass(String fqcn) {
        return getCallerClass(fqcn, "");
    }

    @PerformanceSensitive
    public static Class<?> getCallerClass(String fqcn, String pkg) {
        return stackLocator.getCallerClass(fqcn, pkg);
    }

    @PerformanceSensitive
    public static Class<?> getCallerClass(Class<?> anchor) {
        return stackLocator.getCallerClass(anchor);
    }

    @PerformanceSensitive
    public static Stack<Class<?>> getCurrentStackTrace() {
        return stackLocator.getCurrentStackTrace();
    }

    public static StackTraceElement calcLocation(String fqcnOfLogger) {
        try {
            return stackLocator.calcLocation(fqcnOfLogger);
        } catch (NoSuchElementException ex) {
            if (!errorLogged) {
                errorLogged = true;
                StatusLogger.getLogger().warn("Unable to locate stack trace element for {}", fqcnOfLogger, ex);
                return null;
            }
            return null;
        }
    }
}
