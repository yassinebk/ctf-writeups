package org.springframework.boot.autoconfigure.hateoas;

import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "spring.hateoas")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hateoas/HateoasProperties.class */
public class HateoasProperties {
    private boolean useHalAsDefaultJsonMediaType = true;

    public boolean getUseHalAsDefaultJsonMediaType() {
        return this.useHalAsDefaultJsonMediaType;
    }

    public void setUseHalAsDefaultJsonMediaType(boolean useHalAsDefaultJsonMediaType) {
        this.useHalAsDefaultJsonMediaType = useHalAsDefaultJsonMediaType;
    }
}
