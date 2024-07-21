package org.springframework.core.convert.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.core.convert.converter.Converter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/PropertiesToStringConverter.class */
final class PropertiesToStringConverter implements Converter<Properties, String> {
    @Override // org.springframework.core.convert.converter.Converter
    public String convert(Properties source) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream(256);
            source.store(os, (String) null);
            return os.toString("ISO-8859-1");
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to store [" + source + "] into String", ex);
        }
    }
}
