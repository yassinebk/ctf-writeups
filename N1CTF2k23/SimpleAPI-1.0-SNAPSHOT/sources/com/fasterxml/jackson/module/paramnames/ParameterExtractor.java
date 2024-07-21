package com.fasterxml.jackson.module.paramnames;

import java.io.Serializable;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-module-parameter-names-2.11.0.jar:com/fasterxml/jackson/module/paramnames/ParameterExtractor.class */
class ParameterExtractor implements Serializable {
    private static final long serialVersionUID = 1;

    public Parameter[] getParameters(Executable executable) {
        return executable.getParameters();
    }
}
