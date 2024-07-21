package org.springframework.beans.factory.parsing;

import org.springframework.beans.factory.parsing.ParseState;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/parsing/BeanEntry.class */
public class BeanEntry implements ParseState.Entry {
    private String beanDefinitionName;

    public BeanEntry(String beanDefinitionName) {
        this.beanDefinitionName = beanDefinitionName;
    }

    public String toString() {
        return "Bean '" + this.beanDefinitionName + "'";
    }
}
