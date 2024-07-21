package org.springframework.core.codec;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DataBufferWrapper;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.core.io.buffer.LimitedDataBufferList;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/StringDecoder.class */
public final class StringDecoder extends AbstractDataBufferDecoder<String> {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final List<String> DEFAULT_DELIMITERS = Arrays.asList("\r\n", "\n");
    private final List<String> delimiters;
    private final boolean stripDelimiter;
    private final ConcurrentMap<Charset, byte[][]> delimitersCache;

    @Override // org.springframework.core.codec.Decoder
    public /* bridge */ /* synthetic */ Object decode(DataBuffer dataBuffer, ResolvableType resolvableType, @Nullable MimeType mimeType, @Nullable Map map) throws DecodingException {
        return decode(dataBuffer, resolvableType, mimeType, (Map<String, Object>) map);
    }

    private StringDecoder(List<String> delimiters, boolean stripDelimiter, MimeType... mimeTypes) {
        super(mimeTypes);
        this.delimitersCache = new ConcurrentHashMap();
        Assert.notEmpty(delimiters, "'delimiters' must not be empty");
        this.delimiters = new ArrayList(delimiters);
        this.stripDelimiter = stripDelimiter;
    }

    @Override // org.springframework.core.codec.AbstractDecoder, org.springframework.core.codec.Decoder
    public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
        return elementType.resolve() == String.class && super.canDecode(elementType, mimeType);
    }

    @Override // org.springframework.core.codec.AbstractDataBufferDecoder, org.springframework.core.codec.Decoder
    public Flux<String> decode(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        byte[][] delimiterBytes = getDelimiterBytes(mimeType);
        Flux<DataBuffer> inputFlux = Flux.defer(() -> {
            DataBufferUtils.Matcher matcher = DataBufferUtils.matcher(delimiterBytes);
            LimitChecker limiter = new LimitChecker(getMaxInMemorySize());
            return Flux.from(input).concatMapIterable(buffer -> {
                return endFrameAfterDelimiter(buffer, matcher);
            }).doOnNext(limiter).bufferUntil(buffer2 -> {
                return buffer2 instanceof EndFrameBuffer;
            }).map(list -> {
                return joinAndStrip(list, this.stripDelimiter);
            }).doOnDiscard(PooledDataBuffer.class, (v0) -> {
                DataBufferUtils.release(v0);
            });
        });
        return super.decode((Publisher<DataBuffer>) inputFlux, elementType, mimeType, hints);
    }

    private byte[][] getDelimiterBytes(@Nullable MimeType mimeType) {
        return this.delimitersCache.computeIfAbsent(getCharset(mimeType), charset -> {
            ?? r0 = new byte[this.delimiters.size()];
            for (int i = 0; i < this.delimiters.size(); i++) {
                r0[i] = this.delimiters.get(i).getBytes(charset);
            }
            return r0;
        });
    }

    @Override // org.springframework.core.codec.Decoder
    public String decode(DataBuffer dataBuffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        Charset charset = getCharset(mimeType);
        CharBuffer charBuffer = charset.decode(dataBuffer.asByteBuffer());
        DataBufferUtils.release(dataBuffer);
        String value = charBuffer.toString();
        LogFormatUtils.traceDebug(this.logger, traceOn -> {
            String formatted = LogFormatUtils.formatValue(value, !traceOn.booleanValue());
            return Hints.getLogPrefix(hints) + "Decoded " + formatted;
        });
        return value;
    }

    private static Charset getCharset(@Nullable MimeType mimeType) {
        if (mimeType != null && mimeType.getCharset() != null) {
            return mimeType.getCharset();
        }
        return DEFAULT_CHARSET;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x005b, code lost:
        r0.add(org.springframework.core.io.buffer.DataBufferUtils.retain(r5));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<org.springframework.core.io.buffer.DataBuffer> endFrameAfterDelimiter(org.springframework.core.io.buffer.DataBuffer r5, org.springframework.core.io.buffer.DataBufferUtils.Matcher r6) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r7 = r0
        L8:
            r0 = r6
            r1 = r5
            int r0 = r0.match(r1)     // Catch: java.lang.Throwable -> L7a
            r8 = r0
            r0 = r8
            r1 = -1
            if (r0 == r1) goto L5b
            r0 = r5
            int r0 = r0.readPosition()     // Catch: java.lang.Throwable -> L7a
            r9 = r0
            r0 = r8
            r1 = r9
            int r0 = r0 - r1
            r1 = 1
            int r0 = r0 + r1
            r10 = r0
            r0 = r5
            r1 = r9
            r2 = r10
            org.springframework.core.io.buffer.DataBuffer r0 = r0.retainedSlice(r1, r2)     // Catch: java.lang.Throwable -> L7a
            r11 = r0
            r0 = r7
            r1 = r11
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L7a
            r0 = r7
            org.springframework.core.codec.StringDecoder$EndFrameBuffer r1 = new org.springframework.core.codec.StringDecoder$EndFrameBuffer     // Catch: java.lang.Throwable -> L7a
            r2 = r1
            r3 = r6
            byte[] r3 = r3.delimiter()     // Catch: java.lang.Throwable -> L7a
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L7a
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L7a
            r0 = r5
            r1 = r8
            r2 = 1
            int r1 = r1 + r2
            org.springframework.core.io.buffer.DataBuffer r0 = r0.readPosition(r1)     // Catch: java.lang.Throwable -> L7a
            goto L69
        L5b:
            r0 = r7
            r1 = r5
            org.springframework.core.io.buffer.DataBuffer r1 = org.springframework.core.io.buffer.DataBufferUtils.retain(r1)     // Catch: java.lang.Throwable -> L7a
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L7a
            goto L72
        L69:
            r0 = r5
            int r0 = r0.readableByteCount()     // Catch: java.lang.Throwable -> L7a
            if (r0 > 0) goto L8
        L72:
            r0 = r5
            boolean r0 = org.springframework.core.io.buffer.DataBufferUtils.release(r0)
            goto L84
        L7a:
            r12 = move-exception
            r0 = r5
            boolean r0 = org.springframework.core.io.buffer.DataBufferUtils.release(r0)
            r0 = r12
            throw r0
        L84:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.core.codec.StringDecoder.endFrameAfterDelimiter(org.springframework.core.io.buffer.DataBuffer, org.springframework.core.io.buffer.DataBufferUtils$Matcher):java.util.List");
    }

    private static DataBuffer joinAndStrip(List<DataBuffer> dataBuffers, boolean stripDelimiter) {
        Assert.state(!dataBuffers.isEmpty(), "DataBuffers should not be empty");
        byte[] matchingDelimiter = null;
        int lastIdx = dataBuffers.size() - 1;
        DataBuffer lastBuffer = dataBuffers.get(lastIdx);
        if (lastBuffer instanceof EndFrameBuffer) {
            matchingDelimiter = ((EndFrameBuffer) lastBuffer).delimiter();
            dataBuffers.remove(lastIdx);
        }
        DataBuffer result = dataBuffers.get(0).factory().join(dataBuffers);
        if (stripDelimiter && matchingDelimiter != null) {
            result.writePosition(result.writePosition() - matchingDelimiter.length);
        }
        return result;
    }

    @Deprecated
    public static StringDecoder textPlainOnly(boolean stripDelimiter) {
        return textPlainOnly();
    }

    public static StringDecoder textPlainOnly() {
        return textPlainOnly(DEFAULT_DELIMITERS, true);
    }

    public static StringDecoder textPlainOnly(List<String> delimiters, boolean stripDelimiter) {
        return new StringDecoder(delimiters, stripDelimiter, new MimeType("text", "plain", DEFAULT_CHARSET));
    }

    @Deprecated
    public static StringDecoder allMimeTypes(boolean stripDelimiter) {
        return allMimeTypes();
    }

    public static StringDecoder allMimeTypes() {
        return allMimeTypes(DEFAULT_DELIMITERS, true);
    }

    public static StringDecoder allMimeTypes(List<String> delimiters, boolean stripDelimiter) {
        return new StringDecoder(delimiters, stripDelimiter, new MimeType("text", "plain", DEFAULT_CHARSET), MimeTypeUtils.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/StringDecoder$EndFrameBuffer.class */
    public static class EndFrameBuffer extends DataBufferWrapper {
        private static final DataBuffer BUFFER = new DefaultDataBufferFactory().wrap(new byte[0]);
        private byte[] delimiter;

        public EndFrameBuffer(byte[] delimiter) {
            super(BUFFER);
            this.delimiter = delimiter;
        }

        public byte[] delimiter() {
            return this.delimiter;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/StringDecoder$LimitChecker.class */
    private static class LimitChecker implements Consumer<DataBuffer> {
        private final LimitedDataBufferList list;

        LimitChecker(int maxInMemorySize) {
            this.list = new LimitedDataBufferList(maxInMemorySize);
        }

        @Override // java.util.function.Consumer
        public void accept(DataBuffer buffer) {
            if (buffer instanceof EndFrameBuffer) {
                this.list.clear();
            }
            try {
                this.list.add(buffer);
            } catch (DataBufferLimitException ex) {
                DataBufferUtils.release(buffer);
                throw ex;
            }
        }
    }
}
