package org.springframework.cglib.beans;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.Block;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.Local;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/beans/BulkBeanEmitter.class */
class BulkBeanEmitter extends ClassEmitter {
    private static final Signature GET_PROPERTY_VALUES = TypeUtils.parseSignature("void getPropertyValues(Object, Object[])");
    private static final Signature SET_PROPERTY_VALUES = TypeUtils.parseSignature("void setPropertyValues(Object, Object[])");
    private static final Signature CSTRUCT_EXCEPTION = TypeUtils.parseConstructor("Throwable, int");
    private static final Type BULK_BEAN = TypeUtils.parseType("org.springframework.cglib.beans.BulkBean");
    private static final Type BULK_BEAN_EXCEPTION = TypeUtils.parseType("org.springframework.cglib.beans.BulkBeanException");

    public BulkBeanEmitter(ClassVisitor v, String className, Class target, String[] getterNames, String[] setterNames, Class[] types) {
        super(v);
        Method[] getters = new Method[getterNames.length];
        Method[] setters = new Method[setterNames.length];
        validate(target, getterNames, setterNames, types, getters, setters);
        begin_class(52, 1, className, BULK_BEAN, null, Constants.SOURCE_FILE);
        EmitUtils.null_constructor(this);
        generateGet(target, getters);
        generateSet(target, setters);
        end_class();
    }

    private void generateGet(Class target, Method[] getters) {
        CodeEmitter e = begin_method(1, GET_PROPERTY_VALUES, null);
        if (getters.length > 0) {
            e.load_arg(0);
            e.checkcast(Type.getType(target));
            Local bean = e.make_local();
            e.store_local(bean);
            for (int i = 0; i < getters.length; i++) {
                if (getters[i] != null) {
                    MethodInfo getter = ReflectUtils.getMethodInfo(getters[i]);
                    e.load_arg(1);
                    e.push(i);
                    e.load_local(bean);
                    e.invoke(getter);
                    e.box(getter.getSignature().getReturnType());
                    e.aastore();
                }
            }
        }
        e.return_value();
        e.end_method();
    }

    private void generateSet(Class target, Method[] setters) {
        CodeEmitter e = begin_method(1, SET_PROPERTY_VALUES, null);
        if (setters.length > 0) {
            Local index = e.make_local(Type.INT_TYPE);
            e.push(0);
            e.store_local(index);
            e.load_arg(0);
            e.checkcast(Type.getType(target));
            e.load_arg(1);
            Block handler = e.begin_block();
            int lastIndex = 0;
            for (int i = 0; i < setters.length; i++) {
                if (setters[i] != null) {
                    MethodInfo setter = ReflectUtils.getMethodInfo(setters[i]);
                    int diff = i - lastIndex;
                    if (diff > 0) {
                        e.iinc(index, diff);
                        lastIndex = i;
                    }
                    e.dup2();
                    e.aaload(i);
                    e.unbox(setter.getSignature().getArgumentTypes()[0]);
                    e.invoke(setter);
                }
            }
            handler.end();
            e.return_value();
            e.catch_exception(handler, Constants.TYPE_THROWABLE);
            e.new_instance(BULK_BEAN_EXCEPTION);
            e.dup_x1();
            e.swap();
            e.load_local(index);
            e.invoke_constructor(BULK_BEAN_EXCEPTION, CSTRUCT_EXCEPTION);
            e.athrow();
        } else {
            e.return_value();
        }
        e.end_method();
    }

    private static void validate(Class target, String[] getters, String[] setters, Class[] types, Method[] getters_out, Method[] setters_out) {
        if (setters.length != types.length || getters.length != types.length) {
            throw new BulkBeanException("accessor array length must be equal type array length", -1);
        }
        for (int i = 0; i < types.length; i++) {
            try {
                if (getters[i] != null) {
                    Method method = ReflectUtils.findDeclaredMethod(target, getters[i], null);
                    if (method.getReturnType() != types[i]) {
                        throw new BulkBeanException("Specified type " + types[i] + " does not match declared type " + method.getReturnType(), i);
                    }
                    if (Modifier.isPrivate(method.getModifiers())) {
                        throw new BulkBeanException("Property is private", i);
                    }
                    getters_out[i] = method;
                }
                if (setters[i] != null) {
                    Method method2 = ReflectUtils.findDeclaredMethod(target, setters[i], new Class[]{types[i]});
                    if (Modifier.isPrivate(method2.getModifiers())) {
                        throw new BulkBeanException("Property is private", i);
                    }
                    setters_out[i] = method2;
                }
            } catch (NoSuchMethodException e) {
                throw new BulkBeanException("Cannot find specified property", i);
            }
        }
    }
}
