package org.springframework.beans;

import java.beans.PropertyDescriptor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/BeanWrapper.class */
public interface BeanWrapper extends ConfigurablePropertyAccessor {
    void setAutoGrowCollectionLimit(int i);

    int getAutoGrowCollectionLimit();

    Object getWrappedInstance();

    Class<?> getWrappedClass();

    PropertyDescriptor[] getPropertyDescriptors();

    PropertyDescriptor getPropertyDescriptor(String str) throws InvalidPropertyException;
}
