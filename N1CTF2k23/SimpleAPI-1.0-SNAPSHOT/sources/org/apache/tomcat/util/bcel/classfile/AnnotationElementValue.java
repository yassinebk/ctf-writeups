package org.apache.tomcat.util.bcel.classfile;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/bcel/classfile/AnnotationElementValue.class */
public class AnnotationElementValue extends ElementValue {
    private final AnnotationEntry annotationEntry;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnnotationElementValue(int type, AnnotationEntry annotationEntry, ConstantPool cpool) {
        super(type, cpool);
        if (type != 64) {
            throw new RuntimeException("Only element values of type annotation can be built with this ctor - type specified: " + type);
        }
        this.annotationEntry = annotationEntry;
    }

    @Override // org.apache.tomcat.util.bcel.classfile.ElementValue
    public String stringifyValue() {
        return this.annotationEntry.toString();
    }

    public AnnotationEntry getAnnotationEntry() {
        return this.annotationEntry;
    }
}
