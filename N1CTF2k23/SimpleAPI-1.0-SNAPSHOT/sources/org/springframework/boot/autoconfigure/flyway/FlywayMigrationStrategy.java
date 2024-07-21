package org.springframework.boot.autoconfigure.flyway;

import org.flywaydb.core.Flyway;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayMigrationStrategy.class */
public interface FlywayMigrationStrategy {
    void migrate(Flyway flyway);
}
