package org.springframework.core.codec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.OptionalLong;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/ResourceRegionEncoder.class */
public class ResourceRegionEncoder extends AbstractEncoder<ResourceRegion> {
    public static final int DEFAULT_BUFFER_SIZE = 4096;
    public static final String BOUNDARY_STRING_HINT = ResourceRegionEncoder.class.getName() + ".boundaryString";
    private final int bufferSize;

    public ResourceRegionEncoder() {
        this(4096);
    }

    public ResourceRegionEncoder(int bufferSize) {
        super(MimeTypeUtils.APPLICATION_OCTET_STREAM, MimeTypeUtils.ALL);
        Assert.isTrue(bufferSize > 0, "'bufferSize' must be larger than 0");
        this.bufferSize = bufferSize;
    }

    @Override // org.springframework.core.codec.AbstractEncoder, org.springframework.core.codec.Encoder
    public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
        return super.canEncode(elementType, mimeType) && ResourceRegion.class.isAssignableFrom(elementType.toClass());
    }

    @Override // org.springframework.core.codec.Encoder
    public Flux<DataBuffer> encode(Publisher<? extends ResourceRegion> input, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        Assert.notNull(input, "'inputStream' must not be null");
        Assert.notNull(bufferFactory, "'bufferFactory' must not be null");
        Assert.notNull(elementType, "'elementType' must not be null");
        if (input instanceof Mono) {
            return Mono.from(input).flatMapMany(region -> {
                if (!region.getResource().isReadable()) {
                    return Flux.error(new EncodingException("Resource " + region.getResource() + " is not readable"));
                }
                return writeResourceRegion(region, bufferFactory, hints);
            });
        }
        String boundaryString = (String) Hints.getRequiredHint(hints, BOUNDARY_STRING_HINT);
        byte[] startBoundary = toAsciiBytes("\r\n--" + boundaryString + "\r\n");
        byte[] contentType = mimeType != null ? toAsciiBytes("Content-Type: " + mimeType + "\r\n") : new byte[0];
        return Flux.from(input).concatMap(region2 -> {
            if (!region2.getResource().isReadable()) {
                return Flux.error(new EncodingException("Resource " + region2.getResource() + " is not readable"));
            }
            Flux<DataBuffer> prefix = Flux.just(new DataBuffer[]{bufferFactory.wrap(startBoundary), bufferFactory.wrap(contentType), bufferFactory.wrap(getContentRangeHeader(region2))});
            return prefix.concatWith(writeResourceRegion(region2, bufferFactory, hints));
        }).concatWithValues(new DataBuffer[]{getRegionSuffix(bufferFactory, boundaryString)});
    }

    private Flux<DataBuffer> writeResourceRegion(ResourceRegion region, DataBufferFactory bufferFactory, @Nullable Map<String, Object> hints) {
        Resource resource = region.getResource();
        long position = region.getPosition();
        long count = region.getCount();
        if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
            this.logger.debug(Hints.getLogPrefix(hints) + "Writing region " + position + "-" + (position + count) + " of [" + resource + "]");
        }
        Flux<DataBuffer> in = DataBufferUtils.read(resource, position, bufferFactory, this.bufferSize);
        return DataBufferUtils.takeUntilByteCount(in, count);
    }

    private DataBuffer getRegionSuffix(DataBufferFactory bufferFactory, String boundaryString) {
        byte[] endBoundary = toAsciiBytes("\r\n--" + boundaryString + "--");
        return bufferFactory.wrap(endBoundary);
    }

    private byte[] toAsciiBytes(String in) {
        return in.getBytes(StandardCharsets.US_ASCII);
    }

    private byte[] getContentRangeHeader(ResourceRegion region) {
        long start = region.getPosition();
        long end = (start + region.getCount()) - 1;
        OptionalLong contentLength = contentLength(region.getResource());
        if (contentLength.isPresent()) {
            long length = contentLength.getAsLong();
            return toAsciiBytes("Content-Range: bytes " + start + '-' + end + '/' + length + "\r\n\r\n");
        }
        return toAsciiBytes("Content-Range: bytes " + start + '-' + end + "\r\n\r\n");
    }

    private OptionalLong contentLength(Resource resource) {
        if (InputStreamResource.class != resource.getClass()) {
            try {
                return OptionalLong.of(resource.contentLength());
            } catch (IOException e) {
            }
        }
        return OptionalLong.empty();
    }
}
