package org.springframework.beans;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/BeanMetadataElement.class */
public interface BeanMetadataElement {
    @Nullable
    default Object getSource() {
        return null;
    }
}