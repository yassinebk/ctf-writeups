package com.fasterxml.jackson.datatype.jsr310;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.util.VersionUtil;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-datatype-jsr310-2.11.0.jar:com/fasterxml/jackson/datatype/jsr310/PackageVersion.class */
public final class PackageVersion implements Versioned {
    public static final Version VERSION = VersionUtil.parseVersion("2.11.0", "com.fasterxml.jackson.datatype", "jackson-datatype-jsr310");

    @Override // com.fasterxml.jackson.core.Versioned
    public Version version() {
        return VERSION;
    }
}
