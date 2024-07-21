package org.springframework.boot.json;

import java.util.List;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/json/JsonParser.class */
public interface JsonParser {
    Map<String, Object> parseMap(String json) throws JsonParseException;

    List<Object> parseList(String json) throws JsonParseException;
}
