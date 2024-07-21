package ch.qos.logback.core;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/Layout.class */
public interface Layout<E> extends ContextAware, LifeCycle {
    String doLayout(E e);

    String getFileHeader();

    String getPresentationHeader();

    String getPresentationFooter();

    String getFileFooter();

    String getContentType();
}
