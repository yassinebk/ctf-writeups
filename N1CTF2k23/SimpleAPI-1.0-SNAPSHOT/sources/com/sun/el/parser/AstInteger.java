package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import java.math.BigInteger;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstInteger.class */
public final class AstInteger extends SimpleNode {
    private Number number;

    public AstInteger(int id) {
        super(id);
    }

    protected Number getInteger() {
        if (this.number == null) {
            try {
                this.number = Long.valueOf(this.image);
            } catch (ArithmeticException e) {
                this.number = new BigInteger(this.image);
            }
        }
        return this.number;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        return getInteger().getClass();
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        return getInteger();
    }
}
