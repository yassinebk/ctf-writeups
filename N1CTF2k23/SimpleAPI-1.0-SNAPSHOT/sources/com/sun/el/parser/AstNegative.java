package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstNegative.class */
public final class AstNegative extends SimpleNode {
    public AstNegative(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        return Number.class;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        Object obj = this.children[0].getValue(ctx);
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).negate();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).negate();
        }
        if (obj instanceof String) {
            if (isStringFloat((String) obj)) {
                return Double.valueOf(-Double.parseDouble((String) obj));
            }
            return Long.valueOf(-Long.parseLong((String) obj));
        }
        Class type = obj.getClass();
        if ((obj instanceof Long) || Long.TYPE == type) {
            return Long.valueOf(-((Long) obj).longValue());
        }
        if ((obj instanceof Double) || Double.TYPE == type) {
            return Double.valueOf(-((Double) obj).doubleValue());
        }
        if ((obj instanceof Integer) || Integer.TYPE == type) {
            return Integer.valueOf(-((Integer) obj).intValue());
        }
        if ((obj instanceof Float) || Float.TYPE == type) {
            return Float.valueOf(-((Float) obj).floatValue());
        }
        if ((obj instanceof Short) || Short.TYPE == type) {
            return Short.valueOf((short) (-((Short) obj).shortValue()));
        }
        if ((obj instanceof Byte) || Byte.TYPE == type) {
            return Byte.valueOf((byte) (-((Byte) obj).byteValue()));
        }
        Long num = (Long) coerceToNumber(obj, Long.class);
        return Long.valueOf(-num.longValue());
    }
}
