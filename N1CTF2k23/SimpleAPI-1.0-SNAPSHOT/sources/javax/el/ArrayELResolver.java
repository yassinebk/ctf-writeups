package javax.el;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Array;
import java.util.Iterator;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ArrayELResolver.class */
public class ArrayELResolver extends ELResolver {
    private boolean isReadOnly;

    public ArrayELResolver() {
        this.isReadOnly = false;
    }

    public ArrayELResolver(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && base.getClass().isArray()) {
            context.setPropertyResolved(true);
            int index = toInteger(property);
            if (index < 0 || index >= Array.getLength(base)) {
                throw new PropertyNotFoundException();
            }
            return base.getClass().getComponentType();
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && base.getClass().isArray()) {
            context.setPropertyResolved(base, property);
            int index = toInteger(property);
            if (index >= 0 && index < Array.getLength(base)) {
                return Array.get(base, index);
            }
            return null;
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext context, Object base, Object property, Object val) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && base.getClass().isArray()) {
            context.setPropertyResolved(base, property);
            if (this.isReadOnly) {
                throw new PropertyNotWritableException();
            }
            Class<?> type = base.getClass().getComponentType();
            if (val != null && !type.isAssignableFrom(val.getClass())) {
                throw new ClassCastException();
            }
            int index = toInteger(property);
            if (index < 0 || index >= Array.getLength(base)) {
                throw new PropertyNotFoundException();
            }
            Array.set(base, index, val);
        }
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && base.getClass().isArray()) {
            context.setPropertyResolved(true);
            int index = toInteger(property);
            if (index < 0 || index >= Array.getLength(base)) {
                throw new PropertyNotFoundException();
            }
        }
        return this.isReadOnly;
    }

    @Override // javax.el.ELResolver
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null && base.getClass().isArray()) {
            return Integer.class;
        }
        return null;
    }

    private int toInteger(Object p) {
        if (p instanceof Integer) {
            return ((Integer) p).intValue();
        }
        if (p instanceof Character) {
            return ((Character) p).charValue();
        }
        if (p instanceof Boolean) {
            return ((Boolean) p).booleanValue() ? 1 : 0;
        } else if (p instanceof Number) {
            return ((Number) p).intValue();
        } else {
            if (p instanceof String) {
                return Integer.parseInt((String) p);
            }
            throw new IllegalArgumentException();
        }
    }
}
