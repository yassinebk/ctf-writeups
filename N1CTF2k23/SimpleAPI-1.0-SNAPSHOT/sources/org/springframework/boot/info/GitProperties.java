package org.springframework.boot.info;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Properties;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/info/GitProperties.class */
public class GitProperties extends InfoProperties {
    public GitProperties(Properties entries) {
        super(processEntries(entries));
    }

    public String getBranch() {
        return get("branch");
    }

    public String getCommitId() {
        return get("commit.id");
    }

    public String getShortCommitId() {
        String shortId = get("commit.id.abbrev");
        if (shortId != null) {
            return shortId;
        }
        String id = getCommitId();
        if (id == null) {
            return null;
        }
        return id.length() > 7 ? id.substring(0, 7) : id;
    }

    public Instant getCommitTime() {
        return getInstant("commit.time");
    }

    private static Properties processEntries(Properties properties) {
        coercePropertyToEpoch(properties, "commit.time");
        coercePropertyToEpoch(properties, "build.time");
        Object commitId = properties.get("commit.id");
        if (commitId != null) {
            properties.put("commit.id.full", commitId);
        }
        return properties;
    }

    private static void coercePropertyToEpoch(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            properties.setProperty(key, coerceToEpoch(value));
        }
    }

    private static String coerceToEpoch(String s) {
        Long epoch = parseEpochSecond(s);
        if (epoch != null) {
            return String.valueOf(epoch);
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            return String.valueOf(((Instant) format.parse(s, Instant::from)).toEpochMilli());
        } catch (DateTimeParseException e) {
            return s;
        }
    }

    private static Long parseEpochSecond(String s) {
        try {
            return Long.valueOf(Long.parseLong(s) * 1000);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
