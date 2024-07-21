package com.fasterxml.jackson.core;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-core-2.11.0.jar:com/fasterxml/jackson/core/FormatFeature.class */
public interface FormatFeature {
    boolean enabledByDefault();

    int getMask();

    boolean enabledIn(int i);
}
