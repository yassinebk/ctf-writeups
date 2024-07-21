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
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/DataBufferEncoder.class */
public class DataBufferEncoder extends AbstractEncoder<DataBuffer> {
    @Override // org.springframework.core.codec.Encoder
    public /* bridge */ /* synthetic */ DataBuffer encodeValue(Object obj, DataBufferFactory dataBufferFactory, ResolvableType resolvableType, @Nullable MimeType mimeType, @Nullable Map map) {
        return encodeValue((DataBuffer) obj, dataBufferFactory, resolvableType, mimeType, (Map<String, Object>) map);
    }

    public DataBufferEncoder() {
        super(MimeTypeUtils.ALL);
    }

    @Override // org.springframework.core.codec.AbstractEncoder, org.springframework.core.codec.Encoder
    public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
        Class<?> clazz = elementType.toClass();
        return super.canEncode(elementType, mimeType) && DataBuffer.class.isAssignableFrom(clazz);
    }

    @Override // org.springframework.core.codec.Encoder
    public Flux<DataBuffer> encode(Publisher<? extends DataBuffer> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        Flux<DataBuffer> flux = Flux.from(inputStream);
        if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
            flux = flux.doOnNext(buffer -> {
                logValue(buffer, hints);
            });
        }
        return flux;
    }

    public DataBuffer encodeValue(DataBuffer buffer, DataBufferFactory bufferFactory, ResolvableType valueType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
            logValue(buffer, hints);
        }
        return buffer;
    }

    private void logValue(DataBuffer buffer, @Nullable Map<String, Object> hints) {
        String logPrefix = Hints.getLogPrefix(hints);
        this.logger.debug(logPrefix + "Writing " + buffer.readableByteCount() + " bytes");
    }
}
