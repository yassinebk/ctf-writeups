package org.springframework.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/interceptor/SimpleTraceInterceptor.class */
public class SimpleTraceInterceptor extends AbstractTraceInterceptor {
    public SimpleTraceInterceptor() {
    }

    public SimpleTraceInterceptor(boolean useDynamicLogger) {
        setUseDynamicLogger(useDynamicLogger);
    }

    @Override // org.springframework.aop.interceptor.AbstractTraceInterceptor
    protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
        String invocationDescription = getInvocationDescription(invocation);
        writeToLog(logger, "Entering " + invocationDescription);
        try {
            Object rval = invocation.proceed();
            writeToLog(logger, "Exiting " + invocationDescription);
            return rval;
        } catch (Throwable ex) {
            writeToLog(logger, "Exception thrown in " + invocationDescription, ex);
            throw ex;
        }
    }

    protected String getInvocationDescription(MethodInvocation invocation) {
        String className = invocation.getThis().getClass().getName();
        return "method '" + invocation.getMethod().getName() + "' of class [" + className + "]";
    }
}
