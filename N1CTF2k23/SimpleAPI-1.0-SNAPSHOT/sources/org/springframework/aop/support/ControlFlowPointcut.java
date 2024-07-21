package org.springframework.aop.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/support/ControlFlowPointcut.class */
public class ControlFlowPointcut implements Pointcut, ClassFilter, MethodMatcher, Serializable {
    private final Class<?> clazz;
    @Nullable
    private final String methodName;
    private final AtomicInteger evaluations;

    public ControlFlowPointcut(Class<?> clazz) {
        this(clazz, null);
    }

    public ControlFlowPointcut(Class<?> clazz, @Nullable String methodName) {
        this.evaluations = new AtomicInteger(0);
        Assert.notNull(clazz, "Class must not be null");
        this.clazz = clazz;
        this.methodName = methodName;
    }

    @Override // org.springframework.aop.ClassFilter
    public boolean matches(Class<?> clazz) {
        return true;
    }

    @Override // org.springframework.aop.MethodMatcher
    public boolean matches(Method method, Class<?> targetClass) {
        return true;
    }

    @Override // org.springframework.aop.MethodMatcher
    public boolean isRuntime() {
        return true;
    }

    @Override // org.springframework.aop.MethodMatcher
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        StackTraceElement[] stackTrace;
        this.evaluations.incrementAndGet();
        for (StackTraceElement element : new Throwable().getStackTrace()) {
            if (element.getClassName().equals(this.clazz.getName()) && (this.methodName == null || element.getMethodName().equals(this.methodName))) {
                return true;
            }
        }
        return false;
    }

    public int getEvaluations() {
        return this.evaluations.get();
    }

    @Override // org.springframework.aop.Pointcut
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override // org.springframework.aop.Pointcut
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ControlFlowPointcut)) {
            return false;
        }
        ControlFlowPointcut that = (ControlFlowPointcut) other;
        return this.clazz.equals(that.clazz) && ObjectUtils.nullSafeEquals(this.methodName, that.methodName);
    }

    public int hashCode() {
        int code = this.clazz.hashCode();
        if (this.methodName != null) {
            code = (37 * code) + this.methodName.hashCode();
        }
        return code;
    }

    public String toString() {
        return getClass().getName() + ": class = " + this.clazz.getName() + "; methodName = " + this.methodName;
    }
}
