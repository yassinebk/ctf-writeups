package org.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/remoting/support/RemoteInvocationFactory.class */
public interface RemoteInvocationFactory {
    RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation);
}
