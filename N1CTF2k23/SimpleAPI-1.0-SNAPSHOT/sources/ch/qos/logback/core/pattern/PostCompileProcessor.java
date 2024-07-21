package ch.qos.logback.core.pattern;

import ch.qos.logback.core.Context;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/pattern/PostCompileProcessor.class */
public interface PostCompileProcessor<E> {
    void process(Context context, Converter<E> converter);
}
