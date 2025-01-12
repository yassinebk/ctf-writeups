package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import java.util.Collection;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/type/SimpleType.class */
public class SimpleType extends TypeBase {
    private static final long serialVersionUID = 1;

    /* JADX INFO: Access modifiers changed from: protected */
    public SimpleType(Class<?> cls) {
        this(cls, TypeBindings.emptyBindings(), null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SimpleType(Class<?> cls, TypeBindings bindings, JavaType superClass, JavaType[] superInts) {
        this(cls, bindings, superClass, superInts, null, null, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SimpleType(TypeBase base) {
        super(base);
    }

    protected SimpleType(Class<?> cls, TypeBindings bindings, JavaType superClass, JavaType[] superInts, Object valueHandler, Object typeHandler, boolean asStatic) {
        super(cls, bindings, superClass, superInts, 0, valueHandler, typeHandler, asStatic);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SimpleType(Class<?> cls, TypeBindings bindings, JavaType superClass, JavaType[] superInts, int extraHash, Object valueHandler, Object typeHandler, boolean asStatic) {
        super(cls, bindings, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);
    }

    public static SimpleType constructUnsafe(Class<?> raw) {
        return new SimpleType(raw, null, null, null, null, null, false);
    }

    @Deprecated
    public static SimpleType construct(Class<?> cls) {
        if (Map.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Cannot construct SimpleType for a Map (class: " + cls.getName() + ")");
        }
        if (Collection.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Cannot construct SimpleType for a Collection (class: " + cls.getName() + ")");
        }
        if (cls.isArray()) {
            throw new IllegalArgumentException("Cannot construct SimpleType for an array (class: " + cls.getName() + ")");
        }
        TypeBindings b = TypeBindings.emptyBindings();
        return new SimpleType(cls, b, _buildSuperClass(cls.getSuperclass(), b), null, null, null, false);
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    @Deprecated
    protected JavaType _narrow(Class<?> subclass) {
        if (this._class == subclass) {
            return this;
        }
        if (!this._class.isAssignableFrom(subclass)) {
            return new SimpleType(subclass, this._bindings, this, this._superInterfaces, this._valueHandler, this._typeHandler, this._asStatic);
        }
        Class<?> next = subclass.getSuperclass();
        if (next == this._class) {
            return new SimpleType(subclass, this._bindings, this, this._superInterfaces, this._valueHandler, this._typeHandler, this._asStatic);
        }
        if (next != null && this._class.isAssignableFrom(next)) {
            JavaType superb = _narrow(next);
            return new SimpleType(subclass, this._bindings, superb, null, this._valueHandler, this._typeHandler, this._asStatic);
        }
        Class<?>[] nextI = subclass.getInterfaces();
        for (Class<?> iface : nextI) {
            if (iface == this._class) {
                return new SimpleType(subclass, this._bindings, null, new JavaType[]{this}, this._valueHandler, this._typeHandler, this._asStatic);
            }
            if (this._class.isAssignableFrom(iface)) {
                JavaType superb2 = _narrow(iface);
                return new SimpleType(subclass, this._bindings, null, new JavaType[]{superb2}, this._valueHandler, this._typeHandler, this._asStatic);
            }
        }
        throw new IllegalArgumentException("Internal error: Cannot resolve sub-type for Class " + subclass.getName() + " to " + this._class.getName());
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    public JavaType withContentType(JavaType contentType) {
        throw new IllegalArgumentException("Simple types have no content types; cannot call withContentType()");
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    public SimpleType withTypeHandler(Object h) {
        if (this._typeHandler == h) {
            return this;
        }
        return new SimpleType(this._class, this._bindings, this._superClass, this._superInterfaces, this._valueHandler, h, this._asStatic);
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    public JavaType withContentTypeHandler(Object h) {
        throw new IllegalArgumentException("Simple types have no content types; cannot call withContenTypeHandler()");
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    public SimpleType withValueHandler(Object h) {
        if (h == this._valueHandler) {
            return this;
        }
        return new SimpleType(this._class, this._bindings, this._superClass, this._superInterfaces, h, this._typeHandler, this._asStatic);
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    public SimpleType withContentValueHandler(Object h) {
        throw new IllegalArgumentException("Simple types have no content types; cannot call withContenValueHandler()");
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    public SimpleType withStaticTyping() {
        return this._asStatic ? this : new SimpleType(this._class, this._bindings, this._superClass, this._superInterfaces, this._valueHandler, this._typeHandler, true);
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
        return null;
    }

    @Override // com.fasterxml.jackson.databind.type.TypeBase
    protected String buildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        int count = this._bindings.size();
        if (count > 0) {
            sb.append('<');
            for (int i = 0; i < count; i++) {
                JavaType t = containedType(i);
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(t.toCanonical());
            }
            sb.append('>');
        }
        return sb.toString();
    }

    @Override // com.fasterxml.jackson.databind.JavaType, com.fasterxml.jackson.core.type.ResolvedType
    public boolean isContainerType() {
        return false;
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    public boolean hasContentType() {
        return false;
    }

    @Override // com.fasterxml.jackson.databind.type.TypeBase, com.fasterxml.jackson.databind.JavaType
    public StringBuilder getErasedSignature(StringBuilder sb) {
        return _classSignature(this._class, sb, true);
    }

    @Override // com.fasterxml.jackson.databind.type.TypeBase, com.fasterxml.jackson.databind.JavaType
    public StringBuilder getGenericSignature(StringBuilder sb) {
        _classSignature(this._class, sb, false);
        int count = this._bindings.size();
        if (count > 0) {
            sb.append('<');
            for (int i = 0; i < count; i++) {
                sb = containedType(i).getGenericSignature(sb);
            }
            sb.append('>');
        }
        sb.append(';');
        return sb;
    }

    private static JavaType _buildSuperClass(Class<?> superClass, TypeBindings b) {
        if (superClass == null) {
            return null;
        }
        if (superClass == Object.class) {
            return TypeFactory.unknownType();
        }
        JavaType superSuper = _buildSuperClass(superClass.getSuperclass(), b);
        return new SimpleType(superClass, b, superSuper, null, null, null, false);
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    public String toString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append("[simple type, class ").append(buildCanonicalName()).append(']');
        return sb.toString();
    }

    @Override // com.fasterxml.jackson.databind.JavaType
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && o.getClass() == getClass()) {
            SimpleType other = (SimpleType) o;
            if (other._class != this._class) {
                return false;
            }
            TypeBindings b1 = this._bindings;
            TypeBindings b2 = other._bindings;
            return b1.equals(b2);
        }
        return false;
    }
}
