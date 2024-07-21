package org.springframework.http.codec;

import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;
import org.springframework.http.codec.CodecConfigurer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/ClientCodecConfigurer.class */
public interface ClientCodecConfigurer extends CodecConfigurer {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/ClientCodecConfigurer$ClientDefaultCodecs.class */
    public interface ClientDefaultCodecs extends CodecConfigurer.DefaultCodecs {
        MultipartCodecs multipartCodecs();

        void serverSentEventDecoder(Decoder<?> decoder);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/ClientCodecConfigurer$MultipartCodecs.class */
    public interface MultipartCodecs {
        MultipartCodecs encoder(Encoder<?> encoder);

        MultipartCodecs writer(HttpMessageWriter<?> httpMessageWriter);
    }

    @Override // org.springframework.http.codec.CodecConfigurer
    ClientDefaultCodecs defaultCodecs();

    @Override // org.springframework.http.codec.CodecConfigurer
    ClientCodecConfigurer clone();

    static ClientCodecConfigurer create() {
        return (ClientCodecConfigurer) CodecConfigurerFactory.create(ClientCodecConfigurer.class);
    }
}
