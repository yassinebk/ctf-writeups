package org.springframework.boot.autoconfigure.jdbc;

import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/NamedParameterJdbcOperationsDependsOnPostProcessor.class */
public class NamedParameterJdbcOperationsDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
    public NamedParameterJdbcOperationsDependsOnPostProcessor(String... dependsOn) {
        super(NamedParameterJdbcOperations.class, dependsOn);
    }

    public NamedParameterJdbcOperationsDependsOnPostProcessor(Class<?>... dependsOn) {
        super(NamedParameterJdbcOperations.class, dependsOn);
    }
}
