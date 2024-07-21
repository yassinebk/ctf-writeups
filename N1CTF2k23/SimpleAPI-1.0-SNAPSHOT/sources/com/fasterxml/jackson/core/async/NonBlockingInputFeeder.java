package com.fasterxml.jackson.core.async;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-core-2.11.0.jar:com/fasterxml/jackson/core/async/NonBlockingInputFeeder.class */
public interface NonBlockingInputFeeder {
    boolean needMoreInput();

    void endOfInput();
}
