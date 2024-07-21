package org.yaml.snakeyaml.error;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/error/YAMLException.class */
public class YAMLException extends RuntimeException {
    private static final long serialVersionUID = -4738336175050337570L;

    public YAMLException(String message) {
        super(message);
    }

    public YAMLException(Throwable cause) {
        super(cause);
    }

    public YAMLException(String message, Throwable cause) {
        super(message, cause);
    }
}
