package org.apache.tomcat.util.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/bcel/classfile/ConstantDouble.class */
public final class ConstantDouble extends Constant {
    private final double bytes;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConstantDouble(DataInput file) throws IOException {
        super((byte) 6);
        this.bytes = file.readDouble();
    }

    public double getBytes() {
        return this.bytes;
    }
}
