package org.springframework.expression.spel.ast;

import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.ExpressionState;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/ast/QualifiedIdentifier.class */
public class QualifiedIdentifier extends SpelNodeImpl {
    @Nullable
    private TypedValue value;

    public QualifiedIdentifier(int startPos, int endPos, SpelNodeImpl... operands) {
        super(startPos, endPos, operands);
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        if (this.value == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < getChildCount(); i++) {
                Object value = this.children[i].getValueInternal(state).getValue();
                if (i > 0 && (value == null || !value.toString().startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX))) {
                    sb.append(".");
                }
                sb.append(value);
            }
            this.value = new TypedValue(sb.toString());
        }
        return this.value;
    }

    @Override // org.springframework.expression.spel.SpelNode
    public String toStringAST() {
        StringBuilder sb = new StringBuilder();
        if (this.value != null) {
            sb.append(this.value.getValue());
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                if (i > 0) {
                    sb.append(".");
                }
                sb.append(getChild(i).toStringAST());
            }
        }
        return sb.toString();
    }
}
