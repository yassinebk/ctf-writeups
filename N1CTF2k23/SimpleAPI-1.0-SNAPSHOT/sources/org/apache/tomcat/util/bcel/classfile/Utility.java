package org.apache.tomcat.util.bcel.classfile;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/bcel/classfile/Utility.class */
public final class Utility {
    private Utility() {
    }

    static String compactClassName(String str) {
        return str.replace('/', '.');
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getClassName(ConstantPool constant_pool, int index) {
        Constant c = constant_pool.getConstant(index, (byte) 7);
        int i = ((ConstantClass) c).getNameIndex();
        Constant c2 = constant_pool.getConstant(i, (byte) 1);
        String name = ((ConstantUtf8) c2).getBytes();
        return compactClassName(name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void skipFully(DataInput file, int length) throws IOException {
        int total = file.skipBytes(length);
        if (total != length) {
            throw new EOFException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void swallowFieldOrMethod(DataInput file) throws IOException {
        skipFully(file, 6);
        int attributes_count = file.readUnsignedShort();
        for (int i = 0; i < attributes_count; i++) {
            swallowAttribute(file);
        }
    }

    static void swallowAttribute(DataInput file) throws IOException {
        skipFully(file, 2);
        int length = file.readInt();
        skipFully(file, length);
    }
}
