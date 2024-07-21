package org.springframework.context.annotation;

import java.lang.reflect.Constructor;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/ParserStrategyUtils.class */
abstract class ParserStrategyUtils {
    ParserStrategyUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T instantiateClass(Class<?> clazz, Class<T> assignableTo, Environment environment, ResourceLoader resourceLoader, BeanDefinitionRegistry registry) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.isAssignable(assignableTo, clazz);
        if (clazz.isInterface()) {
            throw new BeanInstantiationException(clazz, "Specified class is an interface");
        }
        ClassLoader classLoader = registry instanceof ConfigurableBeanFactory ? ((ConfigurableBeanFactory) registry).getBeanClassLoader() : resourceLoader.getClassLoader();
        T instance = (T) createInstance(clazz, environment, resourceLoader, registry, classLoader);
        invokeAwareMethods(instance, environment, resourceLoader, registry, classLoader);
        return instance;
    }

    private static Object createInstance(Class<?> clazz, Environment environment, ResourceLoader resourceLoader, BeanDefinitionRegistry registry, @Nullable ClassLoader classLoader) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 1 && constructors[0].getParameterCount() > 0) {
            try {
                Constructor<?> constructor = constructors[0];
                Object[] args = resolveArgs(constructor.getParameterTypes(), environment, resourceLoader, registry, classLoader);
                return BeanUtils.instantiateClass(constructor, args);
            } catch (Exception ex) {
                throw new BeanInstantiationException(clazz, "No suitable constructor found", ex);
            }
        }
        return BeanUtils.instantiateClass(clazz);
    }

    private static Object[] resolveArgs(Class<?>[] parameterTypes, Environment environment, ResourceLoader resourceLoader, BeanDefinitionRegistry registry, @Nullable ClassLoader classLoader) {
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = resolveParameter(parameterTypes[i], environment, resourceLoader, registry, classLoader);
        }
        return parameters;
    }

    @Nullable
    private static Object resolveParameter(Class<?> parameterType, Environment environment, ResourceLoader resourceLoader, BeanDefinitionRegistry registry, @Nullable ClassLoader classLoader) {
        if (parameterType == Environment.class) {
            return environment;
        }
        if (parameterType == ResourceLoader.class) {
            return resourceLoader;
        }
        if (parameterType == BeanFactory.class) {
            if (registry instanceof BeanFactory) {
                return registry;
            }
            return null;
        } else if (parameterType == ClassLoader.class) {
            return classLoader;
        } else {
            throw new IllegalStateException("Illegal method parameter type: " + parameterType.getName());
        }
    }

    private static void invokeAwareMethods(Object parserStrategyBean, Environment environment, ResourceLoader resourceLoader, BeanDefinitionRegistry registry, @Nullable ClassLoader classLoader) {
        if (parserStrategyBean instanceof Aware) {
            if ((parserStrategyBean instanceof BeanClassLoaderAware) && classLoader != null) {
                ((BeanClassLoaderAware) parserStrategyBean).setBeanClassLoader(classLoader);
            }
            if ((parserStrategyBean instanceof BeanFactoryAware) && (registry instanceof BeanFactory)) {
                ((BeanFactoryAware) parserStrategyBean).setBeanFactory((BeanFactory) registry);
            }
            if (parserStrategyBean instanceof EnvironmentAware) {
                ((EnvironmentAware) parserStrategyBean).setEnvironment(environment);
            }
            if (parserStrategyBean instanceof ResourceLoaderAware) {
                ((ResourceLoaderAware) parserStrategyBean).setResourceLoader(resourceLoader);
            }
        }
    }
}
