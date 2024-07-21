package org.springframework.boot.diagnostics;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/diagnostics/FailureAnalysisReporter.class */
public interface FailureAnalysisReporter {
    void report(FailureAnalysis analysis);
}
