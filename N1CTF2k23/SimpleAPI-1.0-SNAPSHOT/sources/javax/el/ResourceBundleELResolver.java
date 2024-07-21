package javax.el;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ResourceBundleELResolver.class */
public class ResourceBundleELResolver extends ELResolver {
    @Override // javax.el.ELResolver
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base instanceof ResourceBundle) {
            context.setPropertyResolved(true);
            if (property != null) {
                try {
                    return ((ResourceBundle) base).getObject(property.toString());
                } catch (MissingResourceException e) {
                    return "???" + property + "???";
                }
            }
            return null;
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base instanceof ResourceBundle) {
            context.setPropertyResolved(true);
            return null;
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base instanceof ResourceBundle) {
            context.setPropertyResolved(true);
            throw new PropertyNotWritableException("ResourceBundles are immutable");
        }
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base instanceof ResourceBundle) {
            context.setPropertyResolved(true);
            return true;
        }
        return false;
    }

    @Override // javax.el.ELResolver
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (base instanceof ResourceBundle) {
            ResourceBundle bundle = (ResourceBundle) base;
            List<FeatureDescriptor> features = new ArrayList<>();
            Enumeration<String> e = bundle.getKeys();
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                FeatureDescriptor desc = new FeatureDescriptor();
                desc.setDisplayName(key);
                desc.setExpert(false);
                desc.setHidden(false);
                desc.setName(key);
                desc.setPreferred(true);
                desc.setValue("type", String.class);
                desc.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, Boolean.TRUE);
                features.add(desc);
            }
            return features.iterator();
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base instanceof ResourceBundle) {
            return String.class;
        }
        return null;
    }
}
