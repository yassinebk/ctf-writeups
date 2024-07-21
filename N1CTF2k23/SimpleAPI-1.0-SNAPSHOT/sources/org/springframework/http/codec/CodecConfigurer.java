package org.springframework.http.codec;

import java.util.List;
import java.util.function.Consumer;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/CodecConfigurer.class */
public interface CodecConfigurer {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/CodecConfigurer$CustomCodecs.class */
    public interface CustomCodecs {
        void register(Object obj);

        void registerWithDefaultConfig(Object obj);

        void registerWithDefaultConfig(Object obj, Consumer<DefaultCodecConfig> consumer);

        @Deprecated
        void decoder(Decoder<?> decoder);

        @Deprecated
        void encoder(Encoder<?> encoder);

        @Deprecated
        void reader(HttpMessageReader<?> httpMessageReader);

        @Deprecated
        void writer(HttpMessageWriter<?> httpMessageWriter);

        @Deprecated
        void withDefaultCodecConfig(Consumer<DefaultCodecConfig> consumer);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/CodecConfigurer$DefaultCodecConfig.class */
    public interface DefaultCodecConfig {
        @Nullable
        Integer maxInMemorySize();

        @Nullable
        Boolean isEnableLoggingRequestDetails();
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/CodecConfigurer$DefaultCodecs.class */
    public interface DefaultCodecs {
        void jackson2JsonDecoder(Decoder<?> decoder);

        void jackson2JsonEncoder(Encoder<?> encoder);

        void jackson2SmileDecoder(Decoder<?> decoder);

        void jackson2SmileEncoder(Encoder<?> encoder);

        void protobufDecoder(Decoder<?> decoder);

        void protobufEncoder(Encoder<?> encoder);

        void jaxb2Decoder(Decoder<?> decoder);

        void jaxb2Encoder(Encoder<?> encoder);

        void maxInMemorySize(int i);

        void enableLoggingRequestDetails(boolean z);
    }

    DefaultCodecs defaultCodecs();

    CustomCodecs customCodecs();

    void registerDefaults(boolean z);

    List<HttpMessageReader<?>> getReaders();

    List<HttpMessageWriter<?>> getWriters();

    CodecConfigurer clone();
}
