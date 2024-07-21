package org.springframework.http.codec.support;

import java.util.List;
import org.springframework.core.codec.Encoder;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.ServerSentEventHttpMessageWriter;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/support/ServerDefaultCodecsImpl.class */
class ServerDefaultCodecsImpl extends BaseDefaultCodecs implements ServerCodecConfigurer.ServerDefaultCodecs {
    @Nullable
    private HttpMessageReader<?> multipartReader;
    @Nullable
    private Encoder<?> sseEncoder;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ServerDefaultCodecsImpl() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ServerDefaultCodecsImpl(ServerDefaultCodecsImpl other) {
        super(other);
        this.multipartReader = other.multipartReader;
        this.sseEncoder = other.sseEncoder;
    }

    @Override // org.springframework.http.codec.ServerCodecConfigurer.ServerDefaultCodecs
    public void multipartReader(HttpMessageReader<?> reader) {
        this.multipartReader = reader;
    }

    @Override // org.springframework.http.codec.ServerCodecConfigurer.ServerDefaultCodecs
    public void serverSentEventEncoder(Encoder<?> encoder) {
        this.sseEncoder = encoder;
    }

    @Override // org.springframework.http.codec.support.BaseDefaultCodecs
    protected void extendTypedReaders(List<HttpMessageReader<?>> typedReaders) {
        if (this.multipartReader != null) {
            addCodec(typedReaders, this.multipartReader);
        } else if (synchronossMultipartPresent) {
            SynchronossPartHttpMessageReader partReader = new SynchronossPartHttpMessageReader();
            addCodec(typedReaders, partReader);
            addCodec(typedReaders, new MultipartHttpMessageReader(partReader));
        }
    }

    @Override // org.springframework.http.codec.support.BaseDefaultCodecs
    protected void extendObjectWriters(List<HttpMessageWriter<?>> objectWriters) {
        objectWriters.add(new ServerSentEventHttpMessageWriter(getSseEncoder()));
    }

    @Nullable
    private Encoder<?> getSseEncoder() {
        if (this.sseEncoder != null) {
            return this.sseEncoder;
        }
        if (jackson2Present) {
            return getJackson2JsonEncoder();
        }
        return null;
    }
}
