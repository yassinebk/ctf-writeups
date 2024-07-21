package org.springframework.http.codec;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.CodecException;
import org.springframework.core.codec.Encoder;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/ServerSentEventHttpMessageWriter.class */
public class ServerSentEventHttpMessageWriter implements HttpMessageWriter<Object> {
    private static final MediaType DEFAULT_MEDIA_TYPE = new MediaType("text", "event-stream", StandardCharsets.UTF_8);
    private static final List<MediaType> WRITABLE_MEDIA_TYPES = Collections.singletonList(MediaType.TEXT_EVENT_STREAM);
    @Nullable
    private final Encoder<?> encoder;

    public ServerSentEventHttpMessageWriter() {
        this(null);
    }

    public ServerSentEventHttpMessageWriter(@Nullable Encoder<?> encoder) {
        this.encoder = encoder;
    }

    @Nullable
    public Encoder<?> getEncoder() {
        return this.encoder;
    }

    @Override // org.springframework.http.codec.HttpMessageWriter
    public List<MediaType> getWritableMediaTypes() {
        return WRITABLE_MEDIA_TYPES;
    }

    @Override // org.springframework.http.codec.HttpMessageWriter
    public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
        return mediaType == null || MediaType.TEXT_EVENT_STREAM.includes(mediaType) || ServerSentEvent.class.isAssignableFrom(elementType.toClass());
    }

    @Override // org.springframework.http.codec.HttpMessageWriter
    public Mono<Void> write(Publisher<? extends Object> publisher, ResolvableType elementType, @Nullable MediaType mediaType, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
        MediaType mediaType2 = (mediaType == null || mediaType.getCharset() == null) ? DEFAULT_MEDIA_TYPE : mediaType;
        DataBufferFactory bufferFactory = message.bufferFactory();
        message.getHeaders().setContentType(mediaType2);
        return message.writeAndFlushWith(encode(publisher, elementType, mediaType2, bufferFactory, hints));
    }

    private Flux<Publisher<DataBuffer>> encode(Publisher<?> input, ResolvableType elementType, MediaType mediaType, DataBufferFactory factory, Map<String, Object> hints) {
        ResolvableType dataType = ServerSentEvent.class.isAssignableFrom(elementType.toClass()) ? elementType.getGeneric(new int[0]) : elementType;
        return Flux.from(input).map(element -> {
            Flux<DataBuffer> result;
            ServerSentEvent<?> sse = element instanceof ServerSentEvent ? (ServerSentEvent) element : ServerSentEvent.builder().data(element).build();
            StringBuilder sb = new StringBuilder();
            String id = sse.id();
            String event = sse.event();
            Duration retry = sse.retry();
            String comment = sse.comment();
            Object data = sse.data();
            if (id != null) {
                writeField("id", id, sb);
            }
            if (event != null) {
                writeField("event", event, sb);
            }
            if (retry != null) {
                writeField("retry", Long.valueOf(retry.toMillis()), sb);
            }
            if (comment != null) {
                sb.append(':').append(StringUtils.replace(comment, "\n", "\n:")).append("\n");
            }
            if (data != null) {
                sb.append("data:");
            }
            if (data == null) {
                result = Flux.just(encodeText(((Object) sb) + "\n", mediaType, factory));
            } else if (data instanceof String) {
                result = Flux.just(encodeText(((Object) sb) + ((String) StringUtils.replace((String) data, "\n", "\ndata:")) + "\n\n", mediaType, factory));
            } else {
                result = encodeEvent(sb.toString(), data, dataType, mediaType, factory, hints);
            }
            return result.doOnDiscard(PooledDataBuffer.class, (v0) -> {
                DataBufferUtils.release(v0);
            });
        });
    }

    private <T> Flux<DataBuffer> encodeEvent(String eventContent, T data, ResolvableType dataType, MediaType mediaType, DataBufferFactory factory, Map<String, Object> hints) {
        if (this.encoder == null) {
            throw new CodecException("No SSE encoder configured and the data is not String.");
        }
        return Flux.just(factory.join(Arrays.asList(encodeText(eventContent, mediaType, factory), this.encoder.encodeValue(data, factory, dataType, mediaType, hints), encodeText("\n\n", mediaType, factory))));
    }

    private void writeField(String fieldName, Object fieldValue, StringBuilder sb) {
        sb.append(fieldName);
        sb.append(':');
        sb.append(fieldValue.toString());
        sb.append("\n");
    }

    private DataBuffer encodeText(CharSequence text, MediaType mediaType, DataBufferFactory bufferFactory) {
        Assert.notNull(mediaType.getCharset(), "Expected MediaType with charset");
        byte[] bytes = text.toString().getBytes(mediaType.getCharset());
        return bufferFactory.wrap(bytes);
    }

    @Override // org.springframework.http.codec.HttpMessageWriter
    public Mono<Void> write(Publisher<? extends Object> publisher, ResolvableType actualType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
        Map<String, Object> allHints = Hints.merge(hints, getEncodeHints(actualType, elementType, mediaType, request, response));
        return write(publisher, elementType, mediaType, response, allHints);
    }

    private Map<String, Object> getEncodeHints(ResolvableType actualType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response) {
        if (this.encoder instanceof HttpMessageEncoder) {
            HttpMessageEncoder<?> encoder = (HttpMessageEncoder) this.encoder;
            return encoder.getEncodeHints(actualType, elementType, mediaType, request, response);
        }
        return Hints.none();
    }
}
