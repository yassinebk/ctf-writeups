package org.springframework.asm;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/asm/ClassTooLargeException.class */
public final class ClassTooLargeException extends IndexOutOfBoundsException {
    private static final long serialVersionUID = 160715609518896765L;
    private final String className;
    private final int constantPoolCount;

    public ClassTooLargeException(String className, int constantPoolCount) {
        super("Class too large: " + className);
        this.className = className;
        this.constantPoolCount = constantPoolCount;
    }

    public String getClassName() {
        return this.className;
    }

    public int getConstantPoolCount() {
        return this.constantPoolCount;
    }
}
