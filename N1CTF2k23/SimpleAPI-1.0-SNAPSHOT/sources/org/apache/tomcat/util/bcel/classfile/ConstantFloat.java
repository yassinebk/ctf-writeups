package org.apache.tomcat.util.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/bcel/classfile/ConstantFloat.class */
public final class ConstantFloat extends Constant {
    private final float bytes;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConstantFloat(DataInput file) throws IOException {
        super((byte) 4);
        this.bytes = file.readFloat();
    }

    public float getBytes() {
        return this.bytes;
    }
}
