package org.springframework.boot.autoconfigure.flyway;

import java.util.ArrayList;
import java.util.List;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayMigrationScriptMissingException.class */
public class FlywayMigrationScriptMissingException extends RuntimeException {
    private final List<String> locations;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FlywayMigrationScriptMissingException(List<String> locations) {
        super(locations.isEmpty() ? "Migration script locations not configured" : "Cannot find migration scripts in: " + locations + " (please add migration scripts or check your Flyway configuration)");
        this.locations = new ArrayList(locations);
    }

    public List<String> getLocations() {
        return this.locations;
    }
}
