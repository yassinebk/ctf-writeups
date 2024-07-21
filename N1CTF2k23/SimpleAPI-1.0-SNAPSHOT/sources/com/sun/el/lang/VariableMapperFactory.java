package com.sun.el.lang;

import javax.el.ValueExpression;
import javax.el.VariableMapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/VariableMapperFactory.class */
public class VariableMapperFactory extends VariableMapper {
    private final VariableMapper target;
    private VariableMapper momento;

    public VariableMapperFactory(VariableMapper target) {
        if (target == null) {
            throw new NullPointerException("Target VariableMapper cannot be null");
        }
        this.target = target;
    }

    public VariableMapper create() {
        return this.momento;
    }

    @Override // javax.el.VariableMapper
    public ValueExpression resolveVariable(String variable) {
        ValueExpression expr = this.target.resolveVariable(variable);
        if (expr != null) {
            if (this.momento == null) {
                this.momento = new VariableMapperImpl();
            }
            this.momento.setVariable(variable, expr);
        }
        return expr;
    }

    @Override // javax.el.VariableMapper
    public ValueExpression setVariable(String variable, ValueExpression expression) {
        throw new UnsupportedOperationException("Cannot Set Variables on Factory");
    }
}
