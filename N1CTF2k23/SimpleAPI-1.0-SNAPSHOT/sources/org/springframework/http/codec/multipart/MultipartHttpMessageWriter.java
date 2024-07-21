package org.springframework.http.codec.multipart;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.apache.tomcat.websocket.BasicAuthenticator;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.codec.CodecException;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.FormHttpMessageWriter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.http.codec.ResourceHttpMessageWriter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/MultipartHttpMessageWriter.class */
public class MultipartHttpMessageWriter extends LoggingCodecSupport implements HttpMessageWriter<MultiValueMap<String, ?>> {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Map<String, Object> DEFAULT_HINTS = Hints.from(Hints.SUPPRESS_LOGGING_HINT, true);
    private final List<HttpMessageWriter<?>> partWriters;
    @Nullable
    private final HttpMessageWriter<MultiValueMap<String, String>> formWriter;
    private Charset charset;
    private final List<MediaType> supportedMediaTypes;

    public MultipartHttpMessageWriter() {
        this(Arrays.asList(new EncoderHttpMessageWriter(CharSequenceEncoder.textPlainOnly()), new ResourceHttpMessageWriter()));
    }

    public MultipartHttpMessageWriter(List<HttpMessageWriter<?>> partWriters) {
        this(partWriters, new FormHttpMessageWriter());
    }

    public MultipartHttpMessageWriter(List<HttpMessageWriter<?>> partWriters, @Nullable HttpMessageWriter<MultiValueMap<String, String>> formWriter) {
        this.charset = DEFAULT_CHARSET;
        this.partWriters = partWriters;
        this.formWriter = formWriter;
        this.supportedMediaTypes = initMediaTypes(formWriter);
    }

    private static List<MediaType> initMediaTypes(@Nullable HttpMessageWriter<?> formWriter) {
        List<MediaType> result = new ArrayList<>(MultipartHttpMessageReader.MIME_TYPES);
        if (formWriter != null) {
            result.addAll(formWriter.getWritableMediaTypes());
        }
        return Collections.unmodifiableList(result);
    }

    public List<HttpMessageWriter<?>> getPartWriters() {
        return Collections.unmodifiableList(this.partWriters);
    }

    @Nullable
    public HttpMessageWriter<MultiValueMap<String, String>> getFormWriter() {
        return this.formWriter;
    }

    public void setCharset(Charset charset) {
        Assert.notNull(charset, "Charset must not be null");
        this.charset = charset;
    }

    public Charset getCharset() {
        return this.charset;
    }

    @Override // org.springframework.http.codec.HttpMessageWriter
    public List<MediaType> getWritableMediaTypes() {
        return this.supportedMediaTypes;
    }

    @Override // org.springframework.http.codec.HttpMessageWriter
    public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
        return MultiValueMap.class.isAssignableFrom(elementType.toClass()) && (mediaType == null || this.supportedMediaTypes.stream().anyMatch(element -> {
            return element.isCompatibleWith(mediaType);
        }));
    }

    @Override // org.springframework.http.codec.HttpMessageWriter
    public Mono<Void> write(Publisher<? extends MultiValueMap<String, ?>> inputStream, ResolvableType elementType, @Nullable MediaType mediaType, ReactiveHttpOutputMessage outputMessage, Map<String, Object> hints) {
        return Mono.from(inputStream).flatMap(map -> {
            if (this.formWriter == null || isMultipart(map, mediaType)) {
                return writeMultipart(map, outputMessage, mediaType, hints);
            }
            return this.formWriter.write(Mono.just(map), elementType, mediaType, outputMessage, hints);
        });
    }

    private boolean isMultipart(MultiValueMap<String, ?> map, @Nullable MediaType contentType) {
        if (contentType != null) {
            return contentType.getType().equalsIgnoreCase("multipart");
        }
        Iterator<?> it = map.values().iterator();
        while (it.hasNext()) {
            List<?> values = (List) it.next();
            for (Object value : values) {
                if (value != null && !(value instanceof String)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Mono<Void> writeMultipart(MultiValueMap<String, ?> map, ReactiveHttpOutputMessage outputMessage, @Nullable MediaType mediaType, Map<String, Object> hints) {
        byte[] boundary = generateMultipartBoundary();
        Map<String, String> params = new HashMap<>();
        if (mediaType != null) {
            params.putAll(mediaType.getParameters());
        }
        params.put("boundary", new String(boundary, StandardCharsets.US_ASCII));
        params.put(BasicAuthenticator.charsetparam, getCharset().name());
        outputMessage.getHeaders().setContentType(new MediaType(mediaType != null ? mediaType : MediaType.MULTIPART_FORM_DATA, params));
        LogFormatUtils.traceDebug(this.logger, traceOn -> {
            String str;
            StringBuilder append = new StringBuilder().append(Hints.getLogPrefix(hints)).append("Encoding ");
            if (isEnableLoggingRequestDetails()) {
                str = LogFormatUtils.formatValue(map, !traceOn.booleanValue());
            } else {
                str = "parts " + map.keySet() + " (content masked)";
            }
            return append.append(str).toString();
        });
        DataBufferFactory bufferFactory = outputMessage.bufferFactory();
        Flux<DataBuffer> body = Flux.fromIterable(map.entrySet()).concatMap(entry -> {
            return encodePartValues(boundary, (String) entry.getKey(), (List) entry.getValue(), bufferFactory);
        }).concatWith(generateLastLine(boundary, bufferFactory)).doOnDiscard(PooledDataBuffer.class, (v0) -> {
            v0.release();
        });
        return outputMessage.writeWith(body);
    }

    protected byte[] generateMultipartBoundary() {
        return MimeTypeUtils.generateMultipartBoundary();
    }

    private Flux<DataBuffer> encodePartValues(byte[] boundary, String name, List<?> values, DataBufferFactory bufferFactory) {
        return Flux.fromIterable(values).concatMap(value -> {
            return encodePart(boundary, name, value, bufferFactory);
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v46, types: [reactor.core.publisher.Mono] */
    private <T> Flux<DataBuffer> encodePart(byte[] boundary, String name, T value, DataBufferFactory bufferFactory) {
        T body;
        MultipartHttpOutputMessage outputMessage = new MultipartHttpOutputMessage(bufferFactory, getCharset());
        HttpHeaders outputHeaders = outputMessage.getHeaders();
        ResolvableType resolvableType = null;
        if (value instanceof HttpEntity) {
            HttpEntity<T> httpEntity = (HttpEntity) value;
            outputHeaders.putAll(httpEntity.getHeaders());
            body = httpEntity.getBody();
            Assert.state(body != null, "MultipartHttpMessageWriter only supports HttpEntity with body");
            if (httpEntity instanceof ResolvableTypeProvider) {
                resolvableType = ((ResolvableTypeProvider) httpEntity).getResolvableType();
            }
        } else {
            body = value;
        }
        if (resolvableType == null) {
            resolvableType = ResolvableType.forClass(body.getClass());
        }
        if (!outputHeaders.containsKey(HttpHeaders.CONTENT_DISPOSITION)) {
            if (body instanceof Resource) {
                outputHeaders.setContentDispositionFormData(name, ((Resource) body).getFilename());
            } else if (resolvableType.resolve() == Resource.class) {
                body = Mono.from((Publisher) body).doOnNext(o -> {
                    outputHeaders.setContentDispositionFormData(name, ((Resource) o).getFilename());
                });
            } else {
                outputHeaders.setContentDispositionFormData(name, null);
            }
        }
        MediaType contentType = outputHeaders.getContentType();
        ResolvableType finalBodyType = resolvableType;
        Optional<HttpMessageWriter<?>> writer = this.partWriters.stream().filter(partWriter -> {
            return partWriter.canWrite(finalBodyType, contentType);
        }).findFirst();
        if (!writer.isPresent()) {
            return Flux.error(new CodecException("No suitable writer found for part: " + name));
        }
        Mono<Void> partContentReady = writer.get().write(body instanceof Publisher ? (Publisher) body : Mono.just(body), resolvableType, contentType, outputMessage, DEFAULT_HINTS);
        outputMessage.getClass();
        return Flux.concat(new Publisher[]{generateBoundaryLine(boundary, bufferFactory), partContentReady.thenMany(Flux.defer(this::getBody)), generateNewLine(bufferFactory)});
    }

    private Mono<DataBuffer> generateBoundaryLine(byte[] boundary, DataBufferFactory bufferFactory) {
        return Mono.fromCallable(() -> {
            DataBuffer buffer = bufferFactory.allocateBuffer(boundary.length + 4);
            buffer.write((byte) 45);
            buffer.write((byte) 45);
            buffer.write(boundary);
            buffer.write((byte) 13);
            buffer.write((byte) 10);
            return buffer;
        });
    }

    private Mono<DataBuffer> generateNewLine(DataBufferFactory bufferFactory) {
        return Mono.fromCallable(() -> {
            DataBuffer buffer = bufferFactory.allocateBuffer(2);
            buffer.write((byte) 13);
            buffer.write((byte) 10);
            return buffer;
        });
    }

    private Mono<DataBuffer> generateLastLine(byte[] boundary, DataBufferFactory bufferFactory) {
        return Mono.fromCallable(() -> {
            DataBuffer buffer = bufferFactory.allocateBuffer(boundary.length + 6);
            buffer.write((byte) 45);
            buffer.write((byte) 45);
            buffer.write(boundary);
            buffer.write((byte) 45);
            buffer.write((byte) 45);
            buffer.write((byte) 13);
            buffer.write((byte) 10);
            return buffer;
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/MultipartHttpMessageWriter$MultipartHttpOutputMessage.class */
    public static class MultipartHttpOutputMessage implements ReactiveHttpOutputMessage {
        private final DataBufferFactory bufferFactory;
        private final Charset charset;
        private final HttpHeaders headers = new HttpHeaders();
        private final AtomicBoolean committed = new AtomicBoolean();
        @Nullable
        private Flux<DataBuffer> body;

        public MultipartHttpOutputMessage(DataBufferFactory bufferFactory, Charset charset) {
            this.bufferFactory = bufferFactory;
            this.charset = charset;
        }

        @Override // org.springframework.http.HttpMessage
        public HttpHeaders getHeaders() {
            return this.body != null ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
        }

        @Override // org.springframework.http.ReactiveHttpOutputMessage
        public DataBufferFactory bufferFactory() {
            return this.bufferFactory;
        }

        @Override // org.springframework.http.ReactiveHttpOutputMessage
        public void beforeCommit(Supplier<? extends Mono<Void>> action) {
            this.committed.set(true);
        }

        @Override // org.springframework.http.ReactiveHttpOutputMessage
        public boolean isCommitted() {
            return this.committed.get();
        }

        @Override // org.springframework.http.ReactiveHttpOutputMessage
        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            if (this.body != null) {
                return Mono.error(new IllegalStateException("Multiple calls to writeWith() not supported"));
            }
            this.body = generateHeaders().concatWith(body);
            return Mono.empty();
        }

        private Mono<DataBuffer> generateHeaders() {
            return Mono.fromCallable(() -> {
                DataBuffer buffer = this.bufferFactory.allocateBuffer();
                for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
                    byte[] headerName = entry.getKey().getBytes(this.charset);
                    for (String headerValueString : entry.getValue()) {
                        byte[] headerValue = headerValueString.getBytes(this.charset);
                        buffer.write(headerName);
                        buffer.write((byte) 58);
                        buffer.write((byte) 32);
                        buffer.write(headerValue);
                        buffer.write((byte) 13);
                        buffer.write((byte) 10);
                    }
                }
                buffer.write((byte) 13);
                buffer.write((byte) 10);
                return buffer;
            });
        }

        @Override // org.springframework.http.ReactiveHttpOutputMessage
        public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
            return Mono.error(new UnsupportedOperationException());
        }

        public Flux<DataBuffer> getBody() {
            return this.body != null ? this.body : Flux.error(new IllegalStateException("Body has not been written yet"));
        }

        @Override // org.springframework.http.ReactiveHttpOutputMessage
        public Mono<Void> setComplete() {
            return Mono.error(new UnsupportedOperationException());
        }
    }
}
