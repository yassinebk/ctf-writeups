package javax.el;

import java.lang.reflect.Method;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/FunctionMapper.class */
public abstract class FunctionMapper {
    public abstract Method resolveFunction(String str, String str2);

    public void mapFunction(String prefix, String localName, Method meth) {
    }
}
