package org.springframework.cglib.transform.impl;

import java.lang.reflect.Method;
import org.springframework.asm.Type;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.transform.ClassEmitterTransformer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/transform/impl/AddInitTransformer.class */
public class AddInitTransformer extends ClassEmitterTransformer {
    private MethodInfo info;

    public AddInitTransformer(Method method) {
        this.info = ReflectUtils.getMethodInfo(method);
        Type[] types = this.info.getSignature().getArgumentTypes();
        if (types.length != 1 || !types[0].equals(Constants.TYPE_OBJECT) || !this.info.getSignature().getReturnType().equals(Type.VOID_TYPE)) {
            throw new IllegalArgumentException(method + " illegal signature");
        }
    }

    @Override // org.springframework.cglib.core.ClassEmitter
    public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
        CodeEmitter emitter = super.begin_method(access, sig, exceptions);
        if (sig.getName().equals(Constants.CONSTRUCTOR_NAME)) {
            return new CodeEmitter(emitter) { // from class: org.springframework.cglib.transform.impl.AddInitTransformer.1
                @Override // org.springframework.asm.MethodVisitor
                public void visitInsn(int opcode) {
                    if (opcode == 177) {
                        load_this();
                        invoke(AddInitTransformer.this.info);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        return emitter;
    }
}
