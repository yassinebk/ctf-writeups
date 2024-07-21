package org.springframework.expression.spel.ast;

import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.ExpressionState;
import org.springframework.expression.spel.InternalParseException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/ast/Literal.class */
public abstract class Literal extends SpelNodeImpl {
    @Nullable
    private final String originalValue;

    public abstract TypedValue getLiteralValue();

    public Literal(@Nullable String originalValue, int startPos, int endPos) {
        super(startPos, endPos, new SpelNodeImpl[0]);
        this.originalValue = originalValue;
    }

    @Nullable
    public final String getOriginalValue() {
        return this.originalValue;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public final TypedValue getValueInternal(ExpressionState state) throws SpelEvaluationException {
        return getLiteralValue();
    }

    public String toString() {
        return String.valueOf(getLiteralValue().getValue());
    }

    @Override // org.springframework.expression.spel.SpelNode
    public String toStringAST() {
        return toString();
    }

    public static Literal getIntLiteral(String numberToken, int startPos, int endPos, int radix) {
        try {
            int value = Integer.parseInt(numberToken, radix);
            return new IntLiteral(numberToken, startPos, endPos, value);
        } catch (NumberFormatException ex) {
            throw new InternalParseException(new SpelParseException(startPos, ex, SpelMessage.NOT_AN_INTEGER, numberToken));
        }
    }

    public static Literal getLongLiteral(String numberToken, int startPos, int endPos, int radix) {
        try {
            long value = Long.parseLong(numberToken, radix);
            return new LongLiteral(numberToken, startPos, endPos, value);
        } catch (NumberFormatException ex) {
            throw new InternalParseException(new SpelParseException(startPos, ex, SpelMessage.NOT_A_LONG, numberToken));
        }
    }

    public static Literal getRealLiteral(String numberToken, int startPos, int endPos, boolean isFloat) {
        try {
            if (isFloat) {
                float value = Float.parseFloat(numberToken);
                return new FloatLiteral(numberToken, startPos, endPos, value);
            }
            double value2 = Double.parseDouble(numberToken);
            return new RealLiteral(numberToken, startPos, endPos, value2);
        } catch (NumberFormatException ex) {
            throw new InternalParseException(new SpelParseException(startPos, ex, SpelMessage.NOT_A_REAL, numberToken));
        }
    }
}
