package com.fasterxml.jackson.databind.node;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/node/NodeSerialization.class */
class NodeSerialization implements Serializable, Externalizable {
    private static final long serialVersionUID = 1;
    public byte[] json;

    public NodeSerialization() {
    }

    public NodeSerialization(byte[] b) {
        this.json = b;
    }

    protected Object readResolve() {
        try {
            return InternalNodeMapper.bytesToNode(this.json);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to JDK deserialize `JsonNode` value: " + e.getMessage(), e);
        }
    }

    public static NodeSerialization from(Object o) {
        try {
            return new NodeSerialization(InternalNodeMapper.valueToBytes(o));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to JDK serialize `" + o.getClass().getSimpleName() + "` value: " + e.getMessage(), e);
        }
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(this.json.length);
        out.write(this.json);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException {
        int len = in.readInt();
        this.json = new byte[len];
        in.readFully(this.json, 0, len);
    }
}
