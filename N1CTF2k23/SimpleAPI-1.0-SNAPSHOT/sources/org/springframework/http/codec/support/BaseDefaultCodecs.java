package org.springframework.http.codec.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.core.codec.AbstractDataBufferDecoder;
import org.springframework.core.codec.ByteArrayDecoder;
import org.springframework.core.codec.ByteArrayEncoder;
import org.springframework.core.codec.ByteBufferDecoder;
import org.springframework.core.codec.ByteBufferEncoder;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.codec.DataBufferDecoder;
import org.springframework.core.codec.DataBufferEncoder;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;
import org.springframework.core.codec.ResourceDecoder;
import org.springframework.core.codec.StringDecoder;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.FormHttpMessageReader;
import org.springframework.http.codec.FormHttpMessageWriter;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ResourceHttpMessageReader;
import org.springframework.http.codec.ResourceHttpMessageWriter;
import org.springframework.http.codec.ServerSentEventHttpMessageReader;
import org.springframework.http.codec.json.AbstractJackson2Decoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.json.Jackson2SmileDecoder;
import org.springframework.http.codec.json.Jackson2SmileEncoder;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageWriter;
import org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.http.codec.protobuf.ProtobufHttpMessageWriter;
import org.springframework.http.codec.support.BaseCodecConfigurer;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/support/BaseDefaultCodecs.class */
class BaseDefaultCodecs implements CodecConfigurer.DefaultCodecs, CodecConfigurer.DefaultCodecConfig {
    static final boolean jackson2Present;
    private static final boolean jackson2SmilePresent;
    private static final boolean jaxb2Present;
    private static final boolean protobufPresent;
    static final boolean synchronossMultipartPresent;
    @Nullable
    private Decoder<?> jackson2JsonDecoder;
    @Nullable
    private Encoder<?> jackson2JsonEncoder;
    @Nullable
    private Encoder<?> jackson2SmileEncoder;
    @Nullable
    private Decoder<?> jackson2SmileDecoder;
    @Nullable
    private Decoder<?> protobufDecoder;
    @Nullable
    private Encoder<?> protobufEncoder;
    @Nullable
    private Decoder<?> jaxb2Decoder;
    @Nullable
    private Encoder<?> jaxb2Encoder;
    @Nullable
    private Integer maxInMemorySize;
    @Nullable
    private Boolean enableLoggingRequestDetails;
    private boolean registerDefaults;

    static {
        ClassLoader classLoader = BaseCodecConfigurer.class.getClassLoader();
        jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader);
        jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
        jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", classLoader);
        protobufPresent = ClassUtils.isPresent("com.google.protobuf.Message", classLoader);
        synchronossMultipartPresent = ClassUtils.isPresent("org.synchronoss.cloud.nio.multipart.NioMultipartParser", classLoader);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseDefaultCodecs() {
        this.registerDefaults = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseDefaultCodecs(BaseDefaultCodecs other) {
        this.registerDefaults = true;
        this.jackson2JsonDecoder = other.jackson2JsonDecoder;
        this.jackson2JsonEncoder = other.jackson2JsonEncoder;
        this.jackson2SmileDecoder = other.jackson2SmileDecoder;
        this.jackson2SmileEncoder = other.jackson2SmileEncoder;
        this.protobufDecoder = other.protobufDecoder;
        this.protobufEncoder = other.protobufEncoder;
        this.jaxb2Decoder = other.jaxb2Decoder;
        this.jaxb2Encoder = other.jaxb2Encoder;
        this.maxInMemorySize = other.maxInMemorySize;
        this.enableLoggingRequestDetails = other.enableLoggingRequestDetails;
        this.registerDefaults = other.registerDefaults;
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecs
    public void jackson2JsonDecoder(Decoder<?> decoder) {
        this.jackson2JsonDecoder = decoder;
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecs
    public void jackson2JsonEncoder(Encoder<?> encoder) {
        this.jackson2JsonEncoder = encoder;
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecs
    public void protobufDecoder(Decoder<?> decoder) {
        this.protobufDecoder = decoder;
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecs
    public void jackson2SmileDecoder(Decoder<?> decoder) {
        this.jackson2SmileDecoder = decoder;
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecs
    public void jackson2SmileEncoder(Encoder<?> encoder) {
        this.jackson2SmileEncoder = encoder;
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecs
    public void protobufEncoder(Encoder<?> encoder) {
        this.protobufEncoder = encoder;
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecs
    public void jaxb2Decoder(Decoder<?> decoder) {
        this.jaxb2Decoder = decoder;
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecs
    public void jaxb2Encoder(Encoder<?> encoder) {
        this.jaxb2Encoder = encoder;
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecs
    public void maxInMemorySize(int byteCount) {
        this.maxInMemorySize = Integer.valueOf(byteCount);
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecConfig
    @Nullable
    public Integer maxInMemorySize() {
        return this.maxInMemorySize;
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecs
    public void enableLoggingRequestDetails(boolean enable) {
        this.enableLoggingRequestDetails = Boolean.valueOf(enable);
    }

    @Override // org.springframework.http.codec.CodecConfigurer.DefaultCodecConfig
    @Nullable
    public Boolean isEnableLoggingRequestDetails() {
        return this.enableLoggingRequestDetails;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerDefaults(boolean registerDefaults) {
        this.registerDefaults = registerDefaults;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<HttpMessageReader<?>> getTypedReaders() {
        if (!this.registerDefaults) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        addCodec(arrayList, new DecoderHttpMessageReader(new ByteArrayDecoder()));
        addCodec(arrayList, new DecoderHttpMessageReader(new ByteBufferDecoder()));
        addCodec(arrayList, new DecoderHttpMessageReader(new DataBufferDecoder()));
        addCodec(arrayList, new ResourceHttpMessageReader(new ResourceDecoder()));
        addCodec(arrayList, new DecoderHttpMessageReader(StringDecoder.textPlainOnly()));
        if (protobufPresent) {
            addCodec(arrayList, new DecoderHttpMessageReader(this.protobufDecoder != null ? (ProtobufDecoder) this.protobufDecoder : new ProtobufDecoder()));
        }
        addCodec(arrayList, new FormHttpMessageReader());
        extendTypedReaders(arrayList);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T> void addCodec(List<T> codecs, T codec) {
        initCodec(codec);
        codecs.add(codec);
    }

    private void initCodec(@Nullable Object codec) {
        if (codec instanceof DecoderHttpMessageReader) {
            codec = ((DecoderHttpMessageReader) codec).getDecoder();
        }
        if (codec == null) {
            return;
        }
        Integer size = this.maxInMemorySize;
        if (size != null) {
            if (codec instanceof AbstractDataBufferDecoder) {
                ((AbstractDataBufferDecoder) codec).setMaxInMemorySize(size.intValue());
            }
            if (protobufPresent && (codec instanceof ProtobufDecoder)) {
                ((ProtobufDecoder) codec).setMaxMessageSize(size.intValue());
            }
            if (jackson2Present && (codec instanceof AbstractJackson2Decoder)) {
                ((AbstractJackson2Decoder) codec).setMaxInMemorySize(size.intValue());
            }
            if (jaxb2Present && (codec instanceof Jaxb2XmlDecoder)) {
                ((Jaxb2XmlDecoder) codec).setMaxInMemorySize(size.intValue());
            }
            if (codec instanceof FormHttpMessageReader) {
                ((FormHttpMessageReader) codec).setMaxInMemorySize(size.intValue());
            }
            if (codec instanceof ServerSentEventHttpMessageReader) {
                ((ServerSentEventHttpMessageReader) codec).setMaxInMemorySize(size.intValue());
                initCodec(((ServerSentEventHttpMessageReader) codec).getDecoder());
            }
            if (synchronossMultipartPresent && (codec instanceof SynchronossPartHttpMessageReader)) {
                ((SynchronossPartHttpMessageReader) codec).setMaxInMemorySize(size.intValue());
            }
        }
        Boolean enable = this.enableLoggingRequestDetails;
        if (enable != null) {
            if (codec instanceof FormHttpMessageReader) {
                ((FormHttpMessageReader) codec).setEnableLoggingRequestDetails(enable.booleanValue());
            }
            if (codec instanceof MultipartHttpMessageReader) {
                ((MultipartHttpMessageReader) codec).setEnableLoggingRequestDetails(enable.booleanValue());
            }
            if (synchronossMultipartPresent && (codec instanceof SynchronossPartHttpMessageReader)) {
                ((SynchronossPartHttpMessageReader) codec).setEnableLoggingRequestDetails(enable.booleanValue());
            }
            if (codec instanceof FormHttpMessageWriter) {
                ((FormHttpMessageWriter) codec).setEnableLoggingRequestDetails(enable.booleanValue());
            }
            if (codec instanceof MultipartHttpMessageWriter) {
                ((MultipartHttpMessageWriter) codec).setEnableLoggingRequestDetails(enable.booleanValue());
            }
        }
        if (codec instanceof MultipartHttpMessageReader) {
            initCodec(((MultipartHttpMessageReader) codec).getPartReader());
        } else if (codec instanceof MultipartHttpMessageWriter) {
            initCodec(((MultipartHttpMessageWriter) codec).getFormWriter());
        }
    }

    protected void extendTypedReaders(List<HttpMessageReader<?>> typedReaders) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<HttpMessageReader<?>> getObjectReaders() {
        if (!this.registerDefaults) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        if (jackson2Present) {
            addCodec(arrayList, new DecoderHttpMessageReader(getJackson2JsonDecoder()));
        }
        if (jackson2SmilePresent) {
            addCodec(arrayList, new DecoderHttpMessageReader(this.jackson2SmileDecoder != null ? (Jackson2SmileDecoder) this.jackson2SmileDecoder : new Jackson2SmileDecoder()));
        }
        if (jaxb2Present) {
            addCodec(arrayList, new DecoderHttpMessageReader(this.jaxb2Decoder != null ? (Jaxb2XmlDecoder) this.jaxb2Decoder : new Jaxb2XmlDecoder()));
        }
        extendObjectReaders(arrayList);
        return arrayList;
    }

    protected void extendObjectReaders(List<HttpMessageReader<?>> objectReaders) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<HttpMessageReader<?>> getCatchAllReaders() {
        if (!this.registerDefaults) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        addCodec(arrayList, new DecoderHttpMessageReader(StringDecoder.allMimeTypes()));
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<HttpMessageWriter<?>> getTypedWriters() {
        if (!this.registerDefaults) {
            return Collections.emptyList();
        }
        List<HttpMessageWriter<?>> writers = getBaseTypedWriters();
        extendTypedWriters(writers);
        return writers;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<HttpMessageWriter<?>> getBaseTypedWriters() {
        if (!this.registerDefaults) {
            return Collections.emptyList();
        }
        List<HttpMessageWriter<?>> writers = new ArrayList<>();
        writers.add(new EncoderHttpMessageWriter<>(new ByteArrayEncoder()));
        writers.add(new EncoderHttpMessageWriter<>(new ByteBufferEncoder()));
        writers.add(new EncoderHttpMessageWriter<>(new DataBufferEncoder()));
        writers.add(new ResourceHttpMessageWriter());
        writers.add(new EncoderHttpMessageWriter<>(CharSequenceEncoder.textPlainOnly()));
        if (protobufPresent) {
            writers.add(new ProtobufHttpMessageWriter(this.protobufEncoder != null ? (ProtobufEncoder) this.protobufEncoder : new ProtobufEncoder()));
        }
        return writers;
    }

    protected void extendTypedWriters(List<HttpMessageWriter<?>> typedWriters) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<HttpMessageWriter<?>> getObjectWriters() {
        if (!this.registerDefaults) {
            return Collections.emptyList();
        }
        List<HttpMessageWriter<?>> writers = getBaseObjectWriters();
        extendObjectWriters(writers);
        return writers;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<HttpMessageWriter<?>> getBaseObjectWriters() {
        List<HttpMessageWriter<?>> writers = new ArrayList<>();
        if (jackson2Present) {
            writers.add(new EncoderHttpMessageWriter<>(getJackson2JsonEncoder()));
        }
        if (jackson2SmilePresent) {
            writers.add(new EncoderHttpMessageWriter<>(this.jackson2SmileEncoder != null ? (Jackson2SmileEncoder) this.jackson2SmileEncoder : new Jackson2SmileEncoder()));
        }
        if (jaxb2Present) {
            writers.add(new EncoderHttpMessageWriter<>(this.jaxb2Encoder != null ? (Jaxb2XmlEncoder) this.jaxb2Encoder : new Jaxb2XmlEncoder()));
        }
        return writers;
    }

    protected void extendObjectWriters(List<HttpMessageWriter<?>> objectWriters) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<HttpMessageWriter<?>> getCatchAllWriters() {
        if (!this.registerDefaults) {
            return Collections.emptyList();
        }
        List<HttpMessageWriter<?>> result = new ArrayList<>();
        result.add(new EncoderHttpMessageWriter<>(CharSequenceEncoder.allMimeTypes()));
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyDefaultConfig(BaseCodecConfigurer.DefaultCustomCodecs customCodecs) {
        applyDefaultConfig(customCodecs.getTypedReaders());
        applyDefaultConfig(customCodecs.getObjectReaders());
        applyDefaultConfig(customCodecs.getTypedWriters());
        applyDefaultConfig(customCodecs.getObjectWriters());
        customCodecs.getDefaultConfigConsumers().forEach(consumer -> {
            consumer.accept(this);
        });
    }

    private void applyDefaultConfig(Map<?, Boolean> readers) {
        readers.entrySet().stream().filter((v0) -> {
            return v0.getValue();
        }).map((v0) -> {
            return v0.getKey();
        }).forEach(this::initCodec);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Decoder<?> getJackson2JsonDecoder() {
        if (this.jackson2JsonDecoder == null) {
            this.jackson2JsonDecoder = new Jackson2JsonDecoder();
        }
        return this.jackson2JsonDecoder;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Encoder<?> getJackson2JsonEncoder() {
        if (this.jackson2JsonEncoder == null) {
            this.jackson2JsonEncoder = new Jackson2JsonEncoder();
        }
        return this.jackson2JsonEncoder;
    }
}
