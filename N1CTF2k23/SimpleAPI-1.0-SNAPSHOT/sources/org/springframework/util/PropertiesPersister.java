package org.springframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/PropertiesPersister.class */
public interface PropertiesPersister {
    void load(Properties properties, InputStream inputStream) throws IOException;

    void load(Properties properties, Reader reader) throws IOException;

    void store(Properties properties, OutputStream outputStream, String str) throws IOException;

    void store(Properties properties, Writer writer, String str) throws IOException;

    void loadFromXml(Properties properties, InputStream inputStream) throws IOException;

    void storeToXml(Properties properties, OutputStream outputStream, String str) throws IOException;

    void storeToXml(Properties properties, OutputStream outputStream, String str, String str2) throws IOException;
}
