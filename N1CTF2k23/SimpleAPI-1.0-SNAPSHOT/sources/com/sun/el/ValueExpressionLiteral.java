package com.sun.el;

import com.sun.el.util.MessageFactory;
import com.sun.el.util.ReflectionUtil;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/ValueExpressionLiteral.class */
public final class ValueExpressionLiteral extends ValueExpression implements Externalizable {
    private static final long serialVersionUID = 1;
    private Object value;
    private Class<?> expectedType;

    public ValueExpressionLiteral() {
    }

    public ValueExpressionLiteral(Object value, Class<?> expectedType) {
        this.value = value;
        this.expectedType = expectedType;
    }

    @Override // javax.el.ValueExpression
    public Object getValue(ELContext context) {
        if (this.expectedType != null) {
            try {
                return context.convertToType(this.value, this.expectedType);
            } catch (IllegalArgumentException ex) {
                throw new ELException(ex);
            }
        }
        return this.value;
    }

    @Override // javax.el.ValueExpression
    public void setValue(ELContext context, Object value) {
        throw new PropertyNotWritableException(MessageFactory.get("error.value.literal.write", value));
    }

    @Override // javax.el.ValueExpression
    public boolean isReadOnly(ELContext context) {
        return true;
    }

    @Override // javax.el.ValueExpression
    public Class<?> getType(ELContext context) {
        if (this.value != null) {
            return this.value.getClass();
        }
        return null;
    }

    @Override // javax.el.ValueExpression
    public Class<?> getExpectedType() {
        return this.expectedType;
    }

    @Override // javax.el.Expression
    public String getExpressionString() {
        if (this.value != null) {
            return this.value.toString();
        }
        return null;
    }

    @Override // javax.el.Expression
    public boolean equals(Object obj) {
        return (obj instanceof ValueExpressionLiteral) && equals((ValueExpressionLiteral) obj);
    }

    public boolean equals(ValueExpressionLiteral ve) {
        return (ve == null || this.value == null || ve.value == null || (this.value != ve.value && !this.value.equals(ve.value))) ? false : true;
    }

    @Override // javax.el.Expression
    public int hashCode() {
        if (this.value != null) {
            return this.value.hashCode();
        }
        return 0;
    }

    @Override // javax.el.Expression
    public boolean isLiteralText() {
        return true;
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.value);
        out.writeUTF(this.expectedType != null ? this.expectedType.getName() : "");
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.value = in.readObject();
        String type = in.readUTF();
        if (!"".equals(type)) {
            this.expectedType = ReflectionUtil.forName(type);
        }
    }
}
