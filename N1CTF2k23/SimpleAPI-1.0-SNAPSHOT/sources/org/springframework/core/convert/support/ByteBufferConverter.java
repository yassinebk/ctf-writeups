package org.springframework.core.convert.support;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/ByteBufferConverter.class */
public final class ByteBufferConverter implements ConditionalGenericConverter {
    private static final TypeDescriptor BYTE_BUFFER_TYPE = TypeDescriptor.valueOf(ByteBuffer.class);
    private static final TypeDescriptor BYTE_ARRAY_TYPE = TypeDescriptor.valueOf(byte[].class);
    private static final Set<GenericConverter.ConvertiblePair> CONVERTIBLE_PAIRS;
    private final ConversionService conversionService;

    static {
        Set<GenericConverter.ConvertiblePair> convertiblePairs = new HashSet<>(4);
        convertiblePairs.add(new GenericConverter.ConvertiblePair(ByteBuffer.class, byte[].class));
        convertiblePairs.add(new GenericConverter.ConvertiblePair(byte[].class, ByteBuffer.class));
        convertiblePairs.add(new GenericConverter.ConvertiblePair(ByteBuffer.class, Object.class));
        convertiblePairs.add(new GenericConverter.ConvertiblePair(Object.class, ByteBuffer.class));
        CONVERTIBLE_PAIRS = Collections.unmodifiableSet(convertiblePairs);
    }

    public ByteBufferConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override // org.springframework.core.convert.converter.GenericConverter
    public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
        return CONVERTIBLE_PAIRS;
    }

    @Override // org.springframework.core.convert.converter.ConditionalConverter
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        boolean byteBufferTarget = targetType.isAssignableTo(BYTE_BUFFER_TYPE);
        return sourceType.isAssignableTo(BYTE_BUFFER_TYPE) ? byteBufferTarget || matchesFromByteBuffer(targetType) : byteBufferTarget && matchesToByteBuffer(sourceType);
    }

    private boolean matchesFromByteBuffer(TypeDescriptor targetType) {
        return targetType.isAssignableTo(BYTE_ARRAY_TYPE) || this.conversionService.canConvert(BYTE_ARRAY_TYPE, targetType);
    }

    private boolean matchesToByteBuffer(TypeDescriptor sourceType) {
        return sourceType.isAssignableTo(BYTE_ARRAY_TYPE) || this.conversionService.canConvert(sourceType, BYTE_ARRAY_TYPE);
    }

    @Override // org.springframework.core.convert.converter.GenericConverter
    @Nullable
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        boolean byteBufferTarget = targetType.isAssignableTo(BYTE_BUFFER_TYPE);
        if (source instanceof ByteBuffer) {
            ByteBuffer buffer = (ByteBuffer) source;
            return byteBufferTarget ? buffer.duplicate() : convertFromByteBuffer(buffer, targetType);
        } else if (byteBufferTarget) {
            return convertToByteBuffer(source, sourceType);
        } else {
            throw new IllegalStateException("Unexpected source/target types");
        }
    }

    @Nullable
    private Object convertFromByteBuffer(ByteBuffer source, TypeDescriptor targetType) {
        byte[] bytes = new byte[source.remaining()];
        source.get(bytes);
        if (targetType.isAssignableTo(BYTE_ARRAY_TYPE)) {
            return bytes;
        }
        return this.conversionService.convert(bytes, BYTE_ARRAY_TYPE, targetType);
    }

    private Object convertToByteBuffer(@Nullable Object source, TypeDescriptor sourceType) {
        byte[] bytes = (byte[]) (source instanceof byte[] ? source : this.conversionService.convert(source, sourceType, BYTE_ARRAY_TYPE));
        if (bytes == null) {
            return ByteBuffer.wrap(new byte[0]);
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        return byteBuffer.rewind();
    }
}
