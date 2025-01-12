package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.SchemaManagement;
import org.springframework.boot.jdbc.SchemaManagementProvider;
import org.springframework.boot.logging.LoggingSystem;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/HibernateDefaultDdlAutoProvider.class */
class HibernateDefaultDdlAutoProvider implements SchemaManagementProvider {
    private final Iterable<SchemaManagementProvider> providers;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HibernateDefaultDdlAutoProvider(Iterable<SchemaManagementProvider> providers) {
        this.providers = providers;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDefaultDdlAuto(DataSource dataSource) {
        if (!EmbeddedDatabaseConnection.isEmbedded(dataSource)) {
            return LoggingSystem.NONE;
        }
        SchemaManagement schemaManagement = getSchemaManagement(dataSource);
        if (SchemaManagement.MANAGED.equals(schemaManagement)) {
            return LoggingSystem.NONE;
        }
        return "create-drop";
    }

    @Override // org.springframework.boot.jdbc.SchemaManagementProvider
    public SchemaManagement getSchemaManagement(DataSource dataSource) {
        Stream map = StreamSupport.stream(this.providers.spliterator(), false).map(provider -> {
            return provider.getSchemaManagement(dataSource);
        });
        SchemaManagement schemaManagement = SchemaManagement.MANAGED;
        schemaManagement.getClass();
        return (SchemaManagement) map.filter((v1) -> {
            return r1.equals(v1);
        }).findFirst().orElse(SchemaManagement.UNMANAGED);
    }
}
