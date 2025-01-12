package org.springframework.boot.logging;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/logging/LoggerGroups.class */
public final class LoggerGroups implements Iterable<LoggerGroup> {
    private final Map<String, LoggerGroup> groups = new ConcurrentHashMap();

    public LoggerGroups() {
    }

    public LoggerGroups(Map<String, List<String>> namesAndMembers) {
        putAll(namesAndMembers);
    }

    public void putAll(Map<String, List<String>> namesAndMembers) {
        namesAndMembers.forEach(this::put);
    }

    private void put(String name, List<String> members) {
        put(new LoggerGroup(name, members));
    }

    private void put(LoggerGroup loggerGroup) {
        this.groups.put(loggerGroup.getName(), loggerGroup);
    }

    public LoggerGroup get(String name) {
        return this.groups.get(name);
    }

    @Override // java.lang.Iterable
    public Iterator<LoggerGroup> iterator() {
        return this.groups.values().iterator();
    }
}
