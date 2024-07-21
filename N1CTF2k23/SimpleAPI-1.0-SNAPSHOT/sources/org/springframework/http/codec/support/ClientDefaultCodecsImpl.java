package org.springframework.http.codec.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.FormHttpMessageWriter;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerSentEventHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageWriter;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/support/ClientDefaultCodecsImpl.class */
class ClientDefaultCodecsImpl extends BaseDefaultCodecs implements ClientCodecConfigurer.ClientDefaultCodecs {
    @Nullable
    private DefaultMultipartCodecs multipartCodecs;
    @Nullable
    private Decoder<?> sseDecoder;
    @Nullable
    private Supplier<List<HttpMessageWriter<?>>> partWritersSupplier;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClientDefaultCodecsImpl() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClientDefaultCodecsImpl(ClientDefaultCodecsImpl other) {
        super(other);
        this.multipartCodecs = other.multipartCodecs != null ? new DefaultMultipartCodecs(other.multipartCodecs) : null;
        this.sseDecoder = other.sseDecoder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPartWritersSupplier(Supplier<List<HttpMessageWriter<?>>> supplier) {
        this.partWritersSupplier = supplier;
    }

    @Override // org.springframework.http.codec.ClientCodecConfigurer.ClientDefaultCodecs
    public ClientCodecConfigurer.MultipartCodecs multipartCodecs() {
        if (this.multipartCodecs == null) {
            this.multipartCodecs = new DefaultMultipartCodecs();
        }
        return this.multipartCodecs;
    }

    @Override // org.springframework.http.codec.ClientCodecConfigurer.ClientDefaultCodecs
    public void serverSentEventDecoder(Decoder<?> decoder) {
        this.sseDecoder = decoder;
    }

    /* renamed from: clone */
    public ClientDefaultCodecsImpl m1604clone() {
        ClientDefaultCodecsImpl codecs = new ClientDefaultCodecsImpl();
        codecs.multipartCodecs = this.multipartCodecs;
        codecs.sseDecoder = this.sseDecoder;
        codecs.partWritersSupplier = this.partWritersSupplier;
        return codecs;
    }

    @Override // org.springframework.http.codec.support.BaseDefaultCodecs
    protected void extendObjectReaders(List<HttpMessageReader<?>> objectReaders) {
        Decoder<?> jackson2JsonDecoder;
        if (this.sseDecoder != null) {
            jackson2JsonDecoder = this.sseDecoder;
        } else {
            jackson2JsonDecoder = jackson2Present ? getJackson2JsonDecoder() : null;
        }
        Decoder<?> decoder = jackson2JsonDecoder;
        addCodec(objectReaders, new ServerSentEventHttpMessageReader(decoder));
    }

    @Override // org.springframework.http.codec.support.BaseDefaultCodecs
    protected void extendTypedWriters(List<HttpMessageWriter<?>> typedWriters) {
        addCodec(typedWriters, new MultipartHttpMessageWriter(getPartWriters(), new FormHttpMessageWriter()));
    }

    private List<HttpMessageWriter<?>> getPartWriters() {
        if (this.multipartCodecs != null) {
            return this.multipartCodecs.getWriters();
        }
        if (this.partWritersSupplier != null) {
            return this.partWritersSupplier.get();
        }
        return Collections.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/support/ClientDefaultCodecsImpl$DefaultMultipartCodecs.class */
    public static class DefaultMultipartCodecs implements ClientCodecConfigurer.MultipartCodecs {
        private final List<HttpMessageWriter<?>> writers = new ArrayList();

        DefaultMultipartCodecs() {
        }

        DefaultMultipartCodecs(DefaultMultipartCodecs other) {
            this.writers.addAll(other.writers);
        }

        @Override // org.springframework.http.codec.ClientCodecConfigurer.MultipartCodecs
        public ClientCodecConfigurer.MultipartCodecs encoder(Encoder<?> encoder) {
            writer(new EncoderHttpMessageWriter(encoder));
            return this;
        }

        @Override // org.springframework.http.codec.ClientCodecConfigurer.MultipartCodecs
        public ClientCodecConfigurer.MultipartCodecs writer(HttpMessageWriter<?> writer) {
            this.writers.add(writer);
            return this;
        }

        List<HttpMessageWriter<?>> getWriters() {
            return this.writers;
        }
    }
}
