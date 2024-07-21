package org.apache.tomcat.util.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/bcel/classfile/ConstantClass.class */
public final class ConstantClass extends Constant {
    private final int name_index;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConstantClass(DataInput dataInput) throws IOException {
        super((byte) 7);
        this.name_index = dataInput.readUnsignedShort();
    }

    public int getNameIndex() {
        return this.name_index;
    }
}
