package org.springframework.cglib.proxy;

import java.lang.reflect.Method;
import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.ReflectUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/proxy/MixinBeanEmitter.class */
class MixinBeanEmitter extends MixinEmitter {
    public MixinBeanEmitter(ClassVisitor v, String className, Class[] classes) {
        super(v, className, classes, null);
    }

    @Override // org.springframework.cglib.proxy.MixinEmitter
    protected Class[] getInterfaces(Class[] classes) {
        return null;
    }

    @Override // org.springframework.cglib.proxy.MixinEmitter
    protected Method[] getMethods(Class type) {
        return ReflectUtils.getPropertyMethods(ReflectUtils.getBeanProperties(type), true, true);
    }
}
