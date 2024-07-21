package com.sun.el;

import com.sun.el.lang.EvaluationContext;
import com.sun.el.lang.ExpressionBuilder;
import com.sun.el.parser.Node;
import com.sun.el.util.ReflectionUtil;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.FunctionMapper;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.MethodNotFoundException;
import javax.el.PropertyNotFoundException;
import javax.el.VariableMapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/MethodExpressionImpl.class */
public final class MethodExpressionImpl extends MethodExpression implements Externalizable {
    private Class<?> expectedType;
    private String expr;
    private FunctionMapper fnMapper;
    private VariableMapper varMapper;
    private Class<?>[] paramTypes;
    private transient Node node;

    public MethodExpressionImpl() {
    }

    public MethodExpressionImpl(String expr, Node node, FunctionMapper fnMapper, VariableMapper varMapper, Class<?> expectedType, Class<?>[] paramTypes) {
        this.expr = expr;
        this.node = node;
        this.fnMapper = fnMapper;
        this.varMapper = varMapper;
        this.expectedType = expectedType;
        this.paramTypes = paramTypes;
    }

    @Override // javax.el.Expression
    public boolean equals(Object obj) {
        if (obj instanceof MethodExpressionImpl) {
            MethodExpressionImpl methodExpressionImpl = (MethodExpressionImpl) obj;
            return getNode().equals(methodExpressionImpl.getNode());
        }
        return false;
    }

    @Override // javax.el.Expression
    public String getExpressionString() {
        return this.expr;
    }

    @Override // javax.el.MethodExpression
    public MethodInfo getMethodInfo(ELContext context) throws PropertyNotFoundException, MethodNotFoundException, ELException {
        return getNode().getMethodInfo(new EvaluationContext(context, this.fnMapper, this.varMapper), this.paramTypes);
    }

    private Node getNode() throws ELException {
        if (this.node == null) {
            this.node = ExpressionBuilder.createNode(this.expr);
        }
        return this.node;
    }

    @Override // javax.el.Expression
    public int hashCode() {
        return getNode().hashCode();
    }

    @Override // javax.el.MethodExpression
    public Object invoke(ELContext context, Object[] params) throws PropertyNotFoundException, MethodNotFoundException, ELException {
        EvaluationContext ctx = new EvaluationContext(context, this.fnMapper, this.varMapper);
        ctx.notifyBeforeEvaluation(this.expr);
        Object obj = getNode().invoke(ctx, this.paramTypes, params);
        ctx.notifyAfterEvaluation(this.expr);
        return obj;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.expr = in.readUTF();
        String type = in.readUTF();
        if (!"".equals(type)) {
            this.expectedType = ReflectionUtil.forName(type);
        }
        this.paramTypes = ReflectionUtil.toTypeArray((String[]) in.readObject());
        this.fnMapper = (FunctionMapper) in.readObject();
        this.varMapper = (VariableMapper) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.expr);
        out.writeUTF(this.expectedType != null ? this.expectedType.getName() : "");
        out.writeObject(ReflectionUtil.toTypeNameArray(this.paramTypes));
        out.writeObject(this.fnMapper);
        out.writeObject(this.varMapper);
    }

    @Override // javax.el.Expression
    public boolean isLiteralText() {
        return false;
    }

    @Override // javax.el.MethodExpression
    public boolean isParametersProvided() {
        return getNode().isParametersProvided();
    }
}
