package org.springframework.cglib.transform.impl;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/transform/impl/FieldProvider.class */
public interface FieldProvider {
    String[] getFieldNames();

    Class[] getFieldTypes();

    void setField(int i, Object obj);

    Object getField(int i);

    void setField(String str, Object obj);

    Object getField(String str);
}
