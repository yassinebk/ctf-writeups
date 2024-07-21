package com.sun.el.lang;

import com.sun.el.util.ReflectionUtil;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.el.FunctionMapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/FunctionMapperImpl.class */
public class FunctionMapperImpl extends FunctionMapper implements Externalizable {
    private static final long serialVersionUID = 1;
    protected Map<String, Function> functions = null;

    @Override // javax.el.FunctionMapper
    public Method resolveFunction(String prefix, String localName) {
        if (this.functions != null) {
            Function f = this.functions.get(prefix + ":" + localName);
            return f.getMethod();
        }
        return null;
    }

    public void addFunction(String prefix, String localName, Method m) {
        if (this.functions == null) {
            this.functions = new HashMap();
        }
        Function f = new Function(prefix, localName, m);
        synchronized (this) {
            this.functions.put(prefix + ":" + localName, f);
        }
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.functions);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.functions = (Map) in.readObject();
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/FunctionMapperImpl$Function.class */
    public static class Function implements Externalizable {
        protected transient Method m;
        protected String owner;
        protected String name;
        protected String[] types;
        protected String prefix;
        protected String localName;

        public Function(String prefix, String localName, Method m) {
            if (localName == null) {
                throw new NullPointerException("LocalName cannot be null");
            }
            if (m == null) {
                throw new NullPointerException("Method cannot be null");
            }
            this.prefix = prefix;
            this.localName = localName;
            this.m = m;
        }

        public Function() {
        }

        @Override // java.io.Externalizable
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(this.prefix != null ? this.prefix : "");
            out.writeUTF(this.localName);
            if (this.owner != null) {
                out.writeUTF(this.owner);
            } else {
                out.writeUTF(this.m.getDeclaringClass().getName());
            }
            if (this.name != null) {
                out.writeUTF(this.name);
            } else {
                out.writeUTF(this.m.getName());
            }
            if (this.types != null) {
                out.writeObject(this.types);
            } else {
                out.writeObject(ReflectionUtil.toTypeNameArray(this.m.getParameterTypes()));
            }
        }

        @Override // java.io.Externalizable
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.prefix = in.readUTF();
            if ("".equals(this.prefix)) {
                this.prefix = null;
            }
            this.localName = in.readUTF();
            this.owner = in.readUTF();
            this.name = in.readUTF();
            this.types = (String[]) in.readObject();
        }

        public Method getMethod() {
            if (this.m == null) {
                try {
                    Class<?> t = Class.forName(this.owner, false, Thread.currentThread().getContextClassLoader());
                    Class[] p = ReflectionUtil.toTypeArray(this.types);
                    this.m = t.getMethod(this.name, p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return this.m;
        }

        public boolean matches(String prefix, String localName) {
            if (this.prefix != null && (prefix == null || !this.prefix.equals(prefix))) {
                return false;
            }
            return this.localName.equals(localName);
        }

        public boolean equals(Object obj) {
            return (obj instanceof Function) && hashCode() == obj.hashCode();
        }

        public int hashCode() {
            return (this.prefix + this.localName).hashCode();
        }
    }
}
