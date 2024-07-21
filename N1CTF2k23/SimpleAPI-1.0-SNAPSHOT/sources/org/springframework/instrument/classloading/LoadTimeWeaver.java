package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/instrument/classloading/LoadTimeWeaver.class */
public interface LoadTimeWeaver {
    void addTransformer(ClassFileTransformer classFileTransformer);

    ClassLoader getInstrumentableClassLoader();

    ClassLoader getThrowawayClassLoader();
}
