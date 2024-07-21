package ch.qos.logback.core.encoder;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/encoder/Encoder.class */
public interface Encoder<E> extends ContextAware, LifeCycle {
    byte[] headerBytes();

    byte[] encode(E e);

    byte[] footerBytes();
}
