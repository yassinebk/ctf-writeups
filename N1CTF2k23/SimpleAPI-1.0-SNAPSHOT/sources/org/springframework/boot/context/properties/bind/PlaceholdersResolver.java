package org.springframework.boot.context.properties.bind;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/PlaceholdersResolver.class */
public interface PlaceholdersResolver {
    public static final PlaceholdersResolver NONE = value -> {
        return value;
    };

    Object resolvePlaceholders(Object value);
}
