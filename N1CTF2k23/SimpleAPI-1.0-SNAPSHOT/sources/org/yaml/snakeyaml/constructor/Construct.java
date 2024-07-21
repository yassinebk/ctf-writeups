package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.Node;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/constructor/Construct.class */
public interface Construct {
    Object construct(Node node);

    void construct2ndStep(Node node, Object obj);
}
