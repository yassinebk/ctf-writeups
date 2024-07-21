package org.springframework.boot.env;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/env/PropertiesPropertySourceLoader.class */
public class PropertiesPropertySourceLoader implements PropertySourceLoader {
    private static final String XML_FILE_EXTENSION = ".xml";

    @Override // org.springframework.boot.env.PropertySourceLoader
    public String[] getFileExtensions() {
        return new String[]{"properties", "xml"};
    }

    @Override // org.springframework.boot.env.PropertySourceLoader
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
        Map<String, ?> properties = loadProperties(resource);
        if (properties.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new OriginTrackedMapPropertySource(name, Collections.unmodifiableMap(properties), true));
    }

    private Map<String, ?> loadProperties(Resource resource) throws IOException {
        String filename = resource.getFilename();
        if (filename != null && filename.endsWith(".xml")) {
            return PropertiesLoaderUtils.loadProperties(resource);
        }
        return new OriginTrackedPropertiesLoader(resource).load();
    }
}
