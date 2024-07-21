package org.springframework.aop.config;

import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/config/AspectEntry.class */
public class AspectEntry implements ParseState.Entry {
    private final String id;
    private final String ref;

    public AspectEntry(String id, String ref) {
        this.id = id;
        this.ref = ref;
    }

    public String toString() {
        return "Aspect: " + (StringUtils.hasLength(this.id) ? "id='" + this.id + "'" : "ref='" + this.ref + "'");
    }
}
