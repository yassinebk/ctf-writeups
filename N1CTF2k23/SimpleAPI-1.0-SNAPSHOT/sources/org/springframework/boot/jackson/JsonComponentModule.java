package org.springframework.boot.jackson;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jackson/JsonComponentModule.class */
public class JsonComponentModule extends SimpleModule implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void registerJsonComponents() {
        BeanFactory beanFactory = this.beanFactory;
        while (true) {
            BeanFactory beanFactory2 = beanFactory;
            if (beanFactory2 != null) {
                if (beanFactory2 instanceof ListableBeanFactory) {
                    addJsonBeans((ListableBeanFactory) beanFactory2);
                }
                beanFactory = beanFactory2 instanceof HierarchicalBeanFactory ? ((HierarchicalBeanFactory) beanFactory2).getParentBeanFactory() : null;
            } else {
                return;
            }
        }
    }

    private void addJsonBeans(ListableBeanFactory beanFactory) {
        Map<String, Object> beans = beanFactory.getBeansWithAnnotation(JsonComponent.class);
        for (Object bean : beans.values()) {
            addJsonBean(bean);
        }
    }

    private void addJsonBean(Object bean) {
        MergedAnnotation<JsonComponent> annotation = MergedAnnotations.from(bean.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(JsonComponent.class);
        Class<?>[] types = annotation.getClassArray("type");
        JsonComponent.Scope scope = (JsonComponent.Scope) annotation.getEnum("scope", JsonComponent.Scope.class);
        addJsonBean(bean, types, scope);
    }

    private void addJsonBean(Object bean, Class<?>[] types, JsonComponent.Scope scope) {
        Class<?>[] declaredClasses;
        if (bean instanceof JsonSerializer) {
            addJsonSerializerBean((JsonSerializer) bean, scope, types);
        } else if (bean instanceof JsonDeserializer) {
            addJsonDeserializerBean((JsonDeserializer) bean, types);
        } else if (bean instanceof KeyDeserializer) {
            addKeyDeserializerBean((KeyDeserializer) bean, types);
        }
        for (Class<?> innerClass : bean.getClass().getDeclaredClasses()) {
            if (isSuitableInnerClass(innerClass)) {
                Object innerInstance = BeanUtils.instantiateClass(innerClass);
                addJsonBean(innerInstance, types, scope);
            }
        }
    }

    private boolean isSuitableInnerClass(Class<?> innerClass) {
        return !Modifier.isAbstract(innerClass.getModifiers()) && (JsonSerializer.class.isAssignableFrom(innerClass) || JsonDeserializer.class.isAssignableFrom(innerClass) || KeyDeserializer.class.isAssignableFrom(innerClass));
    }

    private <T> void addJsonSerializerBean(JsonSerializer<T> serializer, JsonComponent.Scope scope, Class<?>[] types) {
        addBeanToModule(serializer, ResolvableType.forClass(JsonSerializer.class, serializer.getClass()).resolveGeneric(new int[0]), types, scope == JsonComponent.Scope.VALUES ? this::addSerializer : this::addKeySerializer);
    }

    private <T> void addJsonDeserializerBean(JsonDeserializer<T> deserializer, Class<?>[] types) {
        addBeanToModule(deserializer, ResolvableType.forClass(JsonDeserializer.class, deserializer.getClass()).resolveGeneric(new int[0]), types, this::addDeserializer);
    }

    private void addKeyDeserializerBean(KeyDeserializer deserializer, Class<?>[] types) {
        Assert.notEmpty(types, "Type must be specified for KeyDeserializer");
        addBeanToModule(deserializer, Object.class, types, this::addKeyDeserializer);
    }

    private <E, T> void addBeanToModule(E element, Class<T> baseType, Class<?>[] types, BiConsumer<Class<T>, E> consumer) {
        if (ObjectUtils.isEmpty((Object[]) types)) {
            consumer.accept(baseType, element);
            return;
        }
        for (Class<?> type : types) {
            Assert.isAssignable(baseType, type);
            consumer.accept(type, element);
        }
    }
}
