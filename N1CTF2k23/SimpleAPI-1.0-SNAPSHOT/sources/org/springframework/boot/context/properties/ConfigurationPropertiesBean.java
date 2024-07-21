package org.springframework.boot.context.properties;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBean.class */
public final class ConfigurationPropertiesBean {
    private final String name;
    private final Object instance;
    private final ConfigurationProperties annotation;
    private final Bindable<?> bindTarget;
    private final BindMethod bindMethod;

    private ConfigurationPropertiesBean(String name, Object instance, ConfigurationProperties annotation, Bindable<?> bindTarget) {
        this.name = name;
        this.instance = instance;
        this.annotation = annotation;
        this.bindTarget = bindTarget;
        this.bindMethod = BindMethod.forType(bindTarget.getType().resolve());
    }

    public String getName() {
        return this.name;
    }

    public Object getInstance() {
        return this.instance;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Class<?> getType() {
        return this.bindTarget.getType().resolve();
    }

    public BindMethod getBindMethod() {
        return this.bindMethod;
    }

    public ConfigurationProperties getAnnotation() {
        return this.annotation;
    }

    public Bindable<?> asBindTarget() {
        return this.bindTarget;
    }

    public static Map<String, ConfigurationPropertiesBean> getAll(ApplicationContext applicationContext) {
        Assert.notNull(applicationContext, "ApplicationContext must not be null");
        if (applicationContext instanceof ConfigurableApplicationContext) {
            return getAll((ConfigurableApplicationContext) applicationContext);
        }
        Map<String, ConfigurationPropertiesBean> propertiesBeans = new LinkedHashMap<>();
        applicationContext.getBeansWithAnnotation(ConfigurationProperties.class).forEach(beanName, bean -> {
            ConfigurationPropertiesBean configurationPropertiesBean = (ConfigurationPropertiesBean) propertiesBeans.put(beanName, get(applicationContext, bean, beanName));
        });
        return propertiesBeans;
    }

    private static Map<String, ConfigurationPropertiesBean> getAll(ConfigurableApplicationContext applicationContext) {
        Map<String, ConfigurationPropertiesBean> propertiesBeans = new LinkedHashMap<>();
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        Iterator<String> beanNames = beanFactory.getBeanNamesIterator();
        while (beanNames.hasNext()) {
            String beanName = beanNames.next();
            if (isConfigurationPropertiesBean(beanFactory, beanName)) {
                try {
                    Object bean = beanFactory.getBean(beanName);
                    ConfigurationPropertiesBean propertiesBean = get(applicationContext, bean, beanName);
                    propertiesBeans.put(beanName, propertiesBean);
                } catch (Exception e) {
                }
            }
        }
        return propertiesBeans;
    }

    private static boolean isConfigurationPropertiesBean(ConfigurableListableBeanFactory beanFactory, String beanName) {
        try {
            if (beanFactory.getBeanDefinition(beanName).isAbstract()) {
                return false;
            }
            if (beanFactory.findAnnotationOnBean(beanName, ConfigurationProperties.class) != null) {
                return true;
            }
            Method factoryMethod = findFactoryMethod(beanFactory, beanName);
            return findMergedAnnotation(factoryMethod, ConfigurationProperties.class).isPresent();
        } catch (NoSuchBeanDefinitionException e) {
            return false;
        }
    }

    public static ConfigurationPropertiesBean get(ApplicationContext applicationContext, Object bean, String beanName) {
        Method factoryMethod = findFactoryMethod(applicationContext, beanName);
        return create(beanName, bean, bean.getClass(), factoryMethod);
    }

    private static Method findFactoryMethod(ApplicationContext applicationContext, String beanName) {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            return findFactoryMethod((ConfigurableApplicationContext) applicationContext, beanName);
        }
        return null;
    }

    private static Method findFactoryMethod(ConfigurableApplicationContext applicationContext, String beanName) {
        return findFactoryMethod(applicationContext.getBeanFactory(), beanName);
    }

    private static Method findFactoryMethod(ConfigurableListableBeanFactory beanFactory, String beanName) {
        Method resolvedFactoryMethod;
        if (beanFactory.containsBeanDefinition(beanName)) {
            BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
            if ((beanDefinition instanceof RootBeanDefinition) && (resolvedFactoryMethod = ((RootBeanDefinition) beanDefinition).getResolvedFactoryMethod()) != null) {
                return resolvedFactoryMethod;
            }
            return findFactoryMethodUsingReflection(beanFactory, beanDefinition);
        }
        return null;
    }

    private static Method findFactoryMethodUsingReflection(ConfigurableListableBeanFactory beanFactory, BeanDefinition beanDefinition) {
        String factoryMethodName = beanDefinition.getFactoryMethodName();
        String factoryBeanName = beanDefinition.getFactoryBeanName();
        if (factoryMethodName == null || factoryBeanName == null) {
            return null;
        }
        Class<?> factoryType = beanFactory.getType(factoryBeanName);
        if (factoryType.getName().contains(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
            factoryType = factoryType.getSuperclass();
        }
        AtomicReference<Method> factoryMethod = new AtomicReference<>();
        ReflectionUtils.doWithMethods(factoryType, method -> {
            if (method.getName().equals(factoryMethodName)) {
                factoryMethod.set(method);
            }
        });
        return factoryMethod.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ConfigurationPropertiesBean forValueObject(Class<?> beanClass, String beanName) {
        ConfigurationPropertiesBean propertiesBean = create(beanName, null, beanClass, null);
        Assert.state(propertiesBean != null && propertiesBean.getBindMethod() == BindMethod.VALUE_OBJECT, "Bean '" + beanName + "' is not a @ConfigurationProperties value object");
        return propertiesBean;
    }

    private static ConfigurationPropertiesBean create(String name, Object instance, Class<?> type, Method factory) {
        ConfigurationProperties annotation = (ConfigurationProperties) findAnnotation(instance, type, factory, ConfigurationProperties.class);
        if (annotation == null) {
            return null;
        }
        Validated validated = (Validated) findAnnotation(instance, type, factory, Validated.class);
        Annotation[] annotations = validated != null ? new Annotation[]{annotation, validated} : new Annotation[]{annotation};
        ResolvableType bindType = factory != null ? ResolvableType.forMethodReturnType(factory) : ResolvableType.forClass(type);
        Bindable<Object> bindTarget = Bindable.of(bindType).withAnnotations(annotations);
        if (instance != null) {
            bindTarget = bindTarget.withExistingValue(instance);
        }
        return new ConfigurationPropertiesBean(name, instance, annotation, bindTarget);
    }

    private static <A extends Annotation> A findAnnotation(Object instance, Class<?> type, Method factory, Class<A> annotationType) {
        MergedAnnotation<A> annotation = MergedAnnotation.missing();
        if (factory != null) {
            annotation = findMergedAnnotation(factory, annotationType);
        }
        if (!annotation.isPresent()) {
            annotation = findMergedAnnotation(type, annotationType);
        }
        if (!annotation.isPresent() && AopUtils.isAopProxy(instance)) {
            annotation = MergedAnnotations.from(AopUtils.getTargetClass(instance), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(annotationType);
        }
        if (annotation.isPresent()) {
            return annotation.synthesize();
        }
        return null;
    }

    private static <A extends Annotation> MergedAnnotation<A> findMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
        return element != null ? MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(annotationType) : MergedAnnotation.missing();
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBean$BindMethod.class */
    public enum BindMethod {
        JAVA_BEAN,
        VALUE_OBJECT;

        /* JADX INFO: Access modifiers changed from: package-private */
        public static BindMethod forType(Class<?> type) {
            return ConfigurationPropertiesBindConstructorProvider.INSTANCE.getBindConstructor(type, false) != null ? VALUE_OBJECT : JAVA_BEAN;
        }
    }
}
