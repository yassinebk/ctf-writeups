package org.springframework.expression.spel.ast;

import java.util.StringJoiner;
import org.springframework.asm.MethodVisitor;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.expression.spel.ExpressionState;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/ast/CompoundExpression.class */
public class CompoundExpression extends SpelNodeImpl {
    public CompoundExpression(int startPos, int endPos, SpelNodeImpl... expressionComponents) {
        super(startPos, endPos, expressionComponents);
        if (expressionComponents.length < 2) {
            throw new IllegalStateException("Do not build compound expressions with less than two entries: " + expressionComponents.length);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public ValueRef getValueRef(ExpressionState state) throws EvaluationException {
        if (getChildCount() == 1) {
            return this.children[0].getValueRef(state);
        }
        SpelNodeImpl nextNode = this.children[0];
        try {
            TypedValue result = nextNode.getValueInternal(state);
            int cc = getChildCount();
            for (int i = 1; i < cc - 1; i++) {
                state.pushActiveContextObject(result);
                result = this.children[i].getValueInternal(state);
                state.popActiveContextObject();
            }
            state.pushActiveContextObject(result);
            nextNode = this.children[cc - 1];
            ValueRef valueRef = nextNode.getValueRef(state);
            state.popActiveContextObject();
            return valueRef;
        } catch (SpelEvaluationException ex) {
            ex.setPosition(nextNode.getStartPosition());
            throw ex;
        }
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        ValueRef ref = getValueRef(state);
        TypedValue result = ref.getValue();
        this.exitTypeDescriptor = this.children[this.children.length - 1].exitTypeDescriptor;
        return result;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl, org.springframework.expression.spel.SpelNode
    public void setValue(ExpressionState state, @Nullable Object value) throws EvaluationException {
        getValueRef(state).setValue(value);
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl, org.springframework.expression.spel.SpelNode
    public boolean isWritable(ExpressionState state) throws EvaluationException {
        return getValueRef(state).isWritable();
    }

    @Override // org.springframework.expression.spel.SpelNode
    public String toStringAST() {
        StringJoiner sj = new StringJoiner(".");
        for (int i = 0; i < getChildCount(); i++) {
            sj.add(getChild(i).toStringAST());
        }
        return sj.toString();
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public boolean isCompilable() {
        SpelNodeImpl[] spelNodeImplArr;
        for (SpelNodeImpl child : this.children) {
            if (!child.isCompilable()) {
                return false;
            }
        }
        return true;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        SpelNodeImpl[] spelNodeImplArr;
        for (SpelNodeImpl child : this.children) {
            child.generateCode(mv, cf);
        }
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
