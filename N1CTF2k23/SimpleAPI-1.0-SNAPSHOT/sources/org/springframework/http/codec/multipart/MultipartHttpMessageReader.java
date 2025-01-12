package org.springframework.http.codec.multipart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/MultipartHttpMessageReader.class */
public class MultipartHttpMessageReader extends LoggingCodecSupport implements HttpMessageReader<MultiValueMap<String, Part>> {
    private static final ResolvableType MULTIPART_VALUE_TYPE = ResolvableType.forClassWithGenerics(MultiValueMap.class, String.class, Part.class);
    static final List<MediaType> MIME_TYPES = Collections.unmodifiableList(Arrays.asList(MediaType.MULTIPART_FORM_DATA, MediaType.MULTIPART_MIXED, MediaType.MULTIPART_RELATED));
    private final HttpMessageReader<Part> partReader;

    public MultipartHttpMessageReader(HttpMessageReader<Part> partReader) {
        Assert.notNull(partReader, "'partReader' is required");
        this.partReader = partReader;
    }

    public HttpMessageReader<Part> getPartReader() {
        return this.partReader;
    }

    @Override // org.springframework.http.codec.HttpMessageReader
    public List<MediaType> getReadableMediaTypes() {
        return MIME_TYPES;
    }

    @Override // org.springframework.http.codec.HttpMessageReader
    public boolean canRead(ResolvableType elementType, @Nullable MediaType mediaType) {
        if (MULTIPART_VALUE_TYPE.isAssignableFrom(elementType)) {
            if (mediaType == null) {
                return true;
            }
            for (MediaType supportedMediaType : MIME_TYPES) {
                if (supportedMediaType.isCompatibleWith(mediaType)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // org.springframework.http.codec.HttpMessageReader
    public Flux<MultiValueMap<String, Part>> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
        return Flux.from(readMono(elementType, message, hints));
    }

    @Override // org.springframework.http.codec.HttpMessageReader
    public Mono<MultiValueMap<String, Part>> readMono(ResolvableType elementType, ReactiveHttpInputMessage inputMessage, Map<String, Object> hints) {
        Map<String, Object> allHints = Hints.merge(hints, Hints.SUPPRESS_LOGGING_HINT, true);
        return this.partReader.read(elementType, inputMessage, allHints).collectMultimap((v0) -> {
            return v0.name();
        }).doOnNext(map -> {
            LogFormatUtils.traceDebug(this.logger, traceOn -> {
                String str;
                StringBuilder append = new StringBuilder().append(Hints.getLogPrefix(hints)).append("Parsed ");
                if (isEnableLoggingRequestDetails()) {
                    str = LogFormatUtils.formatValue(map, !traceOn.booleanValue());
                } else {
                    str = "parts " + map.keySet() + " (content masked)";
                }
                return append.append(str).toString();
            });
        }).map(this::toMultiValueMap);
    }

    private LinkedMultiValueMap<String, Part> toMultiValueMap(Map<String, Collection<Part>> map) {
        return new LinkedMultiValueMap<>((Map) map.entrySet().stream().collect(Collectors.toMap((v0) -> {
            return v0.getKey();
        }, e -> {
            return toList((Collection) e.getValue());
        })));
    }

    private List<Part> toList(Collection<Part> collection) {
        return collection instanceof List ? (List) collection : new ArrayList(collection);
    }
}
