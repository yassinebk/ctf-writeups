package org.springframework.boot.info;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/info/BuildProperties.class */
public class BuildProperties extends InfoProperties {
    public BuildProperties(Properties entries) {
        super(processEntries(entries));
    }

    public String getGroup() {
        return get("group");
    }

    public String getArtifact() {
        return get("artifact");
    }

    public String getName() {
        return get("name");
    }

    public String getVersion() {
        return get("version");
    }

    public Instant getTime() {
        return getInstant("time");
    }

    private static Properties processEntries(Properties properties) {
        coerceDate(properties, "time");
        return properties;
    }

    private static void coerceDate(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                String updatedValue = String.valueOf(((Instant) DateTimeFormatter.ISO_INSTANT.parse(value, Instant::from)).toEpochMilli());
                properties.setProperty(key, updatedValue);
            } catch (DateTimeException e) {
            }
        }
    }
}
