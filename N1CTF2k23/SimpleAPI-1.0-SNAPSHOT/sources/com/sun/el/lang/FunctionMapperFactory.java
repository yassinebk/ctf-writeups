package com.sun.el.lang;

import java.lang.reflect.Method;
import javax.el.FunctionMapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/FunctionMapperFactory.class */
public class FunctionMapperFactory extends FunctionMapper {
    protected FunctionMapperImpl memento = null;
    protected FunctionMapper target;

    public FunctionMapperFactory(FunctionMapper mapper) {
        if (mapper == null) {
            throw new NullPointerException("FunctionMapper target cannot be null");
        }
        this.target = mapper;
    }

    @Override // javax.el.FunctionMapper
    public Method resolveFunction(String prefix, String localName) {
        if (this.memento == null) {
            this.memento = new FunctionMapperImpl();
        }
        Method m = this.target.resolveFunction(prefix, localName);
        if (m != null) {
            this.memento.addFunction(prefix, localName, m);
        }
        return m;
    }

    public FunctionMapper create() {
        return this.memento;
    }
}
