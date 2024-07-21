package com.sun.el;

import com.sun.el.lang.EvaluationContext;
import com.sun.el.lang.ExpressionBuilder;
import com.sun.el.parser.AstLiteralExpression;
import com.sun.el.parser.Node;
import com.sun.el.util.ReflectionUtil;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.FunctionMapper;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
import javax.el.ValueReference;
import javax.el.VariableMapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/ValueExpressionImpl.class */
public final class ValueExpressionImpl extends ValueExpression implements Externalizable {
    private Class<?> expectedType;
    private String expr;
    private FunctionMapper fnMapper;
    private VariableMapper varMapper;
    private transient Node node;

    public ValueExpressionImpl() {
    }

    public ValueExpressionImpl(String expr, Node node, FunctionMapper fnMapper, VariableMapper varMapper, Class<?> expectedType) {
        this.expr = expr;
        this.node = node;
        this.fnMapper = fnMapper;
        this.varMapper = varMapper;
        this.expectedType = expectedType;
    }

    @Override // javax.el.Expression
    public boolean equals(Object obj) {
        if (obj instanceof ValueExpressionImpl) {
            ValueExpressionImpl valueExpressionImpl = (ValueExpressionImpl) obj;
            return getNode().equals(valueExpressionImpl.getNode());
        }
        return false;
    }

    @Override // javax.el.ValueExpression
    public Class<?> getExpectedType() {
        return this.expectedType;
    }

    @Override // javax.el.Expression
    public String getExpressionString() {
        return this.expr;
    }

    private Node getNode() throws ELException {
        if (this.node == null) {
            this.node = ExpressionBuilder.createNode(this.expr);
        }
        return this.node;
    }

    @Override // javax.el.ValueExpression
    public Class<?> getType(ELContext context) throws PropertyNotFoundException, ELException {
        return getNode().getType(new EvaluationContext(context, this.fnMapper, this.varMapper));
    }

    @Override // javax.el.ValueExpression
    public ValueReference getValueReference(ELContext context) throws PropertyNotFoundException, ELException {
        return getNode().getValueReference(new EvaluationContext(context, this.fnMapper, this.varMapper));
    }

    @Override // javax.el.ValueExpression
    public Object getValue(ELContext context) throws PropertyNotFoundException, ELException {
        EvaluationContext ctx = new EvaluationContext(context, this.fnMapper, this.varMapper);
        ctx.notifyBeforeEvaluation(this.expr);
        Object value = getNode().getValue(ctx);
        if (this.expectedType != null) {
            try {
                value = ctx.convertToType(value, this.expectedType);
            } catch (IllegalArgumentException ex) {
                throw new ELException(ex);
            }
        }
        ctx.notifyAfterEvaluation(this.expr);
        return value;
    }

    @Override // javax.el.Expression
    public int hashCode() {
        return getNode().hashCode();
    }

    @Override // javax.el.Expression
    public boolean isLiteralText() {
        try {
            return getNode() instanceof AstLiteralExpression;
        } catch (ELException e) {
            return false;
        }
    }

    @Override // javax.el.ValueExpression
    public boolean isReadOnly(ELContext context) throws PropertyNotFoundException, ELException {
        return getNode().isReadOnly(new EvaluationContext(context, this.fnMapper, this.varMapper));
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.expr = in.readUTF();
        String type = in.readUTF();
        if (!"".equals(type)) {
            this.expectedType = ReflectionUtil.forName(type);
        }
        this.fnMapper = (FunctionMapper) in.readObject();
        this.varMapper = (VariableMapper) in.readObject();
    }

    @Override // javax.el.ValueExpression
    public void setValue(ELContext context, Object value) throws PropertyNotFoundException, PropertyNotWritableException, ELException {
        getNode().setValue(new EvaluationContext(context, this.fnMapper, this.varMapper), value);
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.expr);
        out.writeUTF(this.expectedType != null ? this.expectedType.getName() : "");
        out.writeObject(this.fnMapper);
        out.writeObject(this.varMapper);
    }

    public String toString() {
        return "ValueExpression[" + this.expr + "]";
    }
}
