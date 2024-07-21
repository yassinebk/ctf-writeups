package org.springframework.expression.spel.ast;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.ExpressionState;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/ast/Assign.class */
public class Assign extends SpelNodeImpl {
    public Assign(int startPos, int endPos, SpelNodeImpl... operands) {
        super(startPos, endPos, operands);
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        TypedValue newValue = this.children[1].getValueInternal(state);
        getChild(0).setValue(state, newValue.getValue());
        return newValue;
    }

    @Override // org.springframework.expression.spel.SpelNode
    public String toStringAST() {
        return getChild(0).toStringAST() + "=" + getChild(1).toStringAST();
    }
}
