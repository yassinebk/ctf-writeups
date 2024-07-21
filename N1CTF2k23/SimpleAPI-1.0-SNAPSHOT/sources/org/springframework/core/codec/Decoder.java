package org.springframework.core.codec;

import java.util.List;
import java.util.Map;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/Decoder.class */
public interface Decoder<T> {
    boolean canDecode(ResolvableType resolvableType, @Nullable MimeType mimeType);

    Flux<T> decode(Publisher<DataBuffer> publisher, ResolvableType resolvableType, @Nullable MimeType mimeType, @Nullable Map<String, Object> map);

    Mono<T> decodeToMono(Publisher<DataBuffer> publisher, ResolvableType resolvableType, @Nullable MimeType mimeType, @Nullable Map<String, Object> map);

    List<MimeType> getDecodableMimeTypes();

    @Nullable
    default T decode(DataBuffer buffer, ResolvableType targetType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) throws DecodingException {
        MonoProcessor<T> processor = MonoProcessor.create();
        decodeToMono(Mono.just(buffer), targetType, mimeType, hints).subscribeWith(processor);
        Assert.state(processor.isTerminated(), "DataBuffer decoding should have completed.");
        Throwable ex = processor.getError();
        if (ex != null) {
            if (ex instanceof CodecException) {
                throw ((CodecException) ex);
            }
            throw new DecodingException("Failed to decode: " + ex.getMessage(), ex);
        }
        return (T) processor.peek();
    }
}
