package org.yaml.snakeyaml.reader;

import org.yaml.snakeyaml.error.YAMLException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/reader/ReaderException.class */
public class ReaderException extends YAMLException {
    private static final long serialVersionUID = 8710781187529689083L;
    private final String name;
    private final int codePoint;
    private final int position;

    public ReaderException(String name, int position, int codePoint, String message) {
        super(message);
        this.name = name;
        this.codePoint = codePoint;
        this.position = position;
    }

    public String getName() {
        return this.name;
    }

    public int getCodePoint() {
        return this.codePoint;
    }

    public int getPosition() {
        return this.position;
    }

    @Override // java.lang.Throwable
    public String toString() {
        String s = new String(Character.toChars(this.codePoint));
        return "unacceptable code point '" + s + "' (0x" + Integer.toHexString(this.codePoint).toUpperCase() + ") " + getMessage() + "\nin \"" + this.name + "\", position " + this.position;
    }
}
