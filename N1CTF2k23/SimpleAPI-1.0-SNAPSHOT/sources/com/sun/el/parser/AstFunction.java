package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import com.sun.el.util.MessageFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.el.ELClass;
import javax.el.ELException;
import javax.el.FunctionMapper;
import javax.el.LambdaExpression;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import org.springframework.beans.PropertyAccessor;
import org.springframework.cglib.core.Constants;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstFunction.class */
public final class AstFunction extends SimpleNode {
    protected String localName;
    protected String prefix;

    public AstFunction(int id) {
        super(id);
        this.localName = "";
        this.prefix = "";
    }

    public String getLocalName() {
        return this.localName;
    }

    public String getOutputName() {
        if (this.prefix.length() == 0) {
            return this.localName;
        }
        return this.prefix + ":" + this.localName;
    }

    public String getPrefix() {
        return this.prefix;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        FunctionMapper fnMapper = ctx.getFunctionMapper();
        if (fnMapper == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.null"));
        }
        Method m = fnMapper.resolveFunction(this.prefix, this.localName);
        if (m == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.method", getOutputName()));
        }
        return m.getReturnType();
    }

    private Object findValue(EvaluationContext ctx, String name) {
        ValueExpression expr;
        if (ctx.isLambdaArgument(name)) {
            return ctx.getLambdaArgument(name);
        }
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null && (expr = varMapper.resolveVariable(name)) != null) {
            return expr.getValue(ctx.getELContext());
        }
        ctx.setPropertyResolved(false);
        Object ret = ctx.getELResolver().getValue(ctx, null, name);
        if (ctx.isPropertyResolved()) {
            return ret;
        }
        return null;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        String methodName;
        if (this.prefix.length() == 0) {
            Object val = findValue(ctx, this.localName);
            if (val != null && (val instanceof LambdaExpression)) {
                for (int i = 0; i < this.children.length; i++) {
                    Object[] params = ((AstMethodArguments) this.children[i]).getParameters(ctx);
                    if (!(val instanceof LambdaExpression)) {
                        throw new ELException(MessageFactory.get("error.function.syntax", getOutputName()));
                    }
                    val = ((LambdaExpression) val).invoke(ctx, params);
                }
                return val;
            }
        }
        FunctionMapper fnMapper = ctx.getFunctionMapper();
        Method m = null;
        if (fnMapper != null) {
            m = fnMapper.resolveFunction(this.prefix, this.localName);
        }
        if (m == null) {
            if (this.prefix.length() == 0 && ctx.getImportHandler() != null) {
                Class<?> c = ctx.getImportHandler().resolveClass(this.localName);
                if (c != null) {
                    methodName = Constants.CONSTRUCTOR_NAME;
                } else {
                    c = ctx.getImportHandler().resolveStatic(this.localName);
                    methodName = this.localName;
                }
                if (c != null) {
                    return ctx.getELResolver().invoke(ctx, new ELClass(c), methodName, null, ((AstMethodArguments) this.children[0]).getParameters(ctx));
                }
            }
            if (fnMapper == null) {
                throw new ELException(MessageFactory.get("error.fnMapper.null"));
            }
            throw new ELException(MessageFactory.get("error.fnMapper.method", getOutputName()));
        }
        Class[] paramTypes = m.getParameterTypes();
        Object[] params2 = ((AstMethodArguments) this.children[0]).getParameters(ctx);
        for (int i2 = 0; i2 < params2.length; i2++) {
            try {
                params2[i2] = ctx.convertToType(params2[i2], paramTypes[i2]);
            } catch (ELException ele) {
                throw new ELException(MessageFactory.get("error.function", getOutputName()), ele);
            }
        }
        try {
            Object result = m.invoke(null, params2);
            return result;
        } catch (IllegalAccessException iae) {
            throw new ELException(MessageFactory.get("error.function", getOutputName()), iae);
        } catch (InvocationTargetException ite) {
            throw new ELException(MessageFactory.get("error.function", getOutputName()), ite.getCause());
        }
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override // com.sun.el.parser.SimpleNode
    public String toString() {
        return ELParserTreeConstants.jjtNodeName[this.id] + PropertyAccessor.PROPERTY_KEY_PREFIX + getOutputName() + "]";
    }
}
