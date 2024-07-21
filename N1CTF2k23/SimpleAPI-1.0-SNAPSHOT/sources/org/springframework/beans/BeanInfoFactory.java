package org.springframework.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/BeanInfoFactory.class */
public interface BeanInfoFactory {
    @Nullable
    BeanInfo getBeanInfo(Class<?> cls) throws IntrospectionException;
}
