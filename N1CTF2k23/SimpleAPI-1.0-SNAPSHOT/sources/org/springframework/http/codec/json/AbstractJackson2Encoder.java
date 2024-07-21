package org.springframework.http.codec.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.reactivestreams.Publisher;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.CodecException;
import org.springframework.core.codec.EncodingException;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/json/AbstractJackson2Encoder.class */
public abstract class AbstractJackson2Encoder extends Jackson2CodecSupport implements HttpMessageEncoder<Object> {
    private static final byte[] NEWLINE_SEPARATOR = {10};
    private static final Map<MediaType, byte[]> STREAM_SEPARATORS = new HashMap(4);
    private final List<MediaType> streamingMediaTypes;

    static {
        STREAM_SEPARATORS.put(MediaType.APPLICATION_STREAM_JSON, NEWLINE_SEPARATOR);
        STREAM_SEPARATORS.put(MediaType.parseMediaType("application/stream+x-jackson-smile"), new byte[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractJackson2Encoder(ObjectMapper mapper, MimeType... mimeTypes) {
        super(mapper, mimeTypes);
        this.streamingMediaTypes = new ArrayList(1);
    }

    public void setStreamingMediaTypes(List<MediaType> mediaTypes) {
        this.streamingMediaTypes.clear();
        this.streamingMediaTypes.addAll(mediaTypes);
    }

    @Override // org.springframework.core.codec.Encoder
    public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
        Class<?> clazz = elementType.toClass();
        return supportsMimeType(mimeType) && (Object.class == clazz || (!String.class.isAssignableFrom(elementType.resolve(clazz)) && getObjectMapper().canSerialize(clazz)));
    }

    public Flux<DataBuffer> encode(Publisher<?> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        Assert.notNull(inputStream, "'inputStream' must not be null");
        Assert.notNull(bufferFactory, "'bufferFactory' must not be null");
        Assert.notNull(elementType, "'elementType' must not be null");
        if (inputStream instanceof Mono) {
            return Mono.from(inputStream).map(value -> {
                return encodeValue(value, bufferFactory, elementType, mimeType, hints);
            }).flux();
        }
        byte[] separator = streamSeparator(mimeType);
        if (separator != null) {
            try {
                ObjectWriter writer = createObjectWriter(elementType, mimeType, hints);
                ByteArrayBuilder byteBuilder = new ByteArrayBuilder(writer.getFactory()._getBufferRecycler());
                JsonEncoding encoding = getJsonEncoding(mimeType);
                JsonGenerator generator = getObjectMapper().getFactory().createGenerator(byteBuilder, encoding);
                SequenceWriter sequenceWriter = writer.writeValues(generator);
                return Flux.from(inputStream).map(value2 -> {
                    return encodeStreamingValue(value2, bufferFactory, hints, sequenceWriter, byteBuilder, separator);
                });
            } catch (IOException ex) {
                return Flux.error(ex);
            }
        }
        ResolvableType listType = ResolvableType.forClassWithGenerics(List.class, elementType);
        return Flux.from(inputStream).collectList().map(list -> {
            return encodeValue(list, bufferFactory, listType, mimeType, hints);
        }).flux();
    }

    @Override // org.springframework.core.codec.Encoder
    public DataBuffer encodeValue(Object value, DataBufferFactory bufferFactory, ResolvableType valueType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        ObjectWriter writer = createObjectWriter(valueType, mimeType, hints);
        ByteArrayBuilder byteBuilder = new ByteArrayBuilder(writer.getFactory()._getBufferRecycler());
        JsonEncoding encoding = getJsonEncoding(mimeType);
        logValue(hints, value);
        try {
            JsonGenerator generator = getObjectMapper().getFactory().createGenerator(byteBuilder, encoding);
            writer.writeValue(generator, value);
            generator.flush();
            byte[] bytes = byteBuilder.toByteArray();
            DataBuffer buffer = bufferFactory.allocateBuffer(bytes.length);
            buffer.write(bytes);
            return buffer;
        } catch (InvalidDefinitionException ex) {
            throw new CodecException("Type definition error: " + ex.getType(), ex);
        } catch (JsonProcessingException ex2) {
            throw new EncodingException("JSON encoding error: " + ex2.getOriginalMessage(), ex2);
        } catch (IOException ex3) {
            throw new IllegalStateException("Unexpected I/O error while writing to byte array builder", ex3);
        }
    }

    private DataBuffer encodeStreamingValue(Object value, DataBufferFactory bufferFactory, @Nullable Map<String, Object> hints, SequenceWriter sequenceWriter, ByteArrayBuilder byteArrayBuilder, byte[] separator) {
        int offset;
        int length;
        logValue(hints, value);
        try {
            sequenceWriter.write(value);
            sequenceWriter.flush();
            byte[] bytes = byteArrayBuilder.toByteArray();
            byteArrayBuilder.reset();
            if (bytes.length > 0 && bytes[0] == 32) {
                offset = 1;
                length = bytes.length - 1;
            } else {
                offset = 0;
                length = bytes.length;
            }
            DataBuffer buffer = bufferFactory.allocateBuffer(length + separator.length);
            buffer.write(bytes, offset, length);
            buffer.write(separator);
            return buffer;
        } catch (InvalidDefinitionException ex) {
            throw new CodecException("Type definition error: " + ex.getType(), ex);
        } catch (JsonProcessingException ex2) {
            throw new EncodingException("JSON encoding error: " + ex2.getOriginalMessage(), ex2);
        } catch (IOException ex3) {
            throw new IllegalStateException("Unexpected I/O error while writing to byte array builder", ex3);
        }
    }

    private void logValue(@Nullable Map<String, Object> hints, Object value) {
        if (!Hints.isLoggingSuppressed(hints)) {
            LogFormatUtils.traceDebug(this.logger, traceOn -> {
                String formatted = LogFormatUtils.formatValue(value, !traceOn.booleanValue());
                return Hints.getLogPrefix(hints) + "Encoding [" + formatted + "]";
            });
        }
    }

    private ObjectWriter createObjectWriter(ResolvableType valueType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        JavaType javaType = getJavaType(valueType.getType(), null);
        Class<?> jsonView = hints != null ? (Class) hints.get(Jackson2CodecSupport.JSON_VIEW_HINT) : null;
        ObjectWriter writer = jsonView != null ? getObjectMapper().writerWithView(jsonView) : getObjectMapper().writer();
        if (javaType.isContainerType()) {
            writer = writer.forType(javaType);
        }
        return customizeWriter(writer, mimeType, valueType, hints);
    }

    protected ObjectWriter customizeWriter(ObjectWriter writer, @Nullable MimeType mimeType, ResolvableType elementType, @Nullable Map<String, Object> hints) {
        return writer;
    }

    @Nullable
    private byte[] streamSeparator(@Nullable MimeType mimeType) {
        for (MediaType streamingMediaType : this.streamingMediaTypes) {
            if (streamingMediaType.isCompatibleWith(mimeType)) {
                return STREAM_SEPARATORS.getOrDefault(streamingMediaType, NEWLINE_SEPARATOR);
            }
        }
        return null;
    }

    protected JsonEncoding getJsonEncoding(@Nullable MimeType mimeType) {
        JsonEncoding[] values;
        if (mimeType != null && mimeType.getCharset() != null) {
            Charset charset = mimeType.getCharset();
            for (JsonEncoding encoding : JsonEncoding.values()) {
                if (charset.name().equals(encoding.getJavaName())) {
                    return encoding;
                }
            }
        }
        return JsonEncoding.UTF8;
    }

    @Override // org.springframework.core.codec.Encoder
    public List<MimeType> getEncodableMimeTypes() {
        return getMimeTypes();
    }

    @Override // org.springframework.http.codec.HttpMessageEncoder
    public List<MediaType> getStreamingMediaTypes() {
        return Collections.unmodifiableList(this.streamingMediaTypes);
    }

    @Override // org.springframework.http.codec.HttpMessageEncoder
    public Map<String, Object> getEncodeHints(@Nullable ResolvableType actualType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response) {
        return actualType != null ? getHints(actualType) : Hints.none();
    }

    @Override // org.springframework.http.codec.json.Jackson2CodecSupport
    protected <A extends Annotation> A getAnnotation(MethodParameter parameter, Class<A> annotType) {
        return (A) parameter.getMethodAnnotation(annotType);
    }
}
