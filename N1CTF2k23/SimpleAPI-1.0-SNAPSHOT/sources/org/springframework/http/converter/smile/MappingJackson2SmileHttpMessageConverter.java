package org.springframework.http.converter.smile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/converter/smile/MappingJackson2SmileHttpMessageConverter.class */
public class MappingJackson2SmileHttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    public MappingJackson2SmileHttpMessageConverter() {
        this(Jackson2ObjectMapperBuilder.smile().build());
    }

    public MappingJackson2SmileHttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, new MediaType("application", "x-jackson-smile"));
        Assert.isInstanceOf(SmileFactory.class, objectMapper.getFactory(), "SmileFactory required");
    }

    @Override // org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.isInstanceOf(SmileFactory.class, objectMapper.getFactory(), "SmileFactory required");
        super.setObjectMapper(objectMapper);
    }
}
