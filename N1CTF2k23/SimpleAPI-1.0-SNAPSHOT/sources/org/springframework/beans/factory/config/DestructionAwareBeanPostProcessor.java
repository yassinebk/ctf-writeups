package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/config/DestructionAwareBeanPostProcessor.class */
public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {
    void postProcessBeforeDestruction(Object obj, String str) throws BeansException;

    default boolean requiresDestruction(Object bean) {
        return true;
    }
}
