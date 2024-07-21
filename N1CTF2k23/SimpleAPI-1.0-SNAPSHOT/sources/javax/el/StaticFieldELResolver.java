package javax.el;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import org.springframework.cglib.core.Constants;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/StaticFieldELResolver.class */
public class StaticFieldELResolver extends ELResolver {
    @Override // javax.el.ELResolver
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if ((base instanceof ELClass) && (property instanceof String)) {
            Class<?> klass = ((ELClass) base).getKlass();
            String fieldName = (String) property;
            try {
                context.setPropertyResolved(base, property);
                Field field = klass.getField(fieldName);
                int mod = field.getModifiers();
                if (Modifier.isPublic(mod) && Modifier.isStatic(mod)) {
                    return field.get(null);
                }
            } catch (IllegalAccessException e) {
            } catch (NoSuchFieldException e2) {
            }
            throw new PropertyNotFoundException(ELUtil.getExceptionMessageString(context, "staticFieldReadError", new Object[]{klass.getName(), fieldName}));
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (context == null) {
            throw new NullPointerException();
        }
        if ((base instanceof ELClass) && (property instanceof String)) {
            Class<?> klass = ((ELClass) base).getKlass();
            String fieldName = (String) property;
            throw new PropertyNotWritableException(ELUtil.getExceptionMessageString(context, "staticFieldWriteError", new Object[]{klass.getName(), fieldName}));
        }
    }

    @Override // javax.el.ELResolver
    public Object invoke(ELContext context, Object base, Object methodName, Class<?>[] paramTypes, Object[] params) {
        Object ret;
        if (context == null) {
            throw new NullPointerException();
        }
        if (!(base instanceof ELClass) || !(methodName instanceof String)) {
            return null;
        }
        Class<?> klass = ((ELClass) base).getKlass();
        String name = (String) methodName;
        if (Constants.CONSTRUCTOR_NAME.equals(name)) {
            Constructor<?> constructor = ELUtil.findConstructor(klass, paramTypes, params);
            ret = ELUtil.invokeConstructor(context, constructor, params);
        } else {
            Method method = ELUtil.findMethod(klass, name, paramTypes, params, true);
            ret = ELUtil.invokeMethod(context, method, null, params);
        }
        context.setPropertyResolved(base, methodName);
        return ret;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if ((base instanceof ELClass) && (property instanceof String)) {
            Class<?> klass = ((ELClass) base).getKlass();
            String fieldName = (String) property;
            try {
                context.setPropertyResolved(true);
                Field field = klass.getField(fieldName);
                int mod = field.getModifiers();
                if (Modifier.isPublic(mod) && Modifier.isStatic(mod)) {
                    return field.getType();
                }
            } catch (NoSuchFieldException e) {
            }
            throw new PropertyNotFoundException(ELUtil.getExceptionMessageString(context, "staticFieldReadError", new Object[]{klass.getName(), fieldName}));
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if ((base instanceof ELClass) && (property instanceof String)) {
            ((ELClass) base).getKlass();
            context.setPropertyResolved(true);
            return true;
        }
        return true;
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
