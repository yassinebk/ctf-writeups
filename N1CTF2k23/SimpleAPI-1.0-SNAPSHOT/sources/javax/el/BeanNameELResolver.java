package javax.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/BeanNameELResolver.class */
public class BeanNameELResolver extends ELResolver {
    private BeanNameResolver beanNameResolver;

    public BeanNameELResolver(BeanNameResolver beanNameResolver) {
        this.beanNameResolver = beanNameResolver;
    }

    @Override // javax.el.ELResolver
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null && (property instanceof String) && this.beanNameResolver.isNameResolved((String) property)) {
            context.setPropertyResolved(base, property);
            return this.beanNameResolver.getBean((String) property);
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null && (property instanceof String)) {
            String beanName = (String) property;
            if (this.beanNameResolver.isNameResolved(beanName) || this.beanNameResolver.canCreateBean(beanName)) {
                this.beanNameResolver.setBeanValue(beanName, value);
                context.setPropertyResolved(base, property);
            }
        }
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null && (property instanceof String) && this.beanNameResolver.isNameResolved((String) property)) {
            context.setPropertyResolved(true);
            return this.beanNameResolver.getBean((String) property).getClass();
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null && (property instanceof String) && this.beanNameResolver.isNameResolved((String) property)) {
            context.setPropertyResolved(true);
            return this.beanNameResolver.isReadOnly((String) property);
        }
        return false;
    }

    @Override // javax.el.ELResolver
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return String.class;
    }
}
