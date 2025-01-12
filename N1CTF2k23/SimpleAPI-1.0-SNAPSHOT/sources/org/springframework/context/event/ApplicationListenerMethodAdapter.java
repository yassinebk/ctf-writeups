package org.springframework.context.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/event/ApplicationListenerMethodAdapter.class */
public class ApplicationListenerMethodAdapter implements GenericApplicationListener {
    private static final boolean reactiveStreamsPresent = ClassUtils.isPresent("org.reactivestreams.Publisher", ApplicationListenerMethodAdapter.class.getClassLoader());
    protected final Log logger = LogFactory.getLog(getClass());
    private final String beanName;
    private final Method method;
    private final Method targetMethod;
    private final AnnotatedElementKey methodKey;
    private final List<ResolvableType> declaredEventTypes;
    @Nullable
    private final String condition;
    private final int order;
    @Nullable
    private ApplicationContext applicationContext;
    @Nullable
    private EventExpressionEvaluator evaluator;

    public ApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method) {
        this.beanName = beanName;
        this.method = BridgeMethodResolver.findBridgedMethod(method);
        this.targetMethod = !Proxy.isProxyClass(targetClass) ? AopUtils.getMostSpecificMethod(method, targetClass) : this.method;
        this.methodKey = new AnnotatedElementKey(this.targetMethod, targetClass);
        EventListener ann = (EventListener) AnnotatedElementUtils.findMergedAnnotation(this.targetMethod, EventListener.class);
        this.declaredEventTypes = resolveDeclaredEventTypes(method, ann);
        this.condition = ann != null ? ann.condition() : null;
        this.order = resolveOrder(this.targetMethod);
    }

    private static List<ResolvableType> resolveDeclaredEventTypes(Method method, @Nullable EventListener ann) {
        int count = method.getParameterCount();
        if (count > 1) {
            throw new IllegalStateException("Maximum one parameter is allowed for event listener method: " + method);
        }
        if (ann != null) {
            Class<?>[] classes = ann.classes();
            if (classes.length > 0) {
                List<ResolvableType> types = new ArrayList<>(classes.length);
                for (Class<?> eventType : classes) {
                    types.add(ResolvableType.forClass(eventType));
                }
                return types;
            }
        }
        if (count == 0) {
            throw new IllegalStateException("Event parameter is mandatory for event listener method: " + method);
        }
        return Collections.singletonList(ResolvableType.forMethodParameter(method, 0));
    }

    private static int resolveOrder(Method method) {
        Order ann = (Order) AnnotatedElementUtils.findMergedAnnotation(method, Order.class);
        if (ann != null) {
            return ann.value();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(ApplicationContext applicationContext, EventExpressionEvaluator evaluator) {
        this.applicationContext = applicationContext;
        this.evaluator = evaluator;
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ApplicationEvent event) {
        processEvent(event);
    }

    @Override // org.springframework.context.event.GenericApplicationListener
    public boolean supportsEventType(ResolvableType eventType) {
        for (ResolvableType declaredEventType : this.declaredEventTypes) {
            if (declaredEventType.isAssignableFrom(eventType)) {
                return true;
            }
            if (PayloadApplicationEvent.class.isAssignableFrom(eventType.toClass())) {
                ResolvableType payloadType = eventType.as(PayloadApplicationEvent.class).getGeneric(new int[0]);
                if (declaredEventType.isAssignableFrom(payloadType)) {
                    return true;
                }
            }
        }
        return eventType.hasUnresolvableGenerics();
    }

    @Override // org.springframework.context.event.GenericApplicationListener
    public boolean supportsSourceType(@Nullable Class<?> sourceType) {
        return true;
    }

    @Override // org.springframework.context.event.GenericApplicationListener, org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void processEvent(ApplicationEvent event) {
        Object[] args = resolveArguments(event);
        if (shouldHandle(event, args)) {
            Object result = doInvoke(args);
            if (result != null) {
                handleResult(result);
            } else {
                this.logger.trace("No result object given - no result to handle");
            }
        }
    }

    @Nullable
    protected Object[] resolveArguments(ApplicationEvent event) {
        ResolvableType declaredEventType = getResolvableType(event);
        if (declaredEventType == null) {
            return null;
        }
        if (this.method.getParameterCount() == 0) {
            return new Object[0];
        }
        Class<?> declaredEventClass = declaredEventType.toClass();
        if (!ApplicationEvent.class.isAssignableFrom(declaredEventClass) && (event instanceof PayloadApplicationEvent)) {
            Object payload = ((PayloadApplicationEvent) event).getPayload();
            if (declaredEventClass.isInstance(payload)) {
                return new Object[]{payload};
            }
        }
        return new Object[]{event};
    }

    protected void handleResult(Object result) {
        if (reactiveStreamsPresent && new ReactiveResultHandler().subscribeToPublisher(result)) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Adapted to reactive result: " + result);
            }
        } else if (result instanceof CompletionStage) {
            ((CompletionStage) result).whenComplete(event, ex -> {
                if (ex != null) {
                    handleAsyncError(ex);
                } else if (event != null) {
                    publishEvent(event);
                }
            });
        } else if (result instanceof ListenableFuture) {
            ((ListenableFuture) result).addCallback(this::publishEvents, this::handleAsyncError);
        } else {
            publishEvents(result);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void publishEvents(Object result) {
        if (result.getClass().isArray()) {
            Object[] events = ObjectUtils.toObjectArray(result);
            for (Object event : events) {
                publishEvent(event);
            }
        } else if (result instanceof Collection) {
            for (Object event2 : (Collection) result) {
                publishEvent(event2);
            }
        } else {
            publishEvent(result);
        }
    }

    private void publishEvent(@Nullable Object event) {
        if (event != null) {
            Assert.notNull(this.applicationContext, "ApplicationContext must not be null");
            this.applicationContext.publishEvent(event);
        }
    }

    protected void handleAsyncError(Throwable t) {
        this.logger.error("Unexpected error occurred in asynchronous listener", t);
    }

    private boolean shouldHandle(ApplicationEvent event, @Nullable Object[] args) {
        if (args == null) {
            return false;
        }
        String condition = getCondition();
        if (StringUtils.hasText(condition)) {
            Assert.notNull(this.evaluator, "EventExpressionEvaluator must not be null");
            return this.evaluator.condition(condition, event, this.targetMethod, this.methodKey, args, this.applicationContext);
        }
        return true;
    }

    @Nullable
    protected Object doInvoke(Object... args) {
        Object bean = getTargetBean();
        if (bean.equals(null)) {
            return null;
        }
        ReflectionUtils.makeAccessible(this.method);
        try {
            return this.method.invoke(bean, args);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(getInvocationErrorMessage(bean, ex.getMessage(), args), ex);
        } catch (IllegalArgumentException ex2) {
            assertTargetBean(this.method, bean, args);
            throw new IllegalStateException(getInvocationErrorMessage(bean, ex2.getMessage(), args), ex2);
        } catch (InvocationTargetException ex3) {
            Throwable targetException = ex3.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            String msg = getInvocationErrorMessage(bean, "Failed to invoke event listener method", args);
            throw new UndeclaredThrowableException(targetException, msg);
        }
    }

    protected Object getTargetBean() {
        Assert.notNull(this.applicationContext, "ApplicationContext must no be null");
        return this.applicationContext.getBean(this.beanName);
    }

    @Nullable
    protected String getCondition() {
        return this.condition;
    }

    protected String getDetailedErrorMessage(Object bean, String message) {
        StringBuilder sb = new StringBuilder(message).append("\n");
        sb.append("HandlerMethod details: \n");
        sb.append("Bean [").append(bean.getClass().getName()).append("]\n");
        sb.append("Method [").append(this.method.toGenericString()).append("]\n");
        return sb.toString();
    }

    private void assertTargetBean(Method method, Object targetBean, Object[] args) {
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        Class<?> targetBeanClass = targetBean.getClass();
        if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
            String msg = "The event listener method class '" + methodDeclaringClass.getName() + "' is not an instance of the actual bean class '" + targetBeanClass.getName() + "'. If the bean requires proxying (e.g. due to @Transactional), please use class-based proxying.";
            throw new IllegalStateException(getInvocationErrorMessage(targetBean, msg, args));
        }
    }

    private String getInvocationErrorMessage(Object bean, String message, Object[] resolvedArgs) {
        StringBuilder sb = new StringBuilder(getDetailedErrorMessage(bean, message));
        sb.append("Resolved arguments: \n");
        for (int i = 0; i < resolvedArgs.length; i++) {
            sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(i).append("] ");
            if (resolvedArgs[i] == null) {
                sb.append("[null] \n");
            } else {
                sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
                sb.append("[value=").append(resolvedArgs[i]).append("]\n");
            }
        }
        return sb.toString();
    }

    @Nullable
    private ResolvableType getResolvableType(ApplicationEvent event) {
        ResolvableType payloadType = null;
        if (event instanceof PayloadApplicationEvent) {
            PayloadApplicationEvent<?> payloadEvent = (PayloadApplicationEvent) event;
            ResolvableType eventType = payloadEvent.getResolvableType();
            if (eventType != null) {
                payloadType = eventType.as(PayloadApplicationEvent.class).getGeneric(new int[0]);
            }
        }
        for (ResolvableType declaredEventType : this.declaredEventTypes) {
            Class<?> eventClass = declaredEventType.toClass();
            if (!ApplicationEvent.class.isAssignableFrom(eventClass) && payloadType != null && declaredEventType.isAssignableFrom(payloadType)) {
                return declaredEventType;
            }
            if (eventClass.isInstance(event)) {
                return declaredEventType;
            }
        }
        return null;
    }

    public String toString() {
        return this.method.toGenericString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/event/ApplicationListenerMethodAdapter$ReactiveResultHandler.class */
    public class ReactiveResultHandler {
        private ReactiveResultHandler() {
        }

        public boolean subscribeToPublisher(Object result) {
            ReactiveAdapter adapter = ReactiveAdapterRegistry.getSharedInstance().getAdapter(result.getClass());
            if (adapter != null) {
                adapter.toPublisher(result).subscribe(new EventPublicationSubscriber());
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/event/ApplicationListenerMethodAdapter$EventPublicationSubscriber.class */
    public class EventPublicationSubscriber implements Subscriber<Object> {
        private EventPublicationSubscriber() {
        }

        public void onSubscribe(Subscription s) {
            s.request(2147483647L);
        }

        public void onNext(Object o) {
            ApplicationListenerMethodAdapter.this.publishEvents(o);
        }

        public void onError(Throwable t) {
            ApplicationListenerMethodAdapter.this.handleAsyncError(t);
        }

        public void onComplete() {
        }
    }
}
