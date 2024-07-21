package org.springframework.cglib.transform.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.asm.Label;
import org.springframework.asm.Type;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.ObjectSwitchCallback;
import org.springframework.cglib.core.ProcessSwitchCallback;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.transform.ClassEmitterTransformer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/transform/impl/FieldProviderTransformer.class */
public class FieldProviderTransformer extends ClassEmitterTransformer {
    private static final String FIELD_NAMES = "CGLIB$FIELD_NAMES";
    private static final String FIELD_TYPES = "CGLIB$FIELD_TYPES";
    private static final Type FIELD_PROVIDER = TypeUtils.parseType("org.springframework.cglib.transform.impl.FieldProvider");
    private static final Type ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
    private static final Signature PROVIDER_GET = TypeUtils.parseSignature("Object getField(String)");
    private static final Signature PROVIDER_SET = TypeUtils.parseSignature("void setField(String, Object)");
    private static final Signature PROVIDER_SET_BY_INDEX = TypeUtils.parseSignature("void setField(int, Object)");
    private static final Signature PROVIDER_GET_BY_INDEX = TypeUtils.parseSignature("Object getField(int)");
    private static final Signature PROVIDER_GET_TYPES = TypeUtils.parseSignature("Class[] getFieldTypes()");
    private static final Signature PROVIDER_GET_NAMES = TypeUtils.parseSignature("String[] getFieldNames()");
    private int access;
    private Map fields;

    @Override // org.springframework.cglib.core.ClassEmitter
    public void begin_class(int version, int access, String className, Type superType, Type[] interfaces, String sourceFile) {
        if (!TypeUtils.isAbstract(access)) {
            interfaces = TypeUtils.add(interfaces, FIELD_PROVIDER);
        }
        this.access = access;
        this.fields = new HashMap();
        super.begin_class(version, access, className, superType, interfaces, sourceFile);
    }

    @Override // org.springframework.cglib.core.ClassEmitter
    public void declare_field(int access, String name, Type type, Object value) {
        super.declare_field(access, name, type, value);
        if (!TypeUtils.isStatic(access)) {
            this.fields.put(name, type);
        }
    }

    @Override // org.springframework.cglib.core.ClassEmitter
    public void end_class() {
        if (!TypeUtils.isInterface(this.access)) {
            try {
                generate();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new CodeGenerationException(e2);
            }
        }
        super.end_class();
    }

    private void generate() throws Exception {
        String[] names = (String[]) this.fields.keySet().toArray(new String[this.fields.size()]);
        int[] indexes = new int[names.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        super.declare_field(26, FIELD_NAMES, Constants.TYPE_STRING_ARRAY, null);
        super.declare_field(26, FIELD_TYPES, Constants.TYPE_CLASS_ARRAY, null);
        initFieldProvider(names);
        getNames();
        getTypes();
        getField(names);
        setField(names);
        setByIndex(names, indexes);
        getByIndex(names, indexes);
    }

    private void initFieldProvider(String[] names) {
        CodeEmitter e = getStaticHook();
        EmitUtils.push_object(e, names);
        e.putstatic(getClassType(), FIELD_NAMES, Constants.TYPE_STRING_ARRAY);
        e.push(names.length);
        e.newarray(Constants.TYPE_CLASS);
        e.dup();
        for (int i = 0; i < names.length; i++) {
            e.dup();
            e.push(i);
            Type type = (Type) this.fields.get(names[i]);
            EmitUtils.load_class(e, type);
            e.aastore();
        }
        e.putstatic(getClassType(), FIELD_TYPES, Constants.TYPE_CLASS_ARRAY);
    }

    private void getNames() {
        CodeEmitter e = super.begin_method(1, PROVIDER_GET_NAMES, null);
        e.getstatic(getClassType(), FIELD_NAMES, Constants.TYPE_STRING_ARRAY);
        e.return_value();
        e.end_method();
    }

    private void getTypes() {
        CodeEmitter e = super.begin_method(1, PROVIDER_GET_TYPES, null);
        e.getstatic(getClassType(), FIELD_TYPES, Constants.TYPE_CLASS_ARRAY);
        e.return_value();
        e.end_method();
    }

    private void setByIndex(final String[] names, int[] indexes) throws Exception {
        final CodeEmitter e = super.begin_method(1, PROVIDER_SET_BY_INDEX, null);
        e.load_this();
        e.load_arg(1);
        e.load_arg(0);
        e.process_switch(indexes, new ProcessSwitchCallback() { // from class: org.springframework.cglib.transform.impl.FieldProviderTransformer.1
            @Override // org.springframework.cglib.core.ProcessSwitchCallback
            public void processCase(int key, Label end) throws Exception {
                Type type = (Type) FieldProviderTransformer.this.fields.get(names[key]);
                e.unbox(type);
                e.putfield(names[key]);
                e.return_value();
            }

            @Override // org.springframework.cglib.core.ProcessSwitchCallback
            public void processDefault() throws Exception {
                e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field index");
            }
        });
        e.end_method();
    }

    private void getByIndex(final String[] names, int[] indexes) throws Exception {
        final CodeEmitter e = super.begin_method(1, PROVIDER_GET_BY_INDEX, null);
        e.load_this();
        e.load_arg(0);
        e.process_switch(indexes, new ProcessSwitchCallback() { // from class: org.springframework.cglib.transform.impl.FieldProviderTransformer.2
            @Override // org.springframework.cglib.core.ProcessSwitchCallback
            public void processCase(int key, Label end) throws Exception {
                Type type = (Type) FieldProviderTransformer.this.fields.get(names[key]);
                e.getfield(names[key]);
                e.box(type);
                e.return_value();
            }

            @Override // org.springframework.cglib.core.ProcessSwitchCallback
            public void processDefault() throws Exception {
                e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field index");
            }
        });
        e.end_method();
    }

    private void getField(String[] names) throws Exception {
        final CodeEmitter e = begin_method(1, PROVIDER_GET, null);
        e.load_this();
        e.load_arg(0);
        EmitUtils.string_switch(e, names, 1, new ObjectSwitchCallback() { // from class: org.springframework.cglib.transform.impl.FieldProviderTransformer.3
            @Override // org.springframework.cglib.core.ObjectSwitchCallback
            public void processCase(Object key, Label end) {
                Type type = (Type) FieldProviderTransformer.this.fields.get(key);
                e.getfield((String) key);
                e.box(type);
                e.return_value();
            }

            @Override // org.springframework.cglib.core.ObjectSwitchCallback
            public void processDefault() {
                e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field name");
            }
        });
        e.end_method();
    }

    private void setField(String[] names) throws Exception {
        final CodeEmitter e = begin_method(1, PROVIDER_SET, null);
        e.load_this();
        e.load_arg(1);
        e.load_arg(0);
        EmitUtils.string_switch(e, names, 1, new ObjectSwitchCallback() { // from class: org.springframework.cglib.transform.impl.FieldProviderTransformer.4
            @Override // org.springframework.cglib.core.ObjectSwitchCallback
            public void processCase(Object key, Label end) {
                Type type = (Type) FieldProviderTransformer.this.fields.get(key);
                e.unbox(type);
                e.putfield((String) key);
                e.return_value();
            }

            @Override // org.springframework.cglib.core.ObjectSwitchCallback
            public void processDefault() {
                e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field name");
            }
        });
        e.end_method();
    }
}
