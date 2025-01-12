package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/events/Event.class */
public abstract class Event {
    private final Mark startMark;
    private final Mark endMark;

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/events/Event$ID.class */
    public enum ID {
        Alias,
        DocumentEnd,
        DocumentStart,
        MappingEnd,
        MappingStart,
        Scalar,
        SequenceEnd,
        SequenceStart,
        StreamEnd,
        StreamStart
    }

    public abstract ID getEventId();

    public Event(Mark startMark, Mark endMark) {
        this.startMark = startMark;
        this.endMark = endMark;
    }

    public String toString() {
        return "<" + getClass().getName() + "(" + getArguments() + ")>";
    }

    public Mark getStartMark() {
        return this.startMark;
    }

    public Mark getEndMark() {
        return this.endMark;
    }

    protected String getArguments() {
        return "";
    }

    public boolean is(ID id) {
        return getEventId() == id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Event) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
