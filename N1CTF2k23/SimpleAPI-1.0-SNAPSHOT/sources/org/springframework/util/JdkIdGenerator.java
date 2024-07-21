package org.springframework.util;

import java.util.UUID;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/JdkIdGenerator.class */
public class JdkIdGenerator implements IdGenerator {
    @Override // org.springframework.util.IdGenerator
    public UUID generateId() {
        return UUID.randomUUID();
    }
}
