package org.springframework.boot.autoconfigure.flyway;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayMigrationScriptMissingFailureAnalyzer.class */
class FlywayMigrationScriptMissingFailureAnalyzer extends AbstractFailureAnalyzer<FlywayMigrationScriptMissingException> {
    FlywayMigrationScriptMissingFailureAnalyzer() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.diagnostics.AbstractFailureAnalyzer
    public FailureAnalysis analyze(Throwable rootFailure, FlywayMigrationScriptMissingException cause) {
        StringBuilder description = new StringBuilder("Flyway failed to initialize: ");
        if (cause.getLocations().isEmpty()) {
            return new FailureAnalysis(description.append("no migration scripts location is configured").toString(), "Check your Flyway configuration", cause);
        }
        description.append(String.format("none of the following migration scripts locations could be found:%n%n", new Object[0]));
        cause.getLocations().forEach(location -> {
            description.append(String.format("\t- %s%n", location));
        });
        return new FailureAnalysis(description.toString(), "Review the locations above or check your Flyway configuration", cause);
    }
}
