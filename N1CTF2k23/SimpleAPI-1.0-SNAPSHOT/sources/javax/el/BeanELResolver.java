package javax.el;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/BeanELResolver.class */
public class BeanELResolver extends ELResolver {
    private boolean isReadOnly;
    private static final SoftConcurrentHashMap properties = new SoftConcurrentHashMap();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/BeanELResolver$BPSoftReference.class */
    public static class BPSoftReference extends SoftReference<BeanProperties> {
        final Class<?> key;

        BPSoftReference(Class<?> key, BeanProperties beanProperties, ReferenceQueue<BeanProperties> refQ) {
            super(beanProperties, refQ);
            this.key = key;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/BeanELResolver$SoftConcurrentHashMap.class */
    public static class SoftConcurrentHashMap extends ConcurrentHashMap<Class<?>, BeanProperties> {
        private static final long serialVersionUID = -178867497897782229L;
        private static final int CACHE_INIT_SIZE = 1024;
        private ConcurrentHashMap<Class<?>, BPSoftReference> map;
        private ReferenceQueue<BeanProperties> refQ;

        private SoftConcurrentHashMap() {
            this.map = new ConcurrentHashMap<>(1024);
            this.refQ = new ReferenceQueue<>();
        }

        private void cleanup() {
            while (true) {
                BPSoftReference BPRef = (BPSoftReference) this.refQ.poll();
                if (BPRef != null) {
                    this.map.remove(BPRef.key);
                } else {
                    return;
                }
            }
        }

        @Override // java.util.concurrent.ConcurrentHashMap, java.util.AbstractMap, java.util.Map
        public BeanProperties put(Class<?> key, BeanProperties value) {
            cleanup();
            BPSoftReference prev = this.map.put(key, new BPSoftReference(key, value, this.refQ));
            if (prev == null) {
                return null;
            }
            return prev.get();
        }

        @Override // java.util.concurrent.ConcurrentHashMap, java.util.Map, java.util.concurrent.ConcurrentMap
        public BeanProperties putIfAbsent(Class<?> key, BeanProperties value) {
            cleanup();
            BPSoftReference prev = this.map.putIfAbsent(key, new BPSoftReference(key, value, this.refQ));
            if (prev == null) {
                return null;
            }
            return prev.get();
        }

        @Override // java.util.concurrent.ConcurrentHashMap, java.util.AbstractMap, java.util.Map
        public BeanProperties get(Object key) {
            cleanup();
            BPSoftReference BPRef = this.map.get(key);
            if (BPRef == null) {
                return null;
            }
            if (BPRef.get() == null) {
                this.map.remove(key);
                return null;
            }
            return BPRef.get();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/BeanELResolver$BeanProperty.class */
    public static final class BeanProperty {
        private Method readMethod;
        private Method writeMethod;
        private PropertyDescriptor descriptor;

        public BeanProperty(Class<?> baseClass, PropertyDescriptor descriptor) {
            this.descriptor = descriptor;
            this.readMethod = ELUtil.getMethod(baseClass, descriptor.getReadMethod());
            this.writeMethod = ELUtil.getMethod(baseClass, descriptor.getWriteMethod());
        }

        public Class<?> getPropertyType() {
            return this.descriptor.getPropertyType();
        }

        public boolean isReadOnly() {
            return getWriteMethod() == null;
        }

        public Method getReadMethod() {
            return this.readMethod;
        }

        public Method getWriteMethod() {
            return this.writeMethod;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/BeanELResolver$BeanProperties.class */
    public static final class BeanProperties {
        private final Map<String, BeanProperty> propertyMap = new HashMap();

        public BeanProperties(Class<?> baseClass) {
            try {
                BeanInfo info = Introspector.getBeanInfo(baseClass);
                PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
                for (PropertyDescriptor descriptor : descriptors) {
                    this.propertyMap.put(descriptor.getName(), new BeanProperty(baseClass, descriptor));
                }
            } catch (IntrospectionException ie) {
                throw new ELException((Throwable) ie);
            }
        }

        public BeanProperty getBeanProperty(String property) {
            return this.propertyMap.get(property);
        }
    }

    public BeanELResolver() {
        this.isReadOnly = false;
    }

    public BeanELResolver(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null || property == null) {
            return null;
        }
        BeanProperty beanProperty = getBeanProperty(context, base, property);
        context.setPropertyResolved(true);
        return beanProperty.getPropertyType();
    }

    @Override // javax.el.ELResolver
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null || property == null) {
            return null;
        }
        Method method = getBeanProperty(context, base, property).getReadMethod();
        if (method == null) {
            throw new PropertyNotFoundException(ELUtil.getExceptionMessageString(context, "propertyNotReadable", new Object[]{base.getClass().getName(), property.toString()}));
        }
        try {
            Object value = method.invoke(base, new Object[0]);
            context.setPropertyResolved(base, property);
            return value;
        } catch (InvocationTargetException ite) {
            throw new ELException(ite.getCause());
        } catch (ELException ex) {
            throw ex;
        } catch (Exception ex2) {
            throw new ELException(ex2);
        }
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext context, Object base, Object property, Object val) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null || property == null) {
            return;
        }
        if (this.isReadOnly) {
            throw new PropertyNotWritableException(ELUtil.getExceptionMessageString(context, "resolverNotwritable", new Object[]{base.getClass().getName()}));
        }
        Method method = getBeanProperty(context, base, property).getWriteMethod();
        if (method == null) {
            throw new PropertyNotWritableException(ELUtil.getExceptionMessageString(context, "propertyNotWritable", new Object[]{base.getClass().getName(), property.toString()}));
        }
        try {
            method.invoke(base, val);
            context.setPropertyResolved(base, property);
        } catch (InvocationTargetException ite) {
            throw new ELException(ite.getCause());
        } catch (ELException ex) {
            throw ex;
        } catch (Exception ex2) {
            if (null == val) {
                val = BeanDefinitionParserDelegate.NULL_ELEMENT;
            }
            String message = ELUtil.getExceptionMessageString(context, "setPropertyFailed", new Object[]{property.toString(), base.getClass().getName(), val});
            throw new ELException(message, ex2);
        }
    }

    @Override // javax.el.ELResolver
    public Object invoke(ELContext context, Object base, Object methodName, Class<?>[] paramTypes, Object[] params) {
        if (base == null || methodName == null) {
            return null;
        }
        Method method = ELUtil.findMethod(base.getClass(), methodName.toString(), paramTypes, params, false);
        for (Object param : params) {
            if (param instanceof LambdaExpression) {
                ((LambdaExpression) param).setELContext(context);
            }
        }
        Object ret = ELUtil.invokeMethod(context, method, base, params);
        context.setPropertyResolved(base, methodName);
        return ret;
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null || property == null) {
            return false;
        }
        context.setPropertyResolved(true);
        if (this.isReadOnly) {
            return true;
        }
        return getBeanProperty(context, base, property).isReadOnly();
    }

    @Override // javax.el.ELResolver
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        PropertyDescriptor[] propertyDescriptors;
        if (base == null) {
            return null;
        }
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(base.getClass());
        } catch (Exception e) {
        }
        if (info == null) {
            return null;
        }
        ArrayList<FeatureDescriptor> featureDescriptors = new ArrayList<>(info.getPropertyDescriptors().length);
        for (PropertyDescriptor propertyDescriptor : info.getPropertyDescriptors()) {
            propertyDescriptor.setValue("type", propertyDescriptor.getPropertyType());
            propertyDescriptor.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, Boolean.TRUE);
            featureDescriptors.add(propertyDescriptor);
        }
        return featureDescriptors.iterator();
    }

    @Override // javax.el.ELResolver
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base == null) {
            return null;
        }
        return Object.class;
    }

    private BeanProperty getBeanProperty(ELContext context, Object base, Object prop) {
        String property = prop.toString();
        Class<?> baseClass = base.getClass();
        BeanProperties beanProperties = properties.get((Object) baseClass);
        if (beanProperties == null) {
            beanProperties = new BeanProperties(baseClass);
            properties.put(baseClass, beanProperties);
        }
        BeanProperty beanProperty = beanProperties.getBeanProperty(property);
        if (beanProperty == null) {
            throw new PropertyNotFoundException(ELUtil.getExceptionMessageString(context, "propertyNotFound", new Object[]{baseClass.getName(), property}));
        }
        return beanProperty;
    }
}
