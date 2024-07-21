package org.springframework.boot.autoconfigure.cassandra;

import com.datastax.oss.driver.api.core.CqlSessionBuilder;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CqlSessionBuilderCustomizer.class */
public interface CqlSessionBuilderCustomizer {
    void customize(CqlSessionBuilder cqlSessionBuilder);
}
