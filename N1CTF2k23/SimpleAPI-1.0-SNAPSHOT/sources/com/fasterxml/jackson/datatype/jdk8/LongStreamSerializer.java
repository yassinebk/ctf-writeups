package com.fasterxml.jackson.datatype.jdk8;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.stream.LongStream;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-datatype-jdk8-2.11.0.jar:com/fasterxml/jackson/datatype/jdk8/LongStreamSerializer.class */
public class LongStreamSerializer extends StdSerializer<LongStream> {
    private static final long serialVersionUID = 1;
    public static final LongStreamSerializer INSTANCE = new LongStreamSerializer();

    private LongStreamSerializer() {
        super(LongStream.class);
    }

    @Override // com.fasterxml.jackson.databind.ser.std.StdSerializer, com.fasterxml.jackson.databind.JsonSerializer
    public void serialize(LongStream stream, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        try {
            jgen.writeStartArray();
            stream.forEachOrdered(value -> {
                try {
                    jgen.writeNumber(value);
                } catch (IOException e) {
                    throw new WrappedIOException(e);
                }
            });
            jgen.writeEndArray();
            if (stream != null) {
                if (0 != 0) {
                    stream.close();
                } else {
                    stream.close();
                }
            }
        } catch (WrappedIOException e) {
            throw e.getCause();
        }
    }
}
