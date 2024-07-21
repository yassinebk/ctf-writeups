package org.yaml.snakeyaml.emitter;

import java.io.IOException;
import org.yaml.snakeyaml.events.Event;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/emitter/Emitable.class */
public interface Emitable {
    void emit(Event event) throws IOException;
}
