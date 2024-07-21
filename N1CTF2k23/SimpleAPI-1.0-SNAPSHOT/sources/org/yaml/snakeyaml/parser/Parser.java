package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.events.Event;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/parser/Parser.class */
public interface Parser {
    boolean checkEvent(Event.ID id);

    Event peekEvent();

    Event getEvent();
}
