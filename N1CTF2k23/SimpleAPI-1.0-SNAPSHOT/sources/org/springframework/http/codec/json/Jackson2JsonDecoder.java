package org.springframework.http.codec.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/json/Jackson2JsonDecoder.class */
public class Jackson2JsonDecoder extends AbstractJackson2Decoder {
    private static final StringDecoder STRING_DECODER = StringDecoder.textPlainOnly(Arrays.asList(",", "\n"), false);
    private static final ResolvableType STRING_TYPE = ResolvableType.forClass(String.class);

    public Jackson2JsonDecoder() {
        super(Jackson2ObjectMapperBuilder.json().build(), new MimeType[0]);
    }

    public Jackson2JsonDecoder(ObjectMapper mapper, MimeType... mimeTypes) {
        super(mapper, mimeTypes);
    }

    @Override // org.springframework.http.codec.json.AbstractJackson2Decoder
    protected Flux<DataBuffer> processInput(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        Flux<DataBuffer> flux = Flux.from(input);
        if (mimeType == null) {
            return flux;
        }
        Charset charset = mimeType.getCharset();
        if (charset == null || StandardCharsets.UTF_8.equals(charset) || StandardCharsets.US_ASCII.equals(charset)) {
            return flux;
        }
        MimeType textMimeType = new MimeType(MimeTypeUtils.TEXT_PLAIN, charset);
        Flux<String> decoded = STRING_DECODER.decode(input, STRING_TYPE, textMimeType, (Map<String, Object>) null);
        DataBufferFactory factory = new DefaultDataBufferFactory();
        return decoded.map(s -> {
            return factory.wrap(s.getBytes(StandardCharsets.UTF_8));
        });
    }
}
