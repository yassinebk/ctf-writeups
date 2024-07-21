package org.springframework.boot.system;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.tomcat.websocket.Constants;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/system/JavaVersion.class */
public enum JavaVersion {
    EIGHT("1.8", Optional.class, "empty"),
    NINE("9", Optional.class, "stream"),
    TEN("10", Optional.class, "orElseThrow"),
    ELEVEN("11", String.class, "strip"),
    TWELVE("12", String.class, "describeConstable"),
    THIRTEEN(Constants.WS_VERSION_HEADER_VALUE, String.class, "stripIndent"),
    FOURTEEN("14", MethodHandles.Lookup.class, "hasFullPrivilegeAccess");
    
    private final String name;
    private final boolean available;

    JavaVersion(String name, Class clazz, String methodName) {
        this.name = name;
        this.available = ClassUtils.hasMethod(clazz, methodName, new Class[0]);
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.name;
    }

    public static JavaVersion getJavaVersion() {
        List<JavaVersion> candidates = Arrays.asList(values());
        Collections.reverse(candidates);
        for (JavaVersion candidate : candidates) {
            if (candidate.available) {
                return candidate;
            }
        }
        return EIGHT;
    }

    public boolean isEqualOrNewerThan(JavaVersion version) {
        return compareTo(version) >= 0;
    }

    public boolean isOlderThan(JavaVersion version) {
        return compareTo(version) < 0;
    }
}
