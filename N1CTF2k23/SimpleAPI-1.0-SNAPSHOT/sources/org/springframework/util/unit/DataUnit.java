package org.springframework.util.unit;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/unit/DataUnit.class */
public enum DataUnit {
    BYTES("B", DataSize.ofBytes(1)),
    KILOBYTES("KB", DataSize.ofKilobytes(1)),
    MEGABYTES("MB", DataSize.ofMegabytes(1)),
    GIGABYTES("GB", DataSize.ofGigabytes(1)),
    TERABYTES("TB", DataSize.ofTerabytes(1));
    
    private final String suffix;
    private final DataSize size;

    DataUnit(String suffix, DataSize size) {
        this.suffix = suffix;
        this.size = size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DataSize size() {
        return this.size;
    }

    public static DataUnit fromSuffix(String suffix) {
        DataUnit[] values;
        for (DataUnit candidate : values()) {
            if (candidate.suffix.equals(suffix)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Unknown data unit suffix '" + suffix + "'");
    }
}
