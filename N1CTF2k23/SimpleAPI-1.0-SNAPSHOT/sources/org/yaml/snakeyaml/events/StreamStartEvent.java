package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.events.Event;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/events/StreamStartEvent.class */
public final class StreamStartEvent extends Event {
    public StreamStartEvent(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }

    @Override // org.yaml.snakeyaml.events.Event
    public Event.ID getEventId() {
        return Event.ID.StreamStart;
    }
}
