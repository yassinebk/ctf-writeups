package org.springframework.beans.factory.support;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/ReplaceOverride.class */
public class ReplaceOverride extends MethodOverride {
    private final String methodReplacerBeanName;
    private List<String> typeIdentifiers;

    public ReplaceOverride(String methodName, String methodReplacerBeanName) {
        super(methodName);
        this.typeIdentifiers = new LinkedList();
        Assert.notNull(methodName, "Method replacer bean name must not be null");
        this.methodReplacerBeanName = methodReplacerBeanName;
    }

    public String getMethodReplacerBeanName() {
        return this.methodReplacerBeanName;
    }

    public void addTypeIdentifier(String identifier) {
        this.typeIdentifiers.add(identifier);
    }

    @Override // org.springframework.beans.factory.support.MethodOverride
    public boolean matches(Method method) {
        if (!method.getName().equals(getMethodName())) {
            return false;
        }
        if (!isOverloaded()) {
            return true;
        }
        if (this.typeIdentifiers.size() != method.getParameterCount()) {
            return false;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < this.typeIdentifiers.size(); i++) {
            String identifier = this.typeIdentifiers.get(i);
            if (!parameterTypes[i].getName().contains(identifier)) {
                return false;
            }
        }
        return true;
    }

    @Override // org.springframework.beans.factory.support.MethodOverride
    public boolean equals(@Nullable Object other) {
        if (!(other instanceof ReplaceOverride) || !super.equals(other)) {
            return false;
        }
        ReplaceOverride that = (ReplaceOverride) other;
        return ObjectUtils.nullSafeEquals(this.methodReplacerBeanName, that.methodReplacerBeanName) && ObjectUtils.nullSafeEquals(this.typeIdentifiers, that.typeIdentifiers);
    }

    @Override // org.springframework.beans.factory.support.MethodOverride
    public int hashCode() {
        int hashCode = super.hashCode();
        return (29 * ((29 * hashCode) + ObjectUtils.nullSafeHashCode(this.methodReplacerBeanName))) + ObjectUtils.nullSafeHashCode(this.typeIdentifiers);
    }

    public String toString() {
        return "Replace override for method '" + getMethodName() + "'";
    }
}
