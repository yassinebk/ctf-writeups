package org.springframework.aop.config;

import org.springframework.beans.factory.parsing.ParseState;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/config/AdvisorEntry.class */
public class AdvisorEntry implements ParseState.Entry {
    private final String name;

    public AdvisorEntry(String name) {
        this.name = name;
    }

    public String toString() {
        return "Advisor '" + this.name + "'";
    }
}
