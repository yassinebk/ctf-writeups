package org.springframework.remoting.httpinvoker;

import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/remoting/httpinvoker/HttpInvokerRequestExecutor.class */
public interface HttpInvokerRequestExecutor {
    RemoteInvocationResult executeRequest(HttpInvokerClientConfiguration httpInvokerClientConfiguration, RemoteInvocation remoteInvocation) throws Exception;
}
