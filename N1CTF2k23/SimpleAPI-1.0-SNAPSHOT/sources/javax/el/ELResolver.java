package javax.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELResolver.class */
public abstract class ELResolver {
    public static final String TYPE = "type";
    public static final String RESOLVABLE_AT_DESIGN_TIME = "resolvableAtDesignTime";

    public abstract Object getValue(ELContext eLContext, Object obj, Object obj2);

    public abstract Class<?> getType(ELContext eLContext, Object obj, Object obj2);

    public abstract void setValue(ELContext eLContext, Object obj, Object obj2, Object obj3);

    public abstract boolean isReadOnly(ELContext eLContext, Object obj, Object obj2);

    public abstract Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext eLContext, Object obj);

    public abstract Class<?> getCommonPropertyType(ELContext eLContext, Object obj);

    public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
        return null;
    }

    public Object convertToType(ELContext context, Object obj, Class<?> targetType) {
        return null;
    }
}
