package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/MethodInfo.class */
public class MethodInfo {
    private String name;
    private Class<?> returnType;
    private Class<?>[] paramTypes;

    public MethodInfo(String name, Class<?> returnType, Class<?>[] paramTypes) {
        this.name = name;
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getReturnType() {
        return this.returnType;
    }

    public Class<?>[] getParamTypes() {
        return this.paramTypes;
    }
}
