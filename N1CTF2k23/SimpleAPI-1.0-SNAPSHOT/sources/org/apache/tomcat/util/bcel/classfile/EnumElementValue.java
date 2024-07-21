package org.apache.tomcat.util.bcel.classfile;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/bcel/classfile/EnumElementValue.class */
public class EnumElementValue extends ElementValue {
    private final int valueIdx;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EnumElementValue(int type, int valueIdx, ConstantPool cpool) {
        super(type, cpool);
        if (type != 101) {
            throw new RuntimeException("Only element values of type enum can be built with this ctor - type specified: " + type);
        }
        this.valueIdx = valueIdx;
    }

    @Override // org.apache.tomcat.util.bcel.classfile.ElementValue
    public String stringifyValue() {
        ConstantUtf8 cu8 = (ConstantUtf8) super.getConstantPool().getConstant(this.valueIdx, (byte) 1);
        return cu8.getBytes();
    }
}
