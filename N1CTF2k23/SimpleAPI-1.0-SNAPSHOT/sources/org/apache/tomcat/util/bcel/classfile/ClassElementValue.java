package org.apache.tomcat.util.bcel.classfile;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/bcel/classfile/ClassElementValue.class */
public class ClassElementValue extends ElementValue {
    private final int idx;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClassElementValue(int type, int idx, ConstantPool cpool) {
        super(type, cpool);
        this.idx = idx;
    }

    @Override // org.apache.tomcat.util.bcel.classfile.ElementValue
    public String stringifyValue() {
        ConstantUtf8 cu8 = (ConstantUtf8) super.getConstantPool().getConstant(this.idx, (byte) 1);
        return cu8.getBytes();
    }
}
