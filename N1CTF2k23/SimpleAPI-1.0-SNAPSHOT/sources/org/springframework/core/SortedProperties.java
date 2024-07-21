package org.springframework.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/SortedProperties.class */
class SortedProperties extends Properties {
    static final String EOL = System.lineSeparator();
    private static final Comparator<Object> keyComparator = Comparator.comparing(String::valueOf);
    private static final Comparator<Map.Entry<Object, Object>> entryComparator = Map.Entry.comparingByKey(keyComparator);
    private final boolean omitComments;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SortedProperties(boolean omitComments) {
        this.omitComments = omitComments;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SortedProperties(Properties properties, boolean omitComments) {
        this(omitComments);
        putAll(properties);
    }

    @Override // java.util.Properties
    public void store(OutputStream out, @Nullable String comments) throws IOException {
        String[] split;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        super.store(baos, this.omitComments ? null : comments);
        String contents = baos.toString(StandardCharsets.ISO_8859_1.name());
        for (String line : contents.split(EOL)) {
            if (!this.omitComments || !line.startsWith("#")) {
                out.write((line + EOL).getBytes(StandardCharsets.ISO_8859_1));
            }
        }
    }

    @Override // java.util.Properties
    public void store(Writer writer, @Nullable String comments) throws IOException {
        String[] split;
        StringWriter stringWriter = new StringWriter();
        super.store(stringWriter, this.omitComments ? null : comments);
        String contents = stringWriter.toString();
        for (String line : contents.split(EOL)) {
            if (!this.omitComments || !line.startsWith("#")) {
                writer.write(line + EOL);
            }
        }
    }

    @Override // java.util.Properties
    public void storeToXML(OutputStream out, @Nullable String comments) throws IOException {
        super.storeToXML(out, this.omitComments ? null : comments);
    }

    @Override // java.util.Properties
    public void storeToXML(OutputStream out, @Nullable String comments, String encoding) throws IOException {
        super.storeToXML(out, this.omitComments ? null : comments, encoding);
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(keySet());
    }

    @Override // java.util.Hashtable, java.util.Map
    public Set<Object> keySet() {
        Set<Object> sortedKeys = new TreeSet<>(keyComparator);
        sortedKeys.addAll(super.keySet());
        return Collections.synchronizedSet(sortedKeys);
    }

    @Override // java.util.Hashtable, java.util.Map
    public Set<Map.Entry<Object, Object>> entrySet() {
        Set<Map.Entry<Object, Object>> sortedEntries = new TreeSet<>(entryComparator);
        sortedEntries.addAll(super.entrySet());
        return Collections.synchronizedSet(sortedEntries);
    }
}
