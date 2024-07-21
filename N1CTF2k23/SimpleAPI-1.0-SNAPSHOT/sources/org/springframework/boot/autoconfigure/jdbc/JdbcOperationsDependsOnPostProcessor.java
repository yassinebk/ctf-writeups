package org.springframework.boot.autoconfigure.jdbc;

import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.jdbc.core.JdbcOperations;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/JdbcOperationsDependsOnPostProcessor.class */
public class JdbcOperationsDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
    public JdbcOperationsDependsOnPostProcessor(String... dependsOn) {
        super(JdbcOperations.class, dependsOn);
    }

    public JdbcOperationsDependsOnPostProcessor(Class<?>... dependsOn) {
        super(JdbcOperations.class, dependsOn);
    }
}
