package ch.qos.logback.core.read;

import ch.qos.logback.core.AppenderBase;
import java.util.ArrayList;
import java.util.List;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/read/ListAppender.class */
public class ListAppender<E> extends AppenderBase<E> {
    public List<E> list = new ArrayList();

    @Override // ch.qos.logback.core.AppenderBase
    protected void append(E e) {
        this.list.add(e);
    }
}
