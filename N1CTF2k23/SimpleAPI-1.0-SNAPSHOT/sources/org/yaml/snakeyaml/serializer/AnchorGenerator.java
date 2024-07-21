package org.yaml.snakeyaml.serializer;

import org.yaml.snakeyaml.nodes.Node;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/serializer/AnchorGenerator.class */
public interface AnchorGenerator {
    String nextAnchor(Node node);
}
