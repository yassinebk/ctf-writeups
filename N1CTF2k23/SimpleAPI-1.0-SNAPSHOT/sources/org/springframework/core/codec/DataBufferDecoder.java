package org.springframework.core.codec;

import java.util.Map;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/DataBufferDecoder.class */
public class DataBufferDecoder extends AbstractDataBufferDecoder<DataBuffer> {
    @Override // org.springframework.core.codec.Decoder
    public /* bridge */ /* synthetic */ Object decode(DataBuffer dataBuffer, ResolvableType resolvableType, @Nullable MimeType mimeType, @Nullable Map map) throws DecodingException {
        return decode(dataBuffer, resolvableType, mimeType, (Map<String, Object>) map);
    }

    public DataBufferDecoder() {
        super(MimeTypeUtils.ALL);
    }

    @Override // org.springframework.core.codec.AbstractDecoder, org.springframework.core.codec.Decoder
    public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
        return DataBuffer.class.isAssignableFrom(elementType.toClass()) && super.canDecode(elementType, mimeType);
    }

    @Override // org.springframework.core.codec.AbstractDataBufferDecoder, org.springframework.core.codec.Decoder
    public Flux<DataBuffer> decode(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        return Flux.from(input);
    }

    @Override // org.springframework.core.codec.Decoder
    public DataBuffer decode(DataBuffer buffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(Hints.getLogPrefix(hints) + "Read " + buffer.readableByteCount() + " bytes");
        }
        return buffer;
    }
}
