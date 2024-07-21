package org.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/remoting/support/RemoteInvocationBasedExporter.class */
public abstract class RemoteInvocationBasedExporter extends RemoteExporter {
    private RemoteInvocationExecutor remoteInvocationExecutor = new DefaultRemoteInvocationExecutor();

    public void setRemoteInvocationExecutor(RemoteInvocationExecutor remoteInvocationExecutor) {
        this.remoteInvocationExecutor = remoteInvocationExecutor;
    }

    public RemoteInvocationExecutor getRemoteInvocationExecutor() {
        return this.remoteInvocationExecutor;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object invoke(RemoteInvocation invocation, Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Executing " + invocation);
        }
        try {
            return getRemoteInvocationExecutor().invoke(invocation, targetObject);
        } catch (IllegalAccessException ex) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Could not access target method for " + invocation, ex);
            }
            throw ex;
        } catch (NoSuchMethodException ex2) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Could not find target method for " + invocation, ex2);
            }
            throw ex2;
        } catch (InvocationTargetException ex3) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Target method failed for " + invocation, ex3.getTargetException());
            }
            throw ex3;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RemoteInvocationResult invokeAndCreateResult(RemoteInvocation invocation, Object targetObject) {
        try {
            Object value = invoke(invocation, targetObject);
            return new RemoteInvocationResult(value);
        } catch (Throwable ex) {
            return new RemoteInvocationResult(ex);
        }
    }
}
