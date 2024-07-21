package org.springframework.beans.factory.support;

import java.security.AccessControlContext;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/SecurityContextProvider.class */
public interface SecurityContextProvider {
    AccessControlContext getAccessControlContext();
}
