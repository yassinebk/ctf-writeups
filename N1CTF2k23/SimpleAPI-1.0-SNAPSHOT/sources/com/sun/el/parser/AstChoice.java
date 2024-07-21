package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstChoice.class */
public final class AstChoice extends SimpleNode {
    public AstChoice(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        Object obj0 = this.children[0].getValue(ctx);
        Boolean b0 = coerceToBoolean(obj0);
        return this.children[b0.booleanValue() ? (char) 1 : (char) 2].getType(ctx);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        Object obj0 = this.children[0].getValue(ctx);
        Boolean b0 = coerceToBoolean(obj0);
        return this.children[b0.booleanValue() ? (char) 1 : (char) 2].getValue(ctx);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public boolean isReadOnly(EvaluationContext ctx) throws ELException {
        Object obj0 = this.children[0].getValue(ctx);
        Boolean b0 = coerceToBoolean(obj0);
        return this.children[b0.booleanValue() ? (char) 1 : (char) 2].isReadOnly(ctx);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public void setValue(EvaluationContext ctx, Object value) throws ELException {
        Object obj0 = this.children[0].getValue(ctx);
        Boolean b0 = coerceToBoolean(obj0);
        this.children[b0.booleanValue() ? (char) 1 : (char) 2].setValue(ctx, value);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object invoke(EvaluationContext ctx, Class[] paramTypes, Object[] paramValues) throws ELException {
        Object obj0 = this.children[0].getValue(ctx);
        Boolean b0 = coerceToBoolean(obj0);
        return this.children[b0.booleanValue() ? (char) 1 : (char) 2].invoke(ctx, paramTypes, paramValues);
    }
}
