package org.apache.tomcat.util.bcel.classfile;

import org.springframework.beans.PropertyAccessor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/bcel/classfile/ArrayElementValue.class */
public class ArrayElementValue extends ElementValue {
    private final ElementValue[] evalues;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayElementValue(int type, ElementValue[] datums, ConstantPool cpool) {
        super(type, cpool);
        if (type != 91) {
            throw new RuntimeException("Only element values of type array can be built with this ctor - type specified: " + type);
        }
        this.evalues = datums;
    }

    @Override // org.apache.tomcat.util.bcel.classfile.ElementValue
    public String stringifyValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
        for (int i = 0; i < this.evalues.length; i++) {
            sb.append(this.evalues[i].stringifyValue());
            if (i + 1 < this.evalues.length) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public ElementValue[] getElementValuesArray() {
        return this.evalues;
    }
}
