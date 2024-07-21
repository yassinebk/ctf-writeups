package org.springframework.beans.factory.wiring;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/wiring/BeanWiringInfoResolver.class */
public interface BeanWiringInfoResolver {
    @Nullable
    BeanWiringInfo resolveWiringInfo(Object obj);
}
