package org.springframework.boot.autoconfigure.couchbase;

import com.couchbase.client.java.env.ClusterEnvironment;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/ClusterEnvironmentBuilderCustomizer.class */
public interface ClusterEnvironmentBuilderCustomizer {
    void customize(ClusterEnvironment.Builder builder);
}
