package org.springframework.jmx.export.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotationPredicates;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;
import org.springframework.jmx.export.metadata.InvalidMetadataException;
import org.springframework.jmx.export.metadata.JmxAttributeSource;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/annotation/AnnotationJmxAttributeSource.class */
public class AnnotationJmxAttributeSource implements JmxAttributeSource, BeanFactoryAware {
    @Nullable
    private StringValueResolver embeddedValueResolver;

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        if (beanFactory instanceof ConfigurableBeanFactory) {
            this.embeddedValueResolver = new EmbeddedValueResolver((ConfigurableBeanFactory) beanFactory);
        }
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    @Nullable
    public org.springframework.jmx.export.metadata.ManagedResource getManagedResource(Class<?> beanClass) throws InvalidMetadataException {
        MergedAnnotation<ManagedResource> ann = MergedAnnotations.from(beanClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ManagedResource.class).withNonMergedAttributes();
        if (!ann.isPresent()) {
            return null;
        }
        Class<?> declaringClass = (Class) ann.getSource();
        Class<?> target = (declaringClass == null || declaringClass.isInterface()) ? beanClass : declaringClass;
        if (!Modifier.isPublic(target.getModifiers())) {
            throw new InvalidMetadataException("@ManagedResource class '" + target.getName() + "' must be public");
        }
        org.springframework.jmx.export.metadata.ManagedResource bean = new org.springframework.jmx.export.metadata.ManagedResource();
        Map<String, Object> map = ann.asMap(new MergedAnnotation.Adapt[0]);
        List<PropertyValue> list = new ArrayList<>(map.size());
        map.forEach(attrName, attrValue -> {
            if (!"value".equals(attrName)) {
                Object value = attrValue;
                if (this.embeddedValueResolver != null && (value instanceof String)) {
                    value = this.embeddedValueResolver.resolveStringValue((String) value);
                }
                list.add(new PropertyValue(attrName, value));
            }
        });
        PropertyAccessorFactory.forBeanPropertyAccess(bean).setPropertyValues(new MutablePropertyValues(list));
        return bean;
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    @Nullable
    public org.springframework.jmx.export.metadata.ManagedAttribute getManagedAttribute(Method method) throws InvalidMetadataException {
        MergedAnnotation<ManagedAttribute> ann = MergedAnnotations.from(method, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ManagedAttribute.class).withNonMergedAttributes();
        if (!ann.isPresent()) {
            return null;
        }
        org.springframework.jmx.export.metadata.ManagedAttribute bean = new org.springframework.jmx.export.metadata.ManagedAttribute();
        Map<String, Object> map = ann.asMap(new MergedAnnotation.Adapt[0]);
        MutablePropertyValues pvs = new MutablePropertyValues(map);
        pvs.removePropertyValue("defaultValue");
        PropertyAccessorFactory.forBeanPropertyAccess(bean).setPropertyValues(pvs);
        String defaultValue = (String) map.get("defaultValue");
        if (defaultValue.length() > 0) {
            bean.setDefaultValue(defaultValue);
        }
        return bean;
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    @Nullable
    public org.springframework.jmx.export.metadata.ManagedMetric getManagedMetric(Method method) throws InvalidMetadataException {
        MergedAnnotation<ManagedMetric> ann = MergedAnnotations.from(method, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ManagedMetric.class).withNonMergedAttributes();
        return (org.springframework.jmx.export.metadata.ManagedMetric) copyPropertiesToBean(ann, org.springframework.jmx.export.metadata.ManagedMetric.class);
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    @Nullable
    public org.springframework.jmx.export.metadata.ManagedOperation getManagedOperation(Method method) throws InvalidMetadataException {
        MergedAnnotation<ManagedOperation> ann = MergedAnnotations.from(method, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ManagedOperation.class).withNonMergedAttributes();
        return (org.springframework.jmx.export.metadata.ManagedOperation) copyPropertiesToBean(ann, org.springframework.jmx.export.metadata.ManagedOperation.class);
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    public org.springframework.jmx.export.metadata.ManagedOperationParameter[] getManagedOperationParameters(Method method) throws InvalidMetadataException {
        List<MergedAnnotation<? extends Annotation>> anns = getRepeatableAnnotations(method, ManagedOperationParameter.class, ManagedOperationParameters.class);
        return (org.springframework.jmx.export.metadata.ManagedOperationParameter[]) copyPropertiesToBeanArray(anns, org.springframework.jmx.export.metadata.ManagedOperationParameter.class);
    }

    @Override // org.springframework.jmx.export.metadata.JmxAttributeSource
    public org.springframework.jmx.export.metadata.ManagedNotification[] getManagedNotifications(Class<?> clazz) throws InvalidMetadataException {
        List<MergedAnnotation<? extends Annotation>> anns = getRepeatableAnnotations(clazz, ManagedNotification.class, ManagedNotifications.class);
        return (org.springframework.jmx.export.metadata.ManagedNotification[]) copyPropertiesToBeanArray(anns, org.springframework.jmx.export.metadata.ManagedNotification.class);
    }

    private static List<MergedAnnotation<? extends Annotation>> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationType, Class<? extends Annotation> containerAnnotationType) {
        return (List) MergedAnnotations.from(annotatedElement, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.of(annotationType, containerAnnotationType)).stream(annotationType).filter(MergedAnnotationPredicates.firstRunOf((v0) -> {
            return v0.getAggregateIndex();
        })).map((v0) -> {
            return v0.withNonMergedAttributes();
        }).collect(Collectors.toList());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> T[] copyPropertiesToBeanArray(List<MergedAnnotation<? extends Annotation>> anns, Class<T> beanClass) {
        T[] beans = (T[]) ((Object[]) Array.newInstance((Class<?>) beanClass, anns.size()));
        int i = 0;
        for (MergedAnnotation<? extends Annotation> ann : anns) {
            int i2 = i;
            i++;
            beans[i2] = copyPropertiesToBean(ann, beanClass);
        }
        return beans;
    }

    @Nullable
    private static <T> T copyPropertiesToBean(MergedAnnotation<? extends Annotation> ann, Class<T> beanClass) {
        if (!ann.isPresent()) {
            return null;
        }
        T bean = (T) BeanUtils.instantiateClass(beanClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        bw.setPropertyValues(new MutablePropertyValues(ann.asMap(new MergedAnnotation.Adapt[0])));
        return bean;
    }
}
