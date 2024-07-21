package org.springframework.expression.spel.ast;

import org.springframework.asm.MethodVisitor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.CodeFlow;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/ast/FloatLiteral.class */
public class FloatLiteral extends Literal {
    private final TypedValue value;

    public FloatLiteral(String payload, int startPos, int endPos, float value) {
        super(payload, startPos, endPos);
        this.value = new TypedValue(Float.valueOf(value));
        this.exitTypeDescriptor = "F";
    }

    @Override // org.springframework.expression.spel.ast.Literal
    public TypedValue getLiteralValue() {
        return this.value;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public boolean isCompilable() {
        return true;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        mv.visitLdcInsn(this.value.getValue());
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
