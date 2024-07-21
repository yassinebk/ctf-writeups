package org.springframework.boot.autoconfigure.codec;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;
@ConfigurationProperties(prefix = "spring.codec")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/codec/CodecProperties.class */
public class CodecProperties {
    private boolean logRequestDetails;
    private DataSize maxInMemorySize;

    public boolean isLogRequestDetails() {
        return this.logRequestDetails;
    }

    public void setLogRequestDetails(boolean logRequestDetails) {
        this.logRequestDetails = logRequestDetails;
    }

    public DataSize getMaxInMemorySize() {
        return this.maxInMemorySize;
    }

    public void setMaxInMemorySize(DataSize maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }
}
