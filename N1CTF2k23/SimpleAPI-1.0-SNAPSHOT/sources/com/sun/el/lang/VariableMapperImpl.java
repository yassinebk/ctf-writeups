package com.sun.el.lang;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/VariableMapperImpl.class */
public class VariableMapperImpl extends VariableMapper implements Externalizable {
    private static final long serialVersionUID = 1;
    private Map<String, ValueExpression> vars = new HashMap();

    @Override // javax.el.VariableMapper
    public ValueExpression resolveVariable(String variable) {
        return this.vars.get(variable);
    }

    @Override // javax.el.VariableMapper
    public ValueExpression setVariable(String variable, ValueExpression expression) {
        return this.vars.put(variable, expression);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.vars = (Map) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.vars);
    }
}
