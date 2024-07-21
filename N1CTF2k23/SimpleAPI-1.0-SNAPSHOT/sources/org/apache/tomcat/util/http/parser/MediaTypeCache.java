package org.apache.tomcat.util.http.parser;

import java.io.IOException;
import java.io.StringReader;
import org.apache.tomcat.util.collections.ConcurrentCache;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/parser/MediaTypeCache.class */
public class MediaTypeCache {
    private final ConcurrentCache<String, String[]> cache;

    public MediaTypeCache(int size) {
        this.cache = new ConcurrentCache<>(size);
    }

    public String[] parse(String input) {
        String[] result = this.cache.get(input);
        if (result != null) {
            return result;
        }
        MediaType m = null;
        try {
            m = MediaType.parseMediaType(new StringReader(input));
        } catch (IOException e) {
        }
        if (m != null) {
            result = new String[]{m.toStringNoCharset(), m.getCharset()};
            this.cache.put(input, result);
        }
        return result;
    }
}
