package org.yaml.snakeyaml.nodes;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/nodes/ScalarNode.class */
public class ScalarNode extends Node {
    private DumperOptions.ScalarStyle style;
    private String value;

    public ScalarNode(Tag tag, String value, Mark startMark, Mark endMark, DumperOptions.ScalarStyle style) {
        this(tag, true, value, startMark, endMark, style);
    }

    public ScalarNode(Tag tag, boolean resolved, String value, Mark startMark, Mark endMark, DumperOptions.ScalarStyle style) {
        super(tag, startMark, endMark);
        if (value == null) {
            throw new NullPointerException("value in a Node is required.");
        }
        this.value = value;
        if (style == null) {
            throw new NullPointerException("Scalar style must be provided.");
        }
        this.style = style;
        this.resolved = resolved;
    }

    @Deprecated
    public ScalarNode(Tag tag, String value, Mark startMark, Mark endMark, Character style) {
        this(tag, value, startMark, endMark, DumperOptions.ScalarStyle.createStyle(style));
    }

    @Deprecated
    public ScalarNode(Tag tag, boolean resolved, String value, Mark startMark, Mark endMark, Character style) {
        this(tag, resolved, value, startMark, endMark, DumperOptions.ScalarStyle.createStyle(style));
    }

    @Deprecated
    public Character getStyle() {
        return this.style.getChar();
    }

    public DumperOptions.ScalarStyle getScalarStyle() {
        return this.style;
    }

    @Override // org.yaml.snakeyaml.nodes.Node
    public NodeId getNodeId() {
        return NodeId.scalar;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return "<" + getClass().getName() + " (tag=" + getTag() + ", value=" + getValue() + ")>";
    }

    public boolean isPlain() {
        return this.style == DumperOptions.ScalarStyle.PLAIN;
    }
}
