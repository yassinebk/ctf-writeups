package org.springframework.beans.factory.xml;

import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/xml/NamespaceHandlerResolver.class */
public interface NamespaceHandlerResolver {
    @Nullable
    NamespaceHandler resolve(String str);
}
