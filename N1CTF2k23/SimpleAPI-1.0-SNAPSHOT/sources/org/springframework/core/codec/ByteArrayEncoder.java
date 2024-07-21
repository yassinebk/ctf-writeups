package org.springframework.core.codec;

import java.util.Map;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/ByteArrayEncoder.class */
public class ByteArrayEncoder extends AbstractEncoder<byte[]> {
    @Override // org.springframework.core.codec.Encoder
    public /* bridge */ /* synthetic */ DataBuffer encodeValue(Object obj, DataBufferFactory dataBufferFactory, ResolvableType resolvableType, @Nullable MimeType mimeType, @Nullable Map map) {
        return encodeValue((byte[]) obj, dataBufferFactory, resolvableType, mimeType, (Map<String, Object>) map);
    }

    public ByteArrayEncoder() {
        super(MimeTypeUtils.ALL);
    }

    @Override // org.springframework.core.codec.AbstractEncoder, org.springframework.core.codec.Encoder
    public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
        Class<?> clazz = elementType.toClass();
        return super.canEncode(elementType, mimeType) && byte[].class.isAssignableFrom(clazz);
    }

    @Override // org.springframework.core.codec.Encoder
    public Flux<DataBuffer> encode(Publisher<? extends byte[]> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        return Flux.from(inputStream).map(bytes -> {
            return encodeValue(bytes, bufferFactory, elementType, mimeType, (Map<String, Object>) hints);
        });
    }

    public DataBuffer encodeValue(byte[] bytes, DataBufferFactory bufferFactory, ResolvableType valueType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        DataBuffer dataBuffer = bufferFactory.wrap(bytes);
        if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
            String logPrefix = Hints.getLogPrefix(hints);
            this.logger.debug(logPrefix + "Writing " + dataBuffer.readableByteCount() + " bytes");
        }
        return dataBuffer;
    }
}
