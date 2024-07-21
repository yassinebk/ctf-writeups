package org.springframework.beans;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/PropertyAccessorFactory.class */
public final class PropertyAccessorFactory {
    private PropertyAccessorFactory() {
    }

    public static BeanWrapper forBeanPropertyAccess(Object target) {
        return new BeanWrapperImpl(target);
    }

    public static ConfigurablePropertyAccessor forDirectFieldAccess(Object target) {
        return new DirectFieldAccessor(target);
    }
}
