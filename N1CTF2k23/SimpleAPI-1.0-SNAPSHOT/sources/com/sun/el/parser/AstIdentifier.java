package com.sun.el.parser;

import com.sun.el.lang.ELSupport;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.util.MessageFactory;
import javax.el.ELClass;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.MethodNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
import javax.el.ValueReference;
import javax.el.VariableMapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstIdentifier.class */
public final class AstIdentifier extends SimpleNode {
    public AstIdentifier(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        ValueExpression expr;
        if (ctx.isLambdaArgument(this.image)) {
            return Object.class;
        }
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null && (expr = varMapper.resolveVariable(this.image)) != null) {
            return expr.getType(ctx.getELContext());
        }
        ctx.setPropertyResolved(false);
        Class ret = ctx.getELResolver().getType(ctx, null, this.image);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(null, this.image);
        }
        return ret;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public ValueReference getValueReference(EvaluationContext ctx) throws ELException {
        ValueExpression expr;
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null && (expr = varMapper.resolveVariable(this.image)) != null) {
            return expr.getValueReference(ctx.getELContext());
        }
        return new ValueReference(null, this.image);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        Class<?> c;
        ValueExpression expr;
        if (ctx.isLambdaArgument(this.image)) {
            return ctx.getLambdaArgument(this.image);
        }
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null && (expr = varMapper.resolveVariable(this.image)) != null) {
            return expr.getValue(ctx.getELContext());
        }
        ctx.setPropertyResolved(false);
        Object ret = ctx.getELResolver().getValue(ctx, null, this.image);
        if (!ctx.isPropertyResolved()) {
            if (ctx.getImportHandler() != null && (c = ctx.getImportHandler().resolveStatic(this.image)) != null) {
                return ctx.getELResolver().getValue(ctx, new ELClass(c), this.image);
            }
            ELSupport.throwUnhandled(null, this.image);
        }
        return ret;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public boolean isReadOnly(EvaluationContext ctx) throws ELException {
        ValueExpression expr;
        if (ctx.isLambdaArgument(this.image)) {
            return true;
        }
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null && (expr = varMapper.resolveVariable(this.image)) != null) {
            return expr.isReadOnly(ctx.getELContext());
        }
        ctx.setPropertyResolved(false);
        boolean ret = ctx.getELResolver().isReadOnly(ctx, null, this.image);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(null, this.image);
        }
        return ret;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public void setValue(EvaluationContext ctx, Object value) throws ELException {
        ValueExpression expr;
        if (ctx.isLambdaArgument(this.image)) {
            throw new PropertyNotWritableException(MessageFactory.get("error.lambda.parameter.readonly", this.image));
        }
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null && (expr = varMapper.resolveVariable(this.image)) != null) {
            expr.setValue(ctx.getELContext(), value);
            return;
        }
        ctx.setPropertyResolved(false);
        ELResolver elResolver = ctx.getELResolver();
        elResolver.setValue(ctx, null, this.image, value);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(null, this.image);
        }
    }

    private Object invokeTarget(EvaluationContext ctx, Object target, Object[] paramValues) throws ELException {
        if (target instanceof MethodExpression) {
            MethodExpression me = (MethodExpression) target;
            return me.invoke(ctx.getELContext(), paramValues);
        } else if (target == null) {
            throw new MethodNotFoundException("Identity '" + this.image + "' was null and was unable to invoke");
        } else {
            throw new ELException("Identity '" + this.image + "' does not reference a MethodExpression instance, returned type: " + target.getClass().getName());
        }
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object invoke(EvaluationContext ctx, Class[] paramTypes, Object[] paramValues) throws ELException {
        return getMethodExpression(ctx).invoke(ctx.getELContext(), paramValues);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public MethodInfo getMethodInfo(EvaluationContext ctx, Class[] paramTypes) throws ELException {
        return getMethodExpression(ctx).getMethodInfo(ctx.getELContext());
    }

    private MethodExpression getMethodExpression(EvaluationContext ctx) throws ELException {
        Object obj = null;
        VariableMapper varMapper = ctx.getVariableMapper();
        ValueExpression ve = null;
        if (varMapper != null) {
            ve = varMapper.resolveVariable(this.image);
            if (ve != null) {
                obj = ve.getValue(ctx);
            }
        }
        if (ve == null) {
            ctx.setPropertyResolved(false);
            obj = ctx.getELResolver().getValue(ctx, null, this.image);
        }
        if (obj instanceof MethodExpression) {
            return (MethodExpression) obj;
        }
        if (obj == null) {
            throw new MethodNotFoundException("Identity '" + this.image + "' was null and was unable to invoke");
        }
        throw new ELException("Identity '" + this.image + "' does not reference a MethodExpression instance, returned type: " + obj.getClass().getName());
    }
}
