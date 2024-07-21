package org.springframework.boot.loader.jar;

import java.io.IOException;
import org.springframework.boot.loader.data.RandomAccessData;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/jar/CentralDirectoryEndRecord.class */
class CentralDirectoryEndRecord {
    private static final int MINIMUM_SIZE = 22;
    private static final int MAXIMUM_COMMENT_LENGTH = 65535;
    private static final int MAXIMUM_SIZE = 65557;
    private static final int SIGNATURE = 101010256;
    private static final int COMMENT_LENGTH_OFFSET = 20;
    private static final int READ_BLOCK_SIZE = 256;
    private final Zip64End zip64End;
    private byte[] block;
    private int offset;
    private int size = 22;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CentralDirectoryEndRecord(RandomAccessData data) throws IOException {
        this.block = createBlockFromEndOfData(data, 256);
        this.offset = this.block.length - this.size;
        while (!isValid()) {
            this.size++;
            if (this.size > this.block.length) {
                if (this.size >= MAXIMUM_SIZE || this.size > data.getSize()) {
                    throw new IOException("Unable to find ZIP central directory records after reading " + this.size + " bytes");
                }
                this.block = createBlockFromEndOfData(data, this.size + 256);
            }
            this.offset = this.block.length - this.size;
        }
        long startOfCentralDirectoryEndRecord = data.getSize() - this.size;
        Zip64Locator zip64Locator = Zip64Locator.find(data, startOfCentralDirectoryEndRecord);
        this.zip64End = zip64Locator != null ? new Zip64End(data, zip64Locator) : null;
    }

    private byte[] createBlockFromEndOfData(RandomAccessData data, int size) throws IOException {
        int length = (int) Math.min(data.getSize(), size);
        return data.read(data.getSize() - length, length);
    }

    private boolean isValid() {
        if (this.block.length < 22 || Bytes.littleEndianValue(this.block, this.offset + 0, 4) != 101010256) {
            return false;
        }
        long commentLength = Bytes.littleEndianValue(this.block, this.offset + 20, 2);
        return ((long) this.size) == 22 + commentLength;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getStartOfArchive(RandomAccessData data) {
        long littleEndianValue;
        long length = Bytes.littleEndianValue(this.block, this.offset + 12, 4);
        if (this.zip64End == null) {
            littleEndianValue = Bytes.littleEndianValue(this.block, this.offset + 16, 4);
        } else {
            littleEndianValue = this.zip64End.centralDirectoryOffset;
        }
        long specifiedOffset = littleEndianValue;
        long zip64EndSize = this.zip64End != null ? this.zip64End.getSize() : 0L;
        int zip64LocSize = this.zip64End != null ? 20 : 0;
        long actualOffset = (((data.getSize() - this.size) - length) - zip64EndSize) - zip64LocSize;
        return actualOffset - specifiedOffset;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RandomAccessData getCentralDirectory(RandomAccessData data) {
        if (this.zip64End == null) {
            long offset = Bytes.littleEndianValue(this.block, this.offset + 16, 4);
            long length = Bytes.littleEndianValue(this.block, this.offset + 12, 4);
            return data.getSubsection(offset, length);
        }
        return this.zip64End.getCentralDirectory(data);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getNumberOfRecords() {
        if (this.zip64End == null) {
            long numberOfRecords = Bytes.littleEndianValue(this.block, this.offset + 10, 2);
            return (int) numberOfRecords;
        }
        return this.zip64End.getNumberOfRecords();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getComment() {
        int commentLength = (int) Bytes.littleEndianValue(this.block, this.offset + 20, 2);
        AsciiBytes comment = new AsciiBytes(this.block, this.offset + 20 + 2, commentLength);
        return comment.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isZip64() {
        return this.zip64End != null;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/jar/CentralDirectoryEndRecord$Zip64End.class */
    private static final class Zip64End {
        private static final int ZIP64_ENDTOT = 32;
        private static final int ZIP64_ENDSIZ = 40;
        private static final int ZIP64_ENDOFF = 48;
        private final Zip64Locator locator;
        private final long centralDirectoryOffset;
        private final long centralDirectoryLength;
        private final int numberOfRecords;

        private Zip64End(RandomAccessData data, Zip64Locator locator) throws IOException {
            this.locator = locator;
            byte[] block = data.read(locator.getZip64EndOffset(), 56L);
            this.centralDirectoryOffset = Bytes.littleEndianValue(block, 48, 8);
            this.centralDirectoryLength = Bytes.littleEndianValue(block, 40, 8);
            this.numberOfRecords = (int) Bytes.littleEndianValue(block, 32, 8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long getSize() {
            return this.locator.getZip64EndSize();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public RandomAccessData getCentralDirectory(RandomAccessData data) {
            return data.getSubsection(this.centralDirectoryOffset, this.centralDirectoryLength);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getNumberOfRecords() {
            return this.numberOfRecords;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/jar/CentralDirectoryEndRecord$Zip64Locator.class */
    public static final class Zip64Locator {
        static final int SIGNATURE = 117853008;
        static final int ZIP64_LOCSIZE = 20;
        static final int ZIP64_LOCOFF = 8;
        private final long zip64EndOffset;
        private final long offset;

        private Zip64Locator(long offset, byte[] block) throws IOException {
            this.offset = offset;
            this.zip64EndOffset = Bytes.littleEndianValue(block, 8, 8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long getZip64EndSize() {
            return this.offset - this.zip64EndOffset;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long getZip64EndOffset() {
            return this.zip64EndOffset;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Zip64Locator find(RandomAccessData data, long centralDirectoryEndOffset) throws IOException {
            long offset = centralDirectoryEndOffset - 20;
            if (offset >= 0) {
                byte[] block = data.read(offset, 20L);
                if (Bytes.littleEndianValue(block, 0, 4) == 117853008) {
                    return new Zip64Locator(offset, block);
                }
                return null;
            }
            return null;
        }
    }
}
