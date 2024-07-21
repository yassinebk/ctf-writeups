package javax.el;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/MapELResolver.class */
public class MapELResolver extends ELResolver {
    private static Class<?> theUnmodifiableMapClass = Collections.unmodifiableMap(new HashMap()).getClass();
    private boolean isReadOnly;

    public MapELResolver() {
        this.isReadOnly = false;
    }

    public MapELResolver(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && (base instanceof Map)) {
            context.setPropertyResolved(true);
            return Object.class;
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && (base instanceof Map)) {
            context.setPropertyResolved(base, property);
            Map<?, ?> map = (Map) base;
            return map.get(property);
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext context, Object base, Object property, Object val) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && (base instanceof Map)) {
            context.setPropertyResolved(base, property);
            Map<Object, Object> map = (Map) base;
            if (this.isReadOnly || map.getClass() == theUnmodifiableMapClass) {
                throw new PropertyNotWritableException();
            }
            try {
                map.put(property, val);
            } catch (UnsupportedOperationException e) {
                throw new PropertyNotWritableException();
            }
        }
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && (base instanceof Map)) {
            context.setPropertyResolved(true);
            Map<?, ?> map = (Map) base;
            return this.isReadOnly || map.getClass() == theUnmodifiableMapClass;
        }
        return false;
    }

    @Override // javax.el.ELResolver
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (base != null && (base instanceof Map)) {
            Map<?, ?> map = (Map) base;
            Iterator<?> iter = map.keySet().iterator();
            List<FeatureDescriptor> list = new ArrayList<>();
            while (iter.hasNext()) {
                Object key = iter.next();
                FeatureDescriptor descriptor = new FeatureDescriptor();
                String name = key == null ? null : key.toString();
                descriptor.setName(name);
                descriptor.setDisplayName(name);
                descriptor.setShortDescription("");
                descriptor.setExpert(false);
                descriptor.setHidden(false);
                descriptor.setPreferred(true);
                if (key != null) {
                    descriptor.setValue("type", key.getClass());
                }
                descriptor.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, Boolean.TRUE);
                list.add(descriptor);
            }
            return list.iterator();
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null && (base instanceof Map)) {
            return Object.class;
        }
        return null;
    }
}
