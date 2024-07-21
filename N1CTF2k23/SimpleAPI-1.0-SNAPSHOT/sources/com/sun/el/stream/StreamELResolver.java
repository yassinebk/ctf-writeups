package com.sun.el.stream;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELResolver;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/stream/StreamELResolver.class */
public class StreamELResolver extends ELResolver {
    @Override // javax.el.ELResolver
    public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base instanceof Collection) {
            Collection<Object> c = (Collection) base;
            if ("stream".equals(method) && params.length == 0) {
                context.setPropertyResolved(true);
                return new Stream(c.iterator());
            }
        }
        if (base.getClass().isArray() && "stream".equals(method) && params.length == 0) {
            context.setPropertyResolved(true);
            return new Stream(arrayIterator(base));
        }
        return null;
    }

    private static Iterator<Object> arrayIterator(final Object base) {
        final int size = Array.getLength(base);
        return new Iterator<Object>() { // from class: com.sun.el.stream.StreamELResolver.1
            int index = 0;
            boolean yielded;
            Object current;

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (!this.yielded && this.index < size) {
                    Object obj = base;
                    int i = this.index;
                    this.index = i + 1;
                    this.current = Array.get(obj, i);
                    this.yielded = true;
                }
                return this.yielded;
            }

            @Override // java.util.Iterator
            public Object next() {
                this.yielded = false;
                return this.current;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // javax.el.ELResolver
    public Object getValue(ELContext context, Object base, Object property) {
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext context, Object base, Object property) {
        return null;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext context, Object base, Object property, Object value) {
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext context, Object base, Object property) {
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
