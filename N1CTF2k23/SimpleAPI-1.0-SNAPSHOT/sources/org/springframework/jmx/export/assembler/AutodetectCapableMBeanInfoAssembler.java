package org.springframework.jmx.export.assembler;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/assembler/AutodetectCapableMBeanInfoAssembler.class */
public interface AutodetectCapableMBeanInfoAssembler extends MBeanInfoAssembler {
    boolean includeBean(Class<?> cls, String str);
}
