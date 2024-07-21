package org.springframework.boot.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/logging/LoggerGroup.class */
public final class LoggerGroup {
    private final String name;
    private final List<String> members;
    private LogLevel configuredLevel;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LoggerGroup(String name, List<String> members) {
        this.name = name;
        this.members = Collections.unmodifiableList(new ArrayList(members));
    }

    public String getName() {
        return this.name;
    }

    public List<String> getMembers() {
        return this.members;
    }

    public boolean hasMembers() {
        return !this.members.isEmpty();
    }

    public LogLevel getConfiguredLevel() {
        return this.configuredLevel;
    }

    public void configureLogLevel(LogLevel level, BiConsumer<String, LogLevel> configurer) {
        this.configuredLevel = level;
        this.members.forEach(name -> {
            configurer.accept(name, level);
        });
    }
}
