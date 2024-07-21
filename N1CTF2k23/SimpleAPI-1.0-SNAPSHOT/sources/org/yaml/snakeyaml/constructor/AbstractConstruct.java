package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/constructor/AbstractConstruct.class */
public abstract class AbstractConstruct implements Construct {
    @Override // org.yaml.snakeyaml.constructor.Construct
    public void construct2ndStep(Node node, Object data) {
        if (node.isTwoStepsConstruction()) {
            throw new IllegalStateException("Not Implemented in " + getClass().getName());
        }
        throw new YAMLException("Unexpected recursive structure for Node: " + node);
    }
}
