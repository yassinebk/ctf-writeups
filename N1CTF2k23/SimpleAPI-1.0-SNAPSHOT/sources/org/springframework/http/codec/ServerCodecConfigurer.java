package org.springframework.http.codec;

import org.springframework.core.codec.Encoder;
import org.springframework.http.codec.CodecConfigurer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/ServerCodecConfigurer.class */
public interface ServerCodecConfigurer extends CodecConfigurer {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/ServerCodecConfigurer$ServerDefaultCodecs.class */
    public interface ServerDefaultCodecs extends CodecConfigurer.DefaultCodecs {
        void multipartReader(HttpMessageReader<?> httpMessageReader);

        void serverSentEventEncoder(Encoder<?> encoder);
    }

    @Override // org.springframework.http.codec.CodecConfigurer
    ServerDefaultCodecs defaultCodecs();

    @Override // org.springframework.http.codec.CodecConfigurer
    ServerCodecConfigurer clone();

    static ServerCodecConfigurer create() {
        return (ServerCodecConfigurer) CodecConfigurerFactory.create(ServerCodecConfigurer.class);
    }
}
