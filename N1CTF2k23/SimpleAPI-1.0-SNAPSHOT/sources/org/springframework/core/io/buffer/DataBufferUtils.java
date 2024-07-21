package org.springframework.core.io.buffer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/buffer/DataBufferUtils.class */
public abstract class DataBufferUtils {
    private static final Consumer<DataBuffer> RELEASE_CONSUMER = DataBufferUtils::release;

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/buffer/DataBufferUtils$Matcher.class */
    public interface Matcher {
        int match(DataBuffer dataBuffer);

        byte[] delimiter();

        void reset();
    }

    public static Flux<DataBuffer> readInputStream(Callable<InputStream> inputStreamSupplier, DataBufferFactory bufferFactory, int bufferSize) {
        Assert.notNull(inputStreamSupplier, "'inputStreamSupplier' must not be null");
        return readByteChannel(() -> {
            return Channels.newChannel((InputStream) inputStreamSupplier.call());
        }, bufferFactory, bufferSize);
    }

    public static Flux<DataBuffer> readByteChannel(Callable<ReadableByteChannel> channelSupplier, DataBufferFactory bufferFactory, int bufferSize) {
        Assert.notNull(channelSupplier, "'channelSupplier' must not be null");
        Assert.notNull(bufferFactory, "'dataBufferFactory' must not be null");
        Assert.isTrue(bufferSize > 0, "'bufferSize' must be > 0");
        return Flux.using(channelSupplier, channel -> {
            return Flux.generate(new ReadableByteChannelGenerator(channel, bufferFactory, bufferSize));
        }, (v0) -> {
            closeChannel(v0);
        });
    }

    public static Flux<DataBuffer> readAsynchronousFileChannel(Callable<AsynchronousFileChannel> channelSupplier, DataBufferFactory bufferFactory, int bufferSize) {
        return readAsynchronousFileChannel(channelSupplier, 0L, bufferFactory, bufferSize);
    }

    public static Flux<DataBuffer> readAsynchronousFileChannel(Callable<AsynchronousFileChannel> channelSupplier, long position, DataBufferFactory bufferFactory, int bufferSize) {
        Assert.notNull(channelSupplier, "'channelSupplier' must not be null");
        Assert.notNull(bufferFactory, "'dataBufferFactory' must not be null");
        Assert.isTrue(position >= 0, "'position' must be >= 0");
        Assert.isTrue(bufferSize > 0, "'bufferSize' must be > 0");
        Flux<DataBuffer> flux = Flux.using(channelSupplier, channel -> {
            return Flux.create(sink -> {
                ReadCompletionHandler handler = new ReadCompletionHandler(channel, sink, position, bufferFactory, bufferSize);
                handler.getClass();
                sink.onCancel(this::cancel);
                handler.getClass();
                sink.onRequest(this::request);
            });
        }, channel2 -> {
        });
        return flux.doOnDiscard(PooledDataBuffer.class, (v0) -> {
            release(v0);
        });
    }

    public static Flux<DataBuffer> read(Path path, DataBufferFactory bufferFactory, int bufferSize, OpenOption... options) {
        Assert.notNull(path, "Path must not be null");
        Assert.notNull(bufferFactory, "BufferFactory must not be null");
        Assert.isTrue(bufferSize > 0, "'bufferSize' must be > 0");
        if (options.length > 0) {
            int length = options.length;
            for (int i = 0; i < length; i++) {
                OpenOption option = options[i];
                Assert.isTrue((option == StandardOpenOption.APPEND || option == StandardOpenOption.WRITE) ? false : true, "'" + option + "' not allowed");
            }
        }
        return readAsynchronousFileChannel(() -> {
            return AsynchronousFileChannel.open(path, options);
        }, bufferFactory, bufferSize);
    }

    public static Flux<DataBuffer> read(Resource resource, DataBufferFactory bufferFactory, int bufferSize) {
        return read(resource, 0L, bufferFactory, bufferSize);
    }

    public static Flux<DataBuffer> read(Resource resource, long position, DataBufferFactory bufferFactory, int bufferSize) {
        try {
            if (resource.isFile()) {
                File file = resource.getFile();
                return readAsynchronousFileChannel(() -> {
                    return AsynchronousFileChannel.open(file.toPath(), StandardOpenOption.READ);
                }, position, bufferFactory, bufferSize);
            }
        } catch (IOException e) {
        }
        resource.getClass();
        Flux<DataBuffer> result = readByteChannel(this::readableChannel, bufferFactory, bufferSize);
        return position == 0 ? result : skipUntilByteCount(result, position);
    }

    public static Flux<DataBuffer> write(Publisher<DataBuffer> source, OutputStream outputStream) {
        Assert.notNull(source, "'source' must not be null");
        Assert.notNull(outputStream, "'outputStream' must not be null");
        WritableByteChannel channel = Channels.newChannel(outputStream);
        return write(source, channel);
    }

    public static Flux<DataBuffer> write(Publisher<DataBuffer> source, WritableByteChannel channel) {
        Assert.notNull(source, "'source' must not be null");
        Assert.notNull(channel, "'channel' must not be null");
        Flux<DataBuffer> flux = Flux.from(source);
        return Flux.create(sink -> {
            WritableByteChannelSubscriber subscriber = new WritableByteChannelSubscriber(sink, channel);
            sink.onDispose(subscriber);
            flux.subscribe(subscriber);
        });
    }

    public static Flux<DataBuffer> write(Publisher<DataBuffer> source, AsynchronousFileChannel channel) {
        return write(source, channel, 0L);
    }

    public static Flux<DataBuffer> write(Publisher<? extends DataBuffer> source, AsynchronousFileChannel channel, long position) {
        Assert.notNull(source, "'source' must not be null");
        Assert.notNull(channel, "'channel' must not be null");
        Assert.isTrue(position >= 0, "'position' must be >= 0");
        Flux<DataBuffer> flux = Flux.from(source);
        return Flux.create(sink -> {
            WriteCompletionHandler handler = new WriteCompletionHandler(sink, channel, position);
            sink.onDispose(handler);
            flux.subscribe(handler);
        });
    }

    public static Mono<Void> write(Publisher<DataBuffer> source, Path destination, OpenOption... options) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(destination, "Destination must not be null");
        Set<OpenOption> optionSet = checkWriteOptions(options);
        return Mono.create(sink -> {
            try {
                AsynchronousFileChannel channel = AsynchronousFileChannel.open(destination, optionSet, null, new FileAttribute[0]);
                sink.onDispose(() -> {
                    closeChannel(channel);
                });
                Flux<DataBuffer> write = write(source, channel);
                Consumer consumer = DataBufferUtils::release;
                sink.getClass();
                Consumer consumer2 = this::error;
                sink.getClass();
                write.subscribe(consumer, consumer2, this::success);
            } catch (IOException ex) {
                sink.error(ex);
            }
        });
    }

    private static Set<OpenOption> checkWriteOptions(OpenOption[] options) {
        int length = options.length;
        Set<OpenOption> result = new HashSet<>(length + 3);
        if (length == 0) {
            result.add(StandardOpenOption.CREATE);
            result.add(StandardOpenOption.TRUNCATE_EXISTING);
        } else {
            for (OpenOption opt : options) {
                if (opt == StandardOpenOption.READ) {
                    throw new IllegalArgumentException("READ not allowed");
                }
                result.add(opt);
            }
        }
        result.add(StandardOpenOption.WRITE);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void closeChannel(@Nullable Channel channel) {
        if (channel != null && channel.isOpen()) {
            try {
                channel.close();
            } catch (IOException e) {
            }
        }
    }

    public static Flux<DataBuffer> takeUntilByteCount(Publisher<? extends DataBuffer> publisher, long maxByteCount) {
        Assert.notNull(publisher, "Publisher must not be null");
        Assert.isTrue(maxByteCount >= 0, "'maxByteCount' must be a positive number");
        return Flux.defer(() -> {
            AtomicLong countDown = new AtomicLong(maxByteCount);
            return Flux.from(publisher).map(buffer -> {
                long remainder = countDown.addAndGet(-buffer.readableByteCount());
                if (remainder < 0) {
                    int length = buffer.readableByteCount() + ((int) remainder);
                    return buffer.slice(0, length);
                }
                return buffer;
            }).takeUntil(buffer2 -> {
                return countDown.get() <= 0;
            });
        });
    }

    public static Flux<DataBuffer> skipUntilByteCount(Publisher<? extends DataBuffer> publisher, long maxByteCount) {
        Assert.notNull(publisher, "Publisher must not be null");
        Assert.isTrue(maxByteCount >= 0, "'maxByteCount' must be a positive number");
        return Flux.defer(() -> {
            AtomicLong countDown = new AtomicLong(maxByteCount);
            return Flux.from(publisher).skipUntil(buffer -> {
                long remainder = countDown.addAndGet(-buffer.readableByteCount());
                return remainder < 0;
            }).map(buffer2 -> {
                long remainder = countDown.get();
                if (remainder < 0) {
                    countDown.set(0L);
                    int start = buffer2.readableByteCount() + ((int) remainder);
                    int length = (int) (-remainder);
                    return buffer2.slice(start, length);
                }
                return buffer2;
            });
        }).doOnDiscard(PooledDataBuffer.class, (v0) -> {
            release(v0);
        });
    }

    public static <T extends DataBuffer> T retain(T dataBuffer) {
        if (dataBuffer instanceof PooledDataBuffer) {
            return ((PooledDataBuffer) dataBuffer).retain();
        }
        return dataBuffer;
    }

    public static boolean release(@Nullable DataBuffer dataBuffer) {
        if (dataBuffer instanceof PooledDataBuffer) {
            PooledDataBuffer pooledDataBuffer = (PooledDataBuffer) dataBuffer;
            if (pooledDataBuffer.isAllocated()) {
                return pooledDataBuffer.release();
            }
            return false;
        }
        return false;
    }

    public static Consumer<DataBuffer> releaseConsumer() {
        return RELEASE_CONSUMER;
    }

    public static Mono<DataBuffer> join(Publisher<? extends DataBuffer> dataBuffers) {
        return join(dataBuffers, -1);
    }

    public static Mono<DataBuffer> join(Publisher<? extends DataBuffer> buffers, int maxByteCount) {
        Assert.notNull(buffers, "'dataBuffers' must not be null");
        if (buffers instanceof Mono) {
            return (Mono) buffers;
        }
        return Flux.from(buffers).collect(() -> {
            return new LimitedDataBufferList(maxByteCount);
        }, (v0, v1) -> {
            v0.add(v1);
        }).filter(list -> {
            return !list.isEmpty();
        }).map(list2 -> {
            return list2.get(0).factory().join(list2);
        }).doOnDiscard(PooledDataBuffer.class, (v0) -> {
            release(v0);
        });
    }

    public static Matcher matcher(byte[] delimiter) {
        Assert.isTrue(delimiter.length > 0, "Delimiter must not be empty");
        return new KnuthMorrisPrattMatcher(delimiter);
    }

    public static Matcher matcher(byte[]... delimiters) {
        Assert.isTrue(delimiters.length > 0, "Delimiters must not be empty");
        if (delimiters.length == 1) {
            return matcher(delimiters[0]);
        }
        Matcher[] matchers = new Matcher[delimiters.length];
        for (int i = 0; i < delimiters.length; i++) {
            matchers[i] = matcher(delimiters[i]);
        }
        return new CompositeMatcher(matchers);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/buffer/DataBufferUtils$ReadableByteChannelGenerator.class */
    public static class ReadableByteChannelGenerator implements Consumer<SynchronousSink<DataBuffer>> {
        private final ReadableByteChannel channel;
        private final DataBufferFactory dataBufferFactory;
        private final int bufferSize;

        public ReadableByteChannelGenerator(ReadableByteChannel channel, DataBufferFactory dataBufferFactory, int bufferSize) {
            this.channel = channel;
            this.dataBufferFactory = dataBufferFactory;
            this.bufferSize = bufferSize;
        }

        @Override // java.util.function.Consumer
        public void accept(SynchronousSink<DataBuffer> sink) {
            boolean release = true;
            DataBuffer dataBuffer = this.dataBufferFactory.allocateBuffer(this.bufferSize);
            try {
                try {
                    ByteBuffer byteBuffer = dataBuffer.asByteBuffer(0, dataBuffer.capacity());
                    int read = this.channel.read(byteBuffer);
                    if (read >= 0) {
                        dataBuffer.writePosition(read);
                        release = false;
                        sink.next(dataBuffer);
                    } else {
                        sink.complete();
                    }
                    if (release) {
                        DataBufferUtils.release(dataBuffer);
                    }
                } catch (IOException ex) {
                    sink.error(ex);
                    if (1 != 0) {
                        DataBufferUtils.release(dataBuffer);
                    }
                }
            } catch (Throwable th) {
                if (1 != 0) {
                    DataBufferUtils.release(dataBuffer);
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/buffer/DataBufferUtils$ReadCompletionHandler.class */
    public static class ReadCompletionHandler implements CompletionHandler<Integer, DataBuffer> {
        private final AsynchronousFileChannel channel;
        private final FluxSink<DataBuffer> sink;
        private final DataBufferFactory dataBufferFactory;
        private final int bufferSize;
        private final AtomicLong position;
        private final AtomicBoolean reading = new AtomicBoolean();
        private final AtomicBoolean disposed = new AtomicBoolean();

        public ReadCompletionHandler(AsynchronousFileChannel channel, FluxSink<DataBuffer> sink, long position, DataBufferFactory dataBufferFactory, int bufferSize) {
            this.channel = channel;
            this.sink = sink;
            this.position = new AtomicLong(position);
            this.dataBufferFactory = dataBufferFactory;
            this.bufferSize = bufferSize;
        }

        public void read() {
            if (this.sink.requestedFromDownstream() > 0 && isNotDisposed() && this.reading.compareAndSet(false, true)) {
                DataBuffer dataBuffer = this.dataBufferFactory.allocateBuffer(this.bufferSize);
                ByteBuffer byteBuffer = dataBuffer.asByteBuffer(0, this.bufferSize);
                this.channel.read(byteBuffer, this.position.get(), dataBuffer, this);
            }
        }

        @Override // java.nio.channels.CompletionHandler
        public void completed(Integer read, DataBuffer dataBuffer) {
            if (isNotDisposed()) {
                if (read.intValue() != -1) {
                    this.position.addAndGet(read.intValue());
                    dataBuffer.writePosition(read.intValue());
                    this.sink.next(dataBuffer);
                    this.reading.set(false);
                    read();
                    return;
                }
                DataBufferUtils.release(dataBuffer);
                DataBufferUtils.closeChannel(this.channel);
                if (this.disposed.compareAndSet(false, true)) {
                    this.sink.complete();
                }
                this.reading.set(false);
                return;
            }
            DataBufferUtils.release(dataBuffer);
            DataBufferUtils.closeChannel(this.channel);
            this.reading.set(false);
        }

        @Override // java.nio.channels.CompletionHandler
        public void failed(Throwable exc, DataBuffer dataBuffer) {
            DataBufferUtils.release(dataBuffer);
            DataBufferUtils.closeChannel(this.channel);
            if (this.disposed.compareAndSet(false, true)) {
                this.sink.error(exc);
            }
            this.reading.set(false);
        }

        public void request(long n) {
            read();
        }

        public void cancel() {
            if (this.disposed.compareAndSet(false, true) && !this.reading.get()) {
                DataBufferUtils.closeChannel(this.channel);
            }
        }

        private boolean isNotDisposed() {
            return !this.disposed.get();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/buffer/DataBufferUtils$WritableByteChannelSubscriber.class */
    public static class WritableByteChannelSubscriber extends BaseSubscriber<DataBuffer> {
        private final FluxSink<DataBuffer> sink;
        private final WritableByteChannel channel;

        public WritableByteChannelSubscriber(FluxSink<DataBuffer> sink, WritableByteChannel channel) {
            this.sink = sink;
            this.channel = channel;
        }

        protected void hookOnSubscribe(Subscription subscription) {
            request(1L);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void hookOnNext(DataBuffer dataBuffer) {
            try {
                ByteBuffer byteBuffer = dataBuffer.asByteBuffer();
                while (byteBuffer.hasRemaining()) {
                    this.channel.write(byteBuffer);
                }
                this.sink.next(dataBuffer);
                request(1L);
            } catch (IOException ex) {
                this.sink.next(dataBuffer);
                this.sink.error(ex);
            }
        }

        protected void hookOnError(Throwable throwable) {
            this.sink.error(throwable);
        }

        protected void hookOnComplete() {
            this.sink.complete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/buffer/DataBufferUtils$WriteCompletionHandler.class */
    public static class WriteCompletionHandler extends BaseSubscriber<DataBuffer> implements CompletionHandler<Integer, ByteBuffer> {
        private final FluxSink<DataBuffer> sink;
        private final AsynchronousFileChannel channel;
        private final AtomicLong position;
        private final AtomicBoolean completed = new AtomicBoolean();
        private final AtomicReference<Throwable> error = new AtomicReference<>();
        private final AtomicReference<DataBuffer> dataBuffer = new AtomicReference<>();

        public WriteCompletionHandler(FluxSink<DataBuffer> sink, AsynchronousFileChannel channel, long position) {
            this.sink = sink;
            this.channel = channel;
            this.position = new AtomicLong(position);
        }

        protected void hookOnSubscribe(Subscription subscription) {
            request(1L);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void hookOnNext(DataBuffer value) {
            if (!this.dataBuffer.compareAndSet(null, value)) {
                throw new IllegalStateException();
            }
            ByteBuffer byteBuffer = value.asByteBuffer();
            this.channel.write(byteBuffer, this.position.get(), byteBuffer, this);
        }

        protected void hookOnError(Throwable throwable) {
            this.error.set(throwable);
            if (this.dataBuffer.get() == null) {
                this.sink.error(throwable);
            }
        }

        protected void hookOnComplete() {
            this.completed.set(true);
            if (this.dataBuffer.get() == null) {
                this.sink.complete();
            }
        }

        @Override // java.nio.channels.CompletionHandler
        public void completed(Integer written, ByteBuffer byteBuffer) {
            long pos = this.position.addAndGet(written.intValue());
            if (byteBuffer.hasRemaining()) {
                this.channel.write(byteBuffer, pos, byteBuffer, this);
                return;
            }
            sinkDataBuffer();
            Throwable throwable = this.error.get();
            if (throwable != null) {
                this.sink.error(throwable);
            } else if (this.completed.get()) {
                this.sink.complete();
            } else {
                request(1L);
            }
        }

        @Override // java.nio.channels.CompletionHandler
        public void failed(Throwable exc, ByteBuffer byteBuffer) {
            sinkDataBuffer();
            this.sink.error(exc);
        }

        private void sinkDataBuffer() {
            DataBuffer dataBuffer = this.dataBuffer.get();
            Assert.state(dataBuffer != null, "DataBuffer should not be null");
            this.sink.next(dataBuffer);
            this.dataBuffer.set(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/buffer/DataBufferUtils$KnuthMorrisPrattMatcher.class */
    public static class KnuthMorrisPrattMatcher implements Matcher {
        private final byte[] delimiter;
        private final int[] table;
        private int matches = 0;

        public KnuthMorrisPrattMatcher(byte[] delimiter) {
            this.delimiter = Arrays.copyOf(delimiter, delimiter.length);
            this.table = longestSuffixPrefixTable(delimiter);
        }

        private static int[] longestSuffixPrefixTable(byte[] delimiter) {
            int j;
            int[] result = new int[delimiter.length];
            result[0] = 0;
            for (int i = 1; i < delimiter.length; i++) {
                int i2 = result[i - 1];
                while (true) {
                    j = i2;
                    if (j <= 0 || delimiter[i] == delimiter[j]) {
                        break;
                    }
                    i2 = result[j - 1];
                }
                if (delimiter[i] == delimiter[j]) {
                    j++;
                }
                result[i] = j;
            }
            return result;
        }

        @Override // org.springframework.core.io.buffer.DataBufferUtils.Matcher
        public int match(DataBuffer dataBuffer) {
            for (int i = dataBuffer.readPosition(); i < dataBuffer.writePosition(); i++) {
                byte b = dataBuffer.getByte(i);
                while (this.matches > 0 && b != this.delimiter[this.matches]) {
                    this.matches = this.table[this.matches - 1];
                }
                if (b == this.delimiter[this.matches]) {
                    this.matches++;
                    if (this.matches == this.delimiter.length) {
                        reset();
                        return i;
                    }
                }
            }
            return -1;
        }

        @Override // org.springframework.core.io.buffer.DataBufferUtils.Matcher
        public byte[] delimiter() {
            return Arrays.copyOf(this.delimiter, this.delimiter.length);
        }

        @Override // org.springframework.core.io.buffer.DataBufferUtils.Matcher
        public void reset() {
            this.matches = 0;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/buffer/DataBufferUtils$CompositeMatcher.class */
    private static class CompositeMatcher implements Matcher {
        private static final byte[] NO_DELIMITER = new byte[0];
        private final Matcher[] matchers;
        byte[] longestDelimiter = NO_DELIMITER;

        public CompositeMatcher(Matcher[] matchers) {
            this.matchers = matchers;
        }

        @Override // org.springframework.core.io.buffer.DataBufferUtils.Matcher
        public int match(DataBuffer dataBuffer) {
            Matcher[] matcherArr;
            this.longestDelimiter = NO_DELIMITER;
            int bestEndIdx = Integer.MAX_VALUE;
            for (Matcher matcher : this.matchers) {
                int endIdx = matcher.match(dataBuffer);
                if (endIdx != -1 && endIdx <= bestEndIdx && matcher.delimiter().length > this.longestDelimiter.length) {
                    bestEndIdx = endIdx;
                    this.longestDelimiter = matcher.delimiter();
                }
            }
            if (bestEndIdx == Integer.MAX_VALUE) {
                this.longestDelimiter = NO_DELIMITER;
                return -1;
            }
            reset();
            return bestEndIdx;
        }

        @Override // org.springframework.core.io.buffer.DataBufferUtils.Matcher
        public byte[] delimiter() {
            Assert.state(this.longestDelimiter != NO_DELIMITER, "Illegal state!");
            return this.longestDelimiter;
        }

        @Override // org.springframework.core.io.buffer.DataBufferUtils.Matcher
        public void reset() {
            Matcher[] matcherArr;
            for (Matcher matcher : this.matchers) {
                matcher.reset();
            }
        }
    }
}
