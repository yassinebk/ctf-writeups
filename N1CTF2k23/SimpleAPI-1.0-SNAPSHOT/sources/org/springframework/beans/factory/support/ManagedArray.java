package org.springframework.beans.factory.support;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/ManagedArray.class */
public class ManagedArray extends ManagedList<Object> {
    @Nullable
    volatile Class<?> resolvedElementType;

    public ManagedArray(String elementTypeName, int size) {
        super(size);
        Assert.notNull(elementTypeName, "elementTypeName must not be null");
        setElementTypeName(elementTypeName);
    }
}
