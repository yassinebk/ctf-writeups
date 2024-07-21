package org.springframework.boot.json;

import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/json/YamlJsonParser.class */
public class YamlJsonParser extends AbstractJsonParser {
    @Override // org.springframework.boot.json.JsonParser
    public Map<String, Object> parseMap(String json) {
        return parseMap(json, trimmed -> {
            return (Map) new Yaml().loadAs(trimmed, Map.class);
        });
    }

    @Override // org.springframework.boot.json.JsonParser
    public List<Object> parseList(String json) {
        return parseList(json, trimmed -> {
            return (List) new Yaml().loadAs(trimmed, List.class);
        });
    }
}
