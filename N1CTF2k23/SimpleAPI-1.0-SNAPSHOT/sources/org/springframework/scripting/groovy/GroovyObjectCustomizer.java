package org.springframework.scripting.groovy;

import groovy.lang.GroovyObject;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scripting/groovy/GroovyObjectCustomizer.class */
public interface GroovyObjectCustomizer {
    void customize(GroovyObject groovyObject);
}
