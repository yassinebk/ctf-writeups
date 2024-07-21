package org.springframework.core.codec;

import java.util.Map;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/ByteArrayDecoder.class */
public class ByteArrayDecoder extends AbstractDataBufferDecoder<byte[]> {
    @Override // org.springframework.core.codec.Decoder
    public /* bridge */ /* synthetic */ Object decode(DataBuffer dataBuffer, ResolvableType resolvableType, @Nullable MimeType mimeType, @Nullable Map map) throws DecodingException {
        return decode(dataBuffer, resolvableType, mimeType, (Map<String, Object>) map);
    }

    public ByteArrayDecoder() {
        super(MimeTypeUtils.ALL);
    }

    @Override // org.springframework.core.codec.AbstractDecoder, org.springframework.core.codec.Decoder
    public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
        return elementType.resolve() == byte[].class && super.canDecode(elementType, mimeType);
    }

    @Override // org.springframework.core.codec.Decoder
    public byte[] decode(DataBuffer dataBuffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        byte[] result = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(result);
        DataBufferUtils.release(dataBuffer);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(Hints.getLogPrefix(hints) + "Read " + result.length + " bytes");
        }
        return result;
    }
}
