package org.springframework.boot.context.annotation;

import java.util.Set;
import org.springframework.core.type.AnnotationMetadata;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/annotation/DeterminableImports.class */
public interface DeterminableImports {
    Set<Object> determineImports(AnnotationMetadata metadata);
}
