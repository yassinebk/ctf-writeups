package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/events/CollectionEndEvent.class */
public abstract class CollectionEndEvent extends Event {
    public CollectionEndEvent(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }
}
