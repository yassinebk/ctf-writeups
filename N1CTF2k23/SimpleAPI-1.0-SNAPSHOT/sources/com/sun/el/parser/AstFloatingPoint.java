package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import java.math.BigDecimal;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstFloatingPoint.class */
public final class AstFloatingPoint extends SimpleNode {
    private Number number;

    public AstFloatingPoint(int id) {
        super(id);
    }

    public Number getFloatingPoint() {
        if (this.number == null) {
            try {
                this.number = Double.valueOf(this.image);
            } catch (ArithmeticException e) {
                this.number = new BigDecimal(this.image);
            }
        }
        return this.number;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        return getFloatingPoint();
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        return getFloatingPoint().getClass();
    }
}
