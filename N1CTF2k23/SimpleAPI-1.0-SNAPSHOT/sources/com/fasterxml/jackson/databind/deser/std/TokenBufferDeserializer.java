package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.IOException;
@JacksonStdImpl
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/deser/std/TokenBufferDeserializer.class */
public class TokenBufferDeserializer extends StdScalarDeserializer<TokenBuffer> {
    private static final long serialVersionUID = 1;

    public TokenBufferDeserializer() {
        super(TokenBuffer.class);
    }

    @Override // com.fasterxml.jackson.databind.JsonDeserializer
    public TokenBuffer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return createBufferInstance(p).deserialize(p, ctxt);
    }

    protected TokenBuffer createBufferInstance(JsonParser p) {
        return new TokenBuffer(p);
    }
}
