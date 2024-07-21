package org.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/remoting/support/RemoteInvocationExecutor.class */
public interface RemoteInvocationExecutor {
    Object invoke(RemoteInvocation remoteInvocation, Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
