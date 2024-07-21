package org.springframework.boot.diagnostics.analyzer;

import javax.validation.ValidationException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/diagnostics/analyzer/ValidationExceptionFailureAnalyzer.class */
class ValidationExceptionFailureAnalyzer extends AbstractFailureAnalyzer<ValidationException> {
    private static final String JAVAX_MISSING_IMPLEMENTATION_MESSAGE = "Unable to create a Configuration, because no Bean Validation provider could be found";
    private static final String JAKARTA_MISSING_IMPLEMENTATION_MESSAGE = "Unable to create a Configuration, because no Jakarta Bean Validation provider could be found";

    ValidationExceptionFailureAnalyzer() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.diagnostics.AbstractFailureAnalyzer
    public FailureAnalysis analyze(Throwable rootFailure, ValidationException cause) {
        if (cause.getMessage().startsWith(JAVAX_MISSING_IMPLEMENTATION_MESSAGE) || cause.getMessage().startsWith(JAKARTA_MISSING_IMPLEMENTATION_MESSAGE)) {
            return new FailureAnalysis("The Bean Validation API is on the classpath but no implementation could be found", "Add an implementation, such as Hibernate Validator, to the classpath", cause);
        }
        return null;
    }
}
