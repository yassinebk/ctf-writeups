package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.error.Mark;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/constructor/DuplicateKeyException.class */
public class DuplicateKeyException extends ConstructorException {
    /* JADX INFO: Access modifiers changed from: protected */
    public DuplicateKeyException(Mark contextMark, Object key, Mark problemMark) {
        super("while constructing a mapping", contextMark, "found duplicate key " + String.valueOf(key), problemMark);
    }
}
