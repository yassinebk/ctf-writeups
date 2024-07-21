package org.springframework.boot.jdbc.metadata;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jdbc/metadata/DataSourcePoolMetadata.class */
public interface DataSourcePoolMetadata {
    Float getUsage();

    Integer getActive();

    Integer getMax();

    Integer getMin();

    String getValidationQuery();

    Boolean getDefaultAutoCommit();

    default Integer getIdle() {
        return null;
    }
}
