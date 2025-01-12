package com.fasterxml.jackson.databind.jsontype;

import java.io.Serializable;
import java.util.Objects;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/jsontype/NamedType.class */
public final class NamedType implements Serializable {
    private static final long serialVersionUID = 1;
    protected final Class<?> _class;
    protected final int _hashCode;
    protected String _name;

    public NamedType(Class<?> c) {
        this(c, null);
    }

    public NamedType(Class<?> c, String name) {
        this._class = c;
        this._hashCode = c.getName().hashCode() + (name == null ? 0 : name.hashCode());
        setName(name);
    }

    public Class<?> getType() {
        return this._class;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = (name == null || name.length() == 0) ? null : name;
    }

    public boolean hasName() {
        return this._name != null;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && o.getClass() == getClass()) {
            NamedType other = (NamedType) o;
            return this._class == other._class && Objects.equals(this._name, other._name);
        }
        return false;
    }

    public int hashCode() {
        return this._hashCode;
    }

    public String toString() {
        return "[NamedType, class " + this._class.getName() + ", name: " + (this._name == null ? BeanDefinitionParserDelegate.NULL_ELEMENT : "'" + this._name + "'") + "]";
    }
}
