package org.springframework.core;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/AttributeAccessor.class */
public interface AttributeAccessor {
    void setAttribute(String str, @Nullable Object obj);

    @Nullable
    Object getAttribute(String str);

    @Nullable
    Object removeAttribute(String str);

    boolean hasAttribute(String str);

    String[] attributeNames();
}
