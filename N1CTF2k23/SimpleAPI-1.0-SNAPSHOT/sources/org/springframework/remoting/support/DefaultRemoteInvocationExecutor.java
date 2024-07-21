package org.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/remoting/support/DefaultRemoteInvocationExecutor.class */
public class DefaultRemoteInvocationExecutor implements RemoteInvocationExecutor {
    @Override // org.springframework.remoting.support.RemoteInvocationExecutor
    public Object invoke(RemoteInvocation invocation, Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Assert.notNull(invocation, "RemoteInvocation must not be null");
        Assert.notNull(targetObject, "Target object must not be null");
        return invocation.invoke(targetObject);
    }
}
