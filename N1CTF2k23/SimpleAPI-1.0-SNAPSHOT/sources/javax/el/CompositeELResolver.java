package javax.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/CompositeELResolver.class */
public class CompositeELResolver extends ELResolver {
    private int size = 0;
    private ELResolver[] elResolvers = new ELResolver[16];

    public void add(ELResolver elResolver) {
        if (elResolver == null) {
            throw new NullPointerException();
        }
        if (this.size >= this.elResolvers.length) {
            ELResolver[] newResolvers = new ELResolver[this.size * 2];
            System.arraycopy(this.elResolvers, 0, newResolvers, 0, this.size);
            this.elResolvers = newResolvers;
        }
        ELResolver[] eLResolverArr = this.elResolvers;
        int i = this.size;
        this.size = i + 1;
        eLResolverArr[i] = elResolver;
    }

    @Override // javax.el.ELResolver
    public Object getValue(ELContext context, Object base, Object property) {
        context.setPropertyResolved(false);
        for (int i = 0; i < this.size; i++) {
            Object value = this.elResolvers[i].getValue(context, base, property);
            if (context.isPropertyResolved()) {
                return value;
            }
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
        context.setPropertyResolved(false);
        for (int i = 0; i < this.size; i++) {
            Object value = this.elResolvers[i].invoke(context, base, method, paramTypes, params);
            if (context.isPropertyResolved()) {
                return value;
            }
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext context, Object base, Object property) {
        context.setPropertyResolved(false);
        for (int i = 0; i < this.size; i++) {
            Class<?> type = this.elResolvers[i].getType(context, base, property);
            if (context.isPropertyResolved()) {
                return type;
            }
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext context, Object base, Object property, Object val) {
        context.setPropertyResolved(false);
        for (int i = 0; i < this.size; i++) {
            this.elResolvers[i].setValue(context, base, property, val);
            if (context.isPropertyResolved()) {
                return;
            }
        }
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        context.setPropertyResolved(false);
        for (int i = 0; i < this.size; i++) {
            boolean readOnly = this.elResolvers[i].isReadOnly(context, base, property);
            if (context.isPropertyResolved()) {
                return readOnly;
            }
        }
        return false;
    }

    @Override // javax.el.ELResolver
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return new CompositeIterator(this.elResolvers, this.size, context, base);
    }

    @Override // javax.el.ELResolver
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        Class<?> commonPropertyType = null;
        for (int i = 0; i < this.size; i++) {
            Class<?> type = this.elResolvers[i].getCommonPropertyType(context, base);
            if (type != null) {
                if (commonPropertyType == null) {
                    commonPropertyType = type;
                } else if (commonPropertyType.isAssignableFrom(type)) {
                    continue;
                } else if (type.isAssignableFrom(commonPropertyType)) {
                    commonPropertyType = type;
                } else {
                    return null;
                }
            }
        }
        return commonPropertyType;
    }

    @Override // javax.el.ELResolver
    public Object convertToType(ELContext context, Object obj, Class<?> targetType) {
        context.setPropertyResolved(false);
        for (int i = 0; i < this.size; i++) {
            Object value = this.elResolvers[i].convertToType(context, obj, targetType);
            if (context.isPropertyResolved()) {
                return value;
            }
        }
        return null;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/CompositeELResolver$CompositeIterator.class */
    private static class CompositeIterator implements Iterator<FeatureDescriptor> {
        ELResolver[] resolvers;
        int size;
        int index = 0;
        Iterator<FeatureDescriptor> propertyIter = null;
        ELContext context;
        Object base;

        CompositeIterator(ELResolver[] resolvers, int size, ELContext context, Object base) {
            this.resolvers = resolvers;
            this.size = size;
            this.context = context;
            this.base = base;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.propertyIter == null || !this.propertyIter.hasNext()) {
                while (this.index < this.size) {
                    ELResolver[] eLResolverArr = this.resolvers;
                    int i = this.index;
                    this.index = i + 1;
                    ELResolver elResolver = eLResolverArr[i];
                    this.propertyIter = elResolver.getFeatureDescriptors(this.context, this.base);
                    if (this.propertyIter != null) {
                        return this.propertyIter.hasNext();
                    }
                }
                return false;
            }
            return this.propertyIter.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public FeatureDescriptor next() {
            if (this.propertyIter == null || !this.propertyIter.hasNext()) {
                while (this.index < this.size) {
                    ELResolver[] eLResolverArr = this.resolvers;
                    int i = this.index;
                    this.index = i + 1;
                    ELResolver elResolver = eLResolverArr[i];
                    this.propertyIter = elResolver.getFeatureDescriptors(this.context, this.base);
                    if (this.propertyIter != null) {
                        return this.propertyIter.next();
                    }
                }
                return null;
            }
            return this.propertyIter.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
