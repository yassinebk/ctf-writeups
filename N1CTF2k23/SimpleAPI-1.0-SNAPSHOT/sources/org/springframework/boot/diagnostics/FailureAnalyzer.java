package org.springframework.boot.diagnostics;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/diagnostics/FailureAnalyzer.class */
public interface FailureAnalyzer {
    FailureAnalysis analyze(Throwable failure);
}
