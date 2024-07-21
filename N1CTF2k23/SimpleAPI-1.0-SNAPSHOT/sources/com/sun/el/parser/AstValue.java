package com.sun.el.parser;

import com.sun.el.lang.ELSupport;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.util.MessageFactory;
import com.sun.el.util.ReflectionUtil;
import java.lang.reflect.Method;
import javax.el.ELClass;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ImportHandler;
import javax.el.MethodInfo;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.el.ValueReference;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstValue.class */
public final class AstValue extends SimpleNode {

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstValue$Target.class */
    public static class Target {
        protected Object base;
        protected Node suffixNode;

        Target(Object base, Node suffixNode) {
            this.base = base;
            this.suffixNode = suffixNode;
        }

        boolean isMethodCall() {
            return AstValue.getArguments(this.suffixNode) != null;
        }
    }

    public AstValue(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            return null;
        }
        Object property = t.suffixNode.getValue(ctx);
        ctx.setPropertyResolved(false);
        Class ret = ctx.getELResolver().getType(ctx, t.base, property);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(t.base, property);
        }
        return ret;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public ValueReference getValueReference(EvaluationContext ctx) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            return null;
        }
        Object property = t.suffixNode.getValue(ctx);
        return new ValueReference(t.base, property);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AstMethodArguments getArguments(Node n) {
        if ((n instanceof AstDotSuffix) && n.jjtGetNumChildren() > 0) {
            return (AstMethodArguments) n.jjtGetChild(0);
        }
        if ((n instanceof AstBracketSuffix) && n.jjtGetNumChildren() > 1) {
            return (AstMethodArguments) n.jjtGetChild(1);
        }
        return null;
    }

    private Object getValue(Object base, Node child, EvaluationContext ctx) throws ELException {
        Object value = null;
        ELResolver resolver = ctx.getELResolver();
        Object property = child.getValue(ctx);
        AstMethodArguments args = getArguments(child);
        if (args != null) {
            if (!(property instanceof String)) {
                throw new ELException(MessageFactory.get("error.method.name", property));
            }
            Class<?>[] paramTypes = args.getParamTypes();
            Object[] params = args.getParameters(ctx);
            ctx.setPropertyResolved(false);
            value = resolver.invoke(ctx, base, property, paramTypes, params);
        } else if (property != null) {
            ctx.setPropertyResolved(false);
            value = resolver.getValue(ctx, base, property);
            if (!ctx.isPropertyResolved()) {
                ELSupport.throwUnhandled(base, property);
            }
        }
        return value;
    }

    private Object getBase(EvaluationContext ctx) {
        Class<?> c;
        try {
            return this.children[0].getValue(ctx);
        } catch (PropertyNotFoundException ex) {
            if (this.children[0] instanceof AstIdentifier) {
                String name = ((AstIdentifier) this.children[0]).image;
                ImportHandler importHandler = ctx.getImportHandler();
                if (importHandler != null && (c = importHandler.resolveClass(name)) != null) {
                    return new ELClass(c);
                }
            }
            throw ex;
        }
    }

    private Target getTarget(EvaluationContext ctx) throws ELException {
        Object base = getBase(ctx);
        if (base == null) {
            throw new PropertyNotFoundException(MessageFactory.get("error.unreachable.base", this.children[0].getImage()));
        }
        int propCount = jjtGetNumChildren() - 1;
        if (propCount > 1) {
            for (int i = 1; base != null && i < propCount; i++) {
                base = getValue(base, this.children[i], ctx);
            }
            if (base == null) {
                throw new PropertyNotFoundException(MessageFactory.get("error.unreachable.property", null));
            }
        }
        return new Target(base, this.children[propCount]);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        Object base = getBase(ctx);
        int propCount = jjtGetNumChildren();
        for (int i = 1; base != null && i < propCount; i++) {
            base = getValue(base, this.children[i], ctx);
        }
        return base;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public boolean isReadOnly(EvaluationContext ctx) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            return true;
        }
        Object property = t.suffixNode.getValue(ctx);
        ctx.setPropertyResolved(false);
        boolean ret = ctx.getELResolver().isReadOnly(ctx, t.base, property);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(t.base, property);
        }
        return ret;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public void setValue(EvaluationContext ctx, Object value) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            throw new PropertyNotWritableException(MessageFactory.get("error.syntax.set"));
        }
        Object property = t.suffixNode.getValue(ctx);
        ELResolver elResolver = ctx.getELResolver();
        ctx.setPropertyResolved(false);
        Class<?> targetType = elResolver.getType(ctx, t.base, property);
        if (ctx.isPropertyResolved()) {
            ctx.setPropertyResolved(false);
            Object targetValue = elResolver.convertToType(ctx, value, targetType);
            if (ctx.isPropertyResolved()) {
                value = targetValue;
            } else if (value != null || targetType.isPrimitive()) {
                value = ELSupport.coerceToType(value, targetType);
            }
        }
        ctx.setPropertyResolved(false);
        elResolver.setValue(ctx, t.base, property, value);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(t.base, property);
        }
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public MethodInfo getMethodInfo(EvaluationContext ctx, Class[] paramTypes) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            return null;
        }
        Object property = t.suffixNode.getValue(ctx);
        Method m = ReflectionUtil.findMethod(t.base.getClass(), property.toString(), paramTypes, null);
        return new MethodInfo(m.getName(), m.getReturnType(), m.getParameterTypes());
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object invoke(EvaluationContext ctx, Class[] paramTypes, Object[] paramValues) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            AstMethodArguments args = getArguments(t.suffixNode);
            Class[] paramTypes2 = args.getParamTypes();
            Object[] params = args.getParameters(ctx);
            String method = (String) t.suffixNode.getValue(ctx);
            ctx.setPropertyResolved(false);
            ELResolver resolver = ctx.getELResolver();
            return resolver.invoke(ctx, t.base, method, paramTypes2, params);
        }
        Object property = t.suffixNode.getValue(ctx);
        Method m = ReflectionUtil.findMethod(t.base.getClass(), property.toString(), paramTypes, paramValues);
        return ReflectionUtil.invokeMethod(ctx, m, t.base, paramValues);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public boolean isParametersProvided() {
        return getArguments(this.children[jjtGetNumChildren() - 1]) != null;
    }
}
