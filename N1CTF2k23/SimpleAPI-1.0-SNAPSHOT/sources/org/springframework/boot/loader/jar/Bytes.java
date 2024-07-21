package org.springframework.boot.loader.jar;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/jar/Bytes.class */
final class Bytes {
    private Bytes() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long littleEndianValue(byte[] bytes, int offset, int length) {
        long value = 0;
        for (int i = length - 1; i >= 0; i--) {
            value = (value << 8) | (bytes[offset + i] & 255);
        }
        return value;
    }
}
