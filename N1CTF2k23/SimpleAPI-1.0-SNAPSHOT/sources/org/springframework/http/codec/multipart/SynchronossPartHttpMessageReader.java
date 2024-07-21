package org.springframework.http.codec.multipart;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.DecodingException;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.synchronoss.cloud.nio.multipart.DefaultPartBodyStreamStorageFactory;
import org.synchronoss.cloud.nio.multipart.Multipart;
import org.synchronoss.cloud.nio.multipart.MultipartContext;
import org.synchronoss.cloud.nio.multipart.MultipartUtils;
import org.synchronoss.cloud.nio.multipart.NioMultipartParser;
import org.synchronoss.cloud.nio.multipart.NioMultipartParserListener;
import org.synchronoss.cloud.nio.multipart.PartBodyStreamStorageFactory;
import org.synchronoss.cloud.nio.stream.storage.StreamStorage;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/SynchronossPartHttpMessageReader.class */
public class SynchronossPartHttpMessageReader extends LoggingCodecSupport implements HttpMessageReader<Part> {
    private static final DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
    private int maxInMemorySize = 262144;
    private long maxDiskUsagePerPart = -1;
    private int maxParts = -1;

    public void setMaxInMemorySize(int byteCount) {
        this.maxInMemorySize = byteCount;
    }

    public int getMaxInMemorySize() {
        return this.maxInMemorySize;
    }

    public void setMaxDiskUsagePerPart(long maxDiskUsagePerPart) {
        this.maxDiskUsagePerPart = maxDiskUsagePerPart;
    }

    public long getMaxDiskUsagePerPart() {
        return this.maxDiskUsagePerPart;
    }

    public void setMaxParts(int maxParts) {
        this.maxParts = maxParts;
    }

    public int getMaxParts() {
        return this.maxParts;
    }

    @Override // org.springframework.http.codec.HttpMessageReader
    public List<MediaType> getReadableMediaTypes() {
        return MultipartHttpMessageReader.MIME_TYPES;
    }

    @Override // org.springframework.http.codec.HttpMessageReader
    public boolean canRead(ResolvableType elementType, @Nullable MediaType mediaType) {
        if (Part.class.equals(elementType.toClass())) {
            if (mediaType == null) {
                return true;
            }
            for (MediaType supportedMediaType : getReadableMediaTypes()) {
                if (supportedMediaType.isCompatibleWith(mediaType)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // org.springframework.http.codec.HttpMessageReader
    public Flux<Part> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
        return Flux.create(new SynchronossPartGenerator(message)).doOnNext(part -> {
            if (!Hints.isLoggingSuppressed(hints)) {
                LogFormatUtils.traceDebug(this.logger, traceOn -> {
                    String str;
                    StringBuilder append = new StringBuilder().append(Hints.getLogPrefix(hints)).append("Parsed ");
                    if (isEnableLoggingRequestDetails()) {
                        str = LogFormatUtils.formatValue(part, !traceOn.booleanValue());
                    } else {
                        str = "parts '" + part.name() + "' (content masked)";
                    }
                    return append.append(str).toString();
                });
            }
        });
    }

    @Override // org.springframework.http.codec.HttpMessageReader
    public Mono<Part> readMono(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
        return Mono.error(new UnsupportedOperationException("Cannot read multipart request body into single Part"));
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/SynchronossPartHttpMessageReader$SynchronossPartGenerator.class */
    private class SynchronossPartGenerator extends BaseSubscriber<DataBuffer> implements Consumer<FluxSink<Part>> {
        private final ReactiveHttpInputMessage inputMessage;
        private final LimitedPartBodyStreamStorageFactory storageFactory;
        @Nullable
        private NioMultipartParserListener listener;
        @Nullable
        private NioMultipartParser parser;

        public SynchronossPartGenerator(ReactiveHttpInputMessage inputMessage) {
            this.storageFactory = new LimitedPartBodyStreamStorageFactory();
            this.inputMessage = inputMessage;
        }

        @Override // java.util.function.Consumer
        public void accept(FluxSink<Part> sink) {
            HttpHeaders headers = this.inputMessage.getHeaders();
            MediaType mediaType = headers.getContentType();
            Assert.state(mediaType != null, "No content type set");
            int length = getContentLength(headers);
            Charset charset = (Charset) Optional.ofNullable(mediaType.getCharset()).orElse(StandardCharsets.UTF_8);
            MultipartContext context = new MultipartContext(mediaType.toString(), length, charset.name());
            this.listener = new FluxSinkAdapterListener(sink, context, this.storageFactory);
            this.parser = Multipart.multipart(context).usePartBodyStreamStorageFactory(this.storageFactory).forNIO(this.listener);
            this.inputMessage.getBody().subscribe(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void hookOnNext(DataBuffer buffer) {
            Assert.state((this.parser == null || this.listener == null) ? false : true, "Not initialized yet");
            int size = buffer.readableByteCount();
            this.storageFactory.increaseByteCount(size);
            byte[] resultBytes = new byte[size];
            buffer.read(resultBytes);
            try {
                try {
                    this.parser.write(resultBytes);
                    DataBufferUtils.release(buffer);
                } catch (IOException ex) {
                    cancel();
                    int index = this.storageFactory.getCurrentPartIndex();
                    this.listener.onError("Parser error for part [" + index + "]", ex);
                    DataBufferUtils.release(buffer);
                }
            } catch (Throwable th) {
                DataBufferUtils.release(buffer);
                throw th;
            }
        }

        protected void hookOnError(Throwable ex) {
            if (this.listener != null) {
                int index = this.storageFactory.getCurrentPartIndex();
                this.listener.onError("Failure while parsing part[" + index + "]", ex);
            }
        }

        protected void hookOnComplete() {
            if (this.listener != null) {
                this.listener.onAllPartsFinished();
            }
        }

        protected void hookFinally(SignalType type) {
            try {
                if (this.parser != null) {
                    this.parser.close();
                }
            } catch (IOException e) {
            }
        }

        private int getContentLength(HttpHeaders headers) {
            long length = headers.getContentLength();
            if (((int) length) == length) {
                return (int) length;
            }
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/SynchronossPartHttpMessageReader$LimitedPartBodyStreamStorageFactory.class */
    public class LimitedPartBodyStreamStorageFactory implements PartBodyStreamStorageFactory {
        private final PartBodyStreamStorageFactory storageFactory;
        private int index;
        private boolean isFilePart;
        private long partSize;

        private LimitedPartBodyStreamStorageFactory() {
            this.storageFactory = SynchronossPartHttpMessageReader.this.maxInMemorySize > 0 ? new DefaultPartBodyStreamStorageFactory(SynchronossPartHttpMessageReader.this.maxInMemorySize) : new DefaultPartBodyStreamStorageFactory();
            this.index = 1;
        }

        public int getCurrentPartIndex() {
            return this.index;
        }

        public StreamStorage newStreamStorageForPartBody(Map<String, List<String>> headers, int index) {
            this.index = index;
            this.isFilePart = MultipartUtils.getFileName(headers) != null;
            this.partSize = 0L;
            if (SynchronossPartHttpMessageReader.this.maxParts > 0 && index > SynchronossPartHttpMessageReader.this.maxParts) {
                throw new DecodingException("Too many parts (" + index + " allowed)");
            }
            return this.storageFactory.newStreamStorageForPartBody(headers, index);
        }

        public void increaseByteCount(long byteCount) {
            this.partSize += byteCount;
            if (SynchronossPartHttpMessageReader.this.maxInMemorySize <= 0 || this.isFilePart || this.partSize < SynchronossPartHttpMessageReader.this.maxInMemorySize) {
                if (SynchronossPartHttpMessageReader.this.maxDiskUsagePerPart > 0 && this.isFilePart && this.partSize > SynchronossPartHttpMessageReader.this.maxDiskUsagePerPart) {
                    throw new DecodingException("Part[" + this.index + "] exceeded the disk usage limit of " + SynchronossPartHttpMessageReader.this.maxDiskUsagePerPart + " bytes");
                }
                return;
            }
            throw new DataBufferLimitException("Part[" + this.index + "] exceeded the in-memory limit of " + SynchronossPartHttpMessageReader.this.maxInMemorySize + " bytes");
        }

        public void partFinished() {
            this.index++;
            this.isFilePart = false;
            this.partSize = 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/SynchronossPartHttpMessageReader$FluxSinkAdapterListener.class */
    public static class FluxSinkAdapterListener implements NioMultipartParserListener {
        private final FluxSink<Part> sink;
        private final MultipartContext context;
        private final LimitedPartBodyStreamStorageFactory storageFactory;
        private final AtomicInteger terminated = new AtomicInteger(0);

        FluxSinkAdapterListener(FluxSink<Part> sink, MultipartContext context, LimitedPartBodyStreamStorageFactory factory) {
            this.sink = sink;
            this.context = context;
            this.storageFactory = factory;
        }

        public void onPartFinished(StreamStorage storage, Map<String, List<String>> headers) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.putAll(headers);
            this.storageFactory.partFinished();
            this.sink.next(createPart(storage, httpHeaders));
        }

        private Part createPart(StreamStorage storage, HttpHeaders httpHeaders) {
            String filename = MultipartUtils.getFileName(httpHeaders);
            if (filename != null) {
                return new SynchronossFilePart(httpHeaders, filename, storage);
            }
            if (MultipartUtils.isFormField(httpHeaders, this.context)) {
                String value = MultipartUtils.readFormParameterValue(storage, httpHeaders);
                return new SynchronossFormFieldPart(httpHeaders, value);
            }
            return new SynchronossPart(httpHeaders, storage);
        }

        public void onError(String message, Throwable cause) {
            if (this.terminated.getAndIncrement() == 0) {
                this.sink.error(new DecodingException(message, cause));
            }
        }

        public void onAllPartsFinished() {
            if (this.terminated.getAndIncrement() == 0) {
                this.sink.complete();
            }
        }

        public void onNestedPartStarted(Map<String, List<String>> headersFromParentPart) {
        }

        public void onNestedPartFinished() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/SynchronossPartHttpMessageReader$AbstractSynchronossPart.class */
    public static abstract class AbstractSynchronossPart implements Part {
        private final String name;
        private final HttpHeaders headers;

        AbstractSynchronossPart(HttpHeaders headers) {
            Assert.notNull(headers, "HttpHeaders is required");
            this.name = MultipartUtils.getFieldName(headers);
            this.headers = headers;
        }

        @Override // org.springframework.http.codec.multipart.Part
        public String name() {
            return this.name;
        }

        @Override // org.springframework.http.codec.multipart.Part
        public HttpHeaders headers() {
            return this.headers;
        }

        public String toString() {
            return "Part '" + this.name + "', headers=" + this.headers;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/SynchronossPartHttpMessageReader$SynchronossPart.class */
    public static class SynchronossPart extends AbstractSynchronossPart {
        private final StreamStorage storage;

        SynchronossPart(HttpHeaders headers, StreamStorage storage) {
            super(headers);
            Assert.notNull(storage, "StreamStorage is required");
            this.storage = storage;
        }

        @Override // org.springframework.http.codec.multipart.Part
        public Flux<DataBuffer> content() {
            StreamStorage storage = getStorage();
            storage.getClass();
            return DataBufferUtils.readInputStream(this::getInputStream, SynchronossPartHttpMessageReader.bufferFactory, 4096);
        }

        protected StreamStorage getStorage() {
            return this.storage;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/SynchronossPartHttpMessageReader$SynchronossFilePart.class */
    public static class SynchronossFilePart extends SynchronossPart implements FilePart {
        private static final OpenOption[] FILE_CHANNEL_OPTIONS = {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};
        private final String filename;

        SynchronossFilePart(HttpHeaders headers, String filename, StreamStorage storage) {
            super(headers, storage);
            this.filename = filename;
        }

        @Override // org.springframework.http.codec.multipart.FilePart
        public String filename() {
            return this.filename;
        }

        @Override // org.springframework.http.codec.multipart.FilePart
        public Mono<Void> transferTo(Path dest) {
            ReadableByteChannel input = null;
            FileChannel output = null;
            try {
                try {
                    input = Channels.newChannel(getStorage().getInputStream());
                    output = FileChannel.open(dest, FILE_CHANNEL_OPTIONS);
                    long size = input instanceof FileChannel ? ((FileChannel) input).size() : Long.MAX_VALUE;
                    long totalWritten = 0;
                    while (totalWritten < size) {
                        long written = output.transferFrom(input, totalWritten, size - totalWritten);
                        if (written <= 0) {
                            break;
                        }
                        totalWritten += written;
                    }
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                        }
                    }
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e2) {
                        }
                    }
                    return Mono.empty();
                } catch (IOException ex) {
                    Mono<Void> error = Mono.error(ex);
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e3) {
                        }
                    }
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e4) {
                        }
                    }
                    return error;
                }
            } catch (Throwable th) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e5) {
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e6) {
                    }
                }
                throw th;
            }
        }

        @Override // org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader.AbstractSynchronossPart
        public String toString() {
            return "Part '" + name() + "', filename='" + this.filename + "'";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/SynchronossPartHttpMessageReader$SynchronossFormFieldPart.class */
    public static class SynchronossFormFieldPart extends AbstractSynchronossPart implements FormFieldPart {
        private final String content;

        SynchronossFormFieldPart(HttpHeaders headers, String content) {
            super(headers);
            this.content = content;
        }

        @Override // org.springframework.http.codec.multipart.FormFieldPart
        public String value() {
            return this.content;
        }

        @Override // org.springframework.http.codec.multipart.Part
        public Flux<DataBuffer> content() {
            byte[] bytes = this.content.getBytes(getCharset());
            return Flux.just(SynchronossPartHttpMessageReader.bufferFactory.wrap(bytes));
        }

        private Charset getCharset() {
            String name = MultipartUtils.getCharEncoding(headers());
            return name != null ? Charset.forName(name) : StandardCharsets.UTF_8;
        }

        @Override // org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader.AbstractSynchronossPart
        public String toString() {
            return "Part '" + name() + "=" + this.content + "'";
        }
    }
}
