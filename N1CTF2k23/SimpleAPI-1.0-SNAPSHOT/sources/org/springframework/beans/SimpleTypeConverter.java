package org.springframework.beans;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/SimpleTypeConverter.class */
public class SimpleTypeConverter extends TypeConverterSupport {
    public SimpleTypeConverter() {
        this.typeConverterDelegate = new TypeConverterDelegate(this);
        registerDefaultEditors();
    }
}
