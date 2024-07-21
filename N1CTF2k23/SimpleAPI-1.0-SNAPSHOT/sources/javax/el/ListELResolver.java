package javax.el;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ListELResolver.class */
public class ListELResolver extends ELResolver {
    private static Class<?> theUnmodifiableListClass = Collections.unmodifiableList(new ArrayList()).getClass();
    private boolean isReadOnly;

    public ListELResolver() {
        this.isReadOnly = false;
    }

    public ListELResolver(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && (base instanceof List)) {
            context.setPropertyResolved(true);
            List<?> list = (List) base;
            int index = toInteger(property);
            if (index < 0 || index >= list.size()) {
                throw new PropertyNotFoundException();
            }
            return Object.class;
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && (base instanceof List)) {
            context.setPropertyResolved(base, property);
            List<?> list = (List) base;
            int index = toInteger(property);
            if (index < 0 || index >= list.size()) {
                return null;
            }
            return list.get(index);
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext context, Object base, Object property, Object val) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && (base instanceof List)) {
            context.setPropertyResolved(base, property);
            List<Object> list = (List) base;
            int index = toInteger(property);
            if (this.isReadOnly) {
                throw new PropertyNotWritableException();
            }
            try {
                list.set(index, val);
            } catch (ClassCastException ex) {
                throw ex;
            } catch (IllegalArgumentException ex2) {
                throw ex2;
            } catch (IndexOutOfBoundsException e) {
                throw new PropertyNotFoundException();
            } catch (NullPointerException ex3) {
                throw ex3;
            } catch (UnsupportedOperationException e2) {
                throw new PropertyNotWritableException();
            }
        }
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base != null && (base instanceof List)) {
            context.setPropertyResolved(true);
            List<?> list = (List) base;
            int index = toInteger(property);
            if (index < 0 || index >= list.size()) {
                throw new PropertyNotFoundException();
            }
            return list.getClass() == theUnmodifiableListClass || this.isReadOnly;
        }
        return false;
    }

    @Override // javax.el.ELResolver
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null && (base instanceof List)) {
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
        if (p instanceof Number) {
            return ((Number) p).intValue();
        }
        if (p instanceof String) {
            return Integer.parseInt((String) p);
        }
        throw new IllegalArgumentException();
    }
}
