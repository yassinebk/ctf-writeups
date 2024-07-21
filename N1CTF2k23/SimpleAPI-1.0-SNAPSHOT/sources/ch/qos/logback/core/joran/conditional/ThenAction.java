package ch.qos.logback.core.joran.conditional;

import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/joran/conditional/ThenAction.class */
public class ThenAction extends ThenOrElseActionBase {
    @Override // ch.qos.logback.core.joran.conditional.ThenOrElseActionBase
    void registerEventList(IfAction ifAction, List<SaxEvent> eventList) {
        ifAction.setThenSaxEventList(eventList);
    }
}
