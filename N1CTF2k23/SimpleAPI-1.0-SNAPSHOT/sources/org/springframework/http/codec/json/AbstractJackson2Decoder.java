package org.springframework.http.codec.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.reactivestreams.Publisher;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.CodecException;
import org.springframework.core.codec.DecodingException;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.codec.HttpMessageDecoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/json/AbstractJackson2Decoder.class */
public abstract class AbstractJackson2Decoder extends Jackson2CodecSupport implements HttpMessageDecoder<Object> {
    private int maxInMemorySize;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractJackson2Decoder(ObjectMapper mapper, MimeType... mimeTypes) {
        super(mapper, mimeTypes);
        this.maxInMemorySize = 262144;
    }

    public void setMaxInMemorySize(int byteCount) {
        this.maxInMemorySize = byteCount;
    }

    public int getMaxInMemorySize() {
        return this.maxInMemorySize;
    }

    @Override // org.springframework.core.codec.Decoder
    public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
        JavaType javaType = getObjectMapper().getTypeFactory().constructType(elementType.getType());
        return !CharSequence.class.isAssignableFrom(elementType.toClass()) && getObjectMapper().canDeserialize(javaType) && supportsMimeType(mimeType);
    }

    public Flux<Object> decode(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        ObjectMapper mapper = getObjectMapper();
        boolean forceUseOfBigDecimal = mapper.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        if (BigDecimal.class.equals(elementType.getType())) {
            forceUseOfBigDecimal = true;
        }
        Flux<DataBuffer> processed = processInput(input, elementType, mimeType, hints);
        Flux<TokenBuffer> tokens = Jackson2Tokenizer.tokenize(processed, mapper.getFactory(), mapper, true, forceUseOfBigDecimal, getMaxInMemorySize());
        ObjectReader reader = getObjectReader(elementType, hints);
        return tokens.handle(tokenBuffer, sink -> {
            try {
                Object value = reader.readValue(tokenBuffer.asParser(getObjectMapper()));
                logValue(value, hints);
                if (value != null) {
                    sink.next(value);
                }
            } catch (IOException ex) {
                sink.error(processException(ex));
            }
        });
    }

    protected Flux<DataBuffer> processInput(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        return Flux.from(input);
    }

    @Override // org.springframework.core.codec.Decoder
    public Mono<Object> decodeToMono(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        return DataBufferUtils.join(input, this.maxInMemorySize).flatMap(dataBuffer -> {
            return Mono.justOrEmpty(decode(dataBuffer, elementType, mimeType, hints));
        });
    }

    @Override // org.springframework.core.codec.Decoder
    public Object decode(DataBuffer dataBuffer, ResolvableType targetType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) throws DecodingException {
        try {
            try {
                ObjectReader objectReader = getObjectReader(targetType, hints);
                Object value = objectReader.readValue(dataBuffer.asInputStream());
                logValue(value, hints);
                DataBufferUtils.release(dataBuffer);
                return value;
            } catch (IOException ex) {
                throw processException(ex);
            }
        } catch (Throwable th) {
            DataBufferUtils.release(dataBuffer);
            throw th;
        }
    }

    private ObjectReader getObjectReader(ResolvableType elementType, @Nullable Map<String, Object> hints) {
        Assert.notNull(elementType, "'elementType' must not be null");
        Class<?> contextClass = getContextClass(elementType);
        if (contextClass == null && hints != null) {
            contextClass = getContextClass((ResolvableType) hints.get(ACTUAL_TYPE_HINT));
        }
        JavaType javaType = getJavaType(elementType.getType(), contextClass);
        Class<?> jsonView = hints != null ? (Class) hints.get(Jackson2CodecSupport.JSON_VIEW_HINT) : null;
        if (jsonView != null) {
            return getObjectMapper().readerWithView(jsonView).forType(javaType);
        }
        return getObjectMapper().readerFor(javaType);
    }

    @Nullable
    private Class<?> getContextClass(@Nullable ResolvableType elementType) {
        MethodParameter param = elementType != null ? getParameter(elementType) : null;
        if (param != null) {
            return param.getContainingClass();
        }
        return null;
    }

    private void logValue(@Nullable Object value, @Nullable Map<String, Object> hints) {
        if (!Hints.isLoggingSuppressed(hints)) {
            LogFormatUtils.traceDebug(this.logger, traceOn -> {
                String formatted = LogFormatUtils.formatValue(value, !traceOn.booleanValue());
                return Hints.getLogPrefix(hints) + "Decoded [" + formatted + "]";
            });
        }
    }

    private CodecException processException(IOException ex) {
        if (ex instanceof InvalidDefinitionException) {
            JavaType type = ((InvalidDefinitionException) ex).getType();
            return new CodecException("Type definition error: " + type, ex);
        } else if (ex instanceof JsonProcessingException) {
            String originalMessage = ((JsonProcessingException) ex).getOriginalMessage();
            return new DecodingException("JSON decoding error: " + originalMessage, ex);
        } else {
            return new DecodingException("I/O error while parsing input stream", ex);
        }
    }

    @Override // org.springframework.http.codec.HttpMessageDecoder
    public Map<String, Object> getDecodeHints(ResolvableType actualType, ResolvableType elementType, ServerHttpRequest request, ServerHttpResponse response) {
        return getHints(actualType);
    }

    @Override // org.springframework.core.codec.Decoder
    public List<MimeType> getDecodableMimeTypes() {
        return getMimeTypes();
    }

    @Override // org.springframework.http.codec.json.Jackson2CodecSupport
    protected <A extends Annotation> A getAnnotation(MethodParameter parameter, Class<A> annotType) {
        return (A) parameter.getParameterAnnotation(annotType);
    }
}
