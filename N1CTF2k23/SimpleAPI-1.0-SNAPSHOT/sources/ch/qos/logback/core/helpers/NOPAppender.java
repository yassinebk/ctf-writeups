package ch.qos.logback.core.helpers;

import ch.qos.logback.core.AppenderBase;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/helpers/NOPAppender.class */
public final class NOPAppender<E> extends AppenderBase<E> {
    @Override // ch.qos.logback.core.AppenderBase
    protected void append(E eventObject) {
    }
}
