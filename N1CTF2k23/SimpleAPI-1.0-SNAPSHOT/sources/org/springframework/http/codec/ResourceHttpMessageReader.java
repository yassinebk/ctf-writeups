package org.springframework.http.codec;

import java.util.Map;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.core.codec.ResourceDecoder;
import org.springframework.core.io.Resource;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/ResourceHttpMessageReader.class */
public class ResourceHttpMessageReader extends DecoderHttpMessageReader<Resource> {
    public ResourceHttpMessageReader() {
        super(new ResourceDecoder());
    }

    public ResourceHttpMessageReader(ResourceDecoder resourceDecoder) {
        super(resourceDecoder);
    }

    @Override // org.springframework.http.codec.DecoderHttpMessageReader
    protected Map<String, Object> getReadHints(ResolvableType actualType, ResolvableType elementType, ServerHttpRequest request, ServerHttpResponse response) {
        String name = request.getHeaders().getContentDisposition().getFilename();
        return StringUtils.hasText(name) ? Hints.from(ResourceDecoder.FILENAME_HINT, name) : Hints.none();
    }
}
