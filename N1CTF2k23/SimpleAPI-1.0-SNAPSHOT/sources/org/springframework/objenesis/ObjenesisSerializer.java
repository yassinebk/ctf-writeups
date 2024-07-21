package org.springframework.objenesis;

import org.springframework.objenesis.strategy.SerializingInstantiatorStrategy;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/objenesis/ObjenesisSerializer.class */
public class ObjenesisSerializer extends ObjenesisBase {
    public ObjenesisSerializer() {
        super(new SerializingInstantiatorStrategy());
    }

    public ObjenesisSerializer(boolean useCache) {
        super(new SerializingInstantiatorStrategy(), useCache);
    }
}
