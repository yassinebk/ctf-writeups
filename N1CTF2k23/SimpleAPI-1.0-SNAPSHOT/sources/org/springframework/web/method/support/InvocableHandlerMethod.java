package org.springframework.web.method.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/method/support/InvocableHandlerMethod.class */
public class InvocableHandlerMethod extends HandlerMethod {
    private static final Object[] EMPTY_ARGS = new Object[0];
    @Nullable
    private WebDataBinderFactory dataBinderFactory;
    private HandlerMethodArgumentResolverComposite resolvers;
    private ParameterNameDiscoverer parameterNameDiscoverer;

    public InvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
        this.resolvers = new HandlerMethodArgumentResolverComposite();
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    public InvocableHandlerMethod(Object bean, Method method) {
        super(bean, method);
        this.resolvers = new HandlerMethodArgumentResolverComposite();
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    public InvocableHandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        super(bean, methodName, parameterTypes);
        this.resolvers = new HandlerMethodArgumentResolverComposite();
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory) {
        this.dataBinderFactory = dataBinderFactory;
    }

    public void setHandlerMethodArgumentResolvers(HandlerMethodArgumentResolverComposite argumentResolvers) {
        this.resolvers = argumentResolvers;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    @Nullable
    public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
        Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Arguments: " + Arrays.toString(args));
        }
        return doInvoke(args);
    }

    protected Object[] getMethodArgumentValues(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
        String exMsg;
        MethodParameter[] parameters = getMethodParameters();
        if (ObjectUtils.isEmpty((Object[]) parameters)) {
            return EMPTY_ARGS;
        }
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];
            parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            args[i] = findProvidedArgument(parameter, providedArgs);
            if (args[i] == null) {
                if (!this.resolvers.supportsParameter(parameter)) {
                    throw new IllegalStateException(formatArgumentError(parameter, "No suitable resolver"));
                }
                try {
                    args[i] = this.resolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
                } catch (Exception ex) {
                    if (this.logger.isDebugEnabled() && (exMsg = ex.getMessage()) != null && !exMsg.contains(parameter.getExecutable().toGenericString())) {
                        this.logger.debug(formatArgumentError(parameter, exMsg));
                    }
                    throw ex;
                }
            }
        }
        return args;
    }

    @Nullable
    protected Object doInvoke(Object... args) throws Exception {
        ReflectionUtils.makeAccessible(getBridgedMethod());
        try {
            return getBridgedMethod().invoke(getBean(), args);
        } catch (IllegalArgumentException ex) {
            assertTargetBean(getBridgedMethod(), getBean(), args);
            String text = ex.getMessage() != null ? ex.getMessage() : "Illegal argument";
            throw new IllegalStateException(formatInvokeError(text, args), ex);
        } catch (InvocationTargetException ex2) {
            Throwable targetException = ex2.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            if (targetException instanceof Error) {
                throw ((Error) targetException);
            }
            if (targetException instanceof Exception) {
                throw ((Exception) targetException);
            }
            throw new IllegalStateException(formatInvokeError("Invocation failure", args), targetException);
        }
    }
}
