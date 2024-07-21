package org.apache.tomcat;

import java.lang.instrument.ClassFileTransformer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/InstrumentableClassLoader.class */
public interface InstrumentableClassLoader {
    void addTransformer(ClassFileTransformer classFileTransformer);

    void removeTransformer(ClassFileTransformer classFileTransformer);

    ClassLoader copyWithoutTransformers();
}
