package org.springframework.cglib.core;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/core/GeneratorStrategy.class */
public interface GeneratorStrategy {
    byte[] generate(ClassGenerator classGenerator) throws Exception;

    boolean equals(Object obj);
}
