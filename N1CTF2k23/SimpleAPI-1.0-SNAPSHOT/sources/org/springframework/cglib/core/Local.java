package org.springframework.cglib.core;

import org.springframework.asm.Type;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/core/Local.class */
public class Local {
    private Type type;
    private int index;

    public Local(int index, Type type) {
        this.type = type;
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public Type getType() {
        return this.type;
    }
}
