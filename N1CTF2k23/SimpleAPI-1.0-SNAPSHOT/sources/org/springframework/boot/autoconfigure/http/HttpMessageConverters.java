package org.springframework.boot.autoconfigure.http;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/http/HttpMessageConverters.class */
public class HttpMessageConverters implements Iterable<HttpMessageConverter<?>> {
    private static final List<Class<?>> NON_REPLACING_CONVERTERS;
    private final List<HttpMessageConverter<?>> converters;

    static {
        List<Class<?>> nonReplacingConverters = new ArrayList<>();
        addClassIfExists(nonReplacingConverters, "org.springframework.hateoas.server.mvc.TypeConstrainedMappingJackson2HttpMessageConverter");
        NON_REPLACING_CONVERTERS = Collections.unmodifiableList(nonReplacingConverters);
    }

    public HttpMessageConverters(HttpMessageConverter<?>... additionalConverters) {
        this(Arrays.asList(additionalConverters));
    }

    public HttpMessageConverters(Collection<HttpMessageConverter<?>> additionalConverters) {
        this(true, additionalConverters);
    }

    public HttpMessageConverters(boolean addDefaultConverters, Collection<HttpMessageConverter<?>> converters) {
        List<HttpMessageConverter<?>> combined = getCombinedConverters(converters, addDefaultConverters ? getDefaultConverters() : Collections.emptyList());
        this.converters = Collections.unmodifiableList(postProcessConverters(combined));
    }

    private List<HttpMessageConverter<?>> getCombinedConverters(Collection<HttpMessageConverter<?>> converters, List<HttpMessageConverter<?>> defaultConverters) {
        List<HttpMessageConverter<?>> combined = new ArrayList<>();
        List<HttpMessageConverter<?>> processing = new ArrayList<>(converters);
        for (HttpMessageConverter<?> defaultConverter : defaultConverters) {
            Iterator<HttpMessageConverter<?>> iterator = processing.iterator();
            while (iterator.hasNext()) {
                HttpMessageConverter<?> candidate = iterator.next();
                if (isReplacement(defaultConverter, candidate)) {
                    combined.add(candidate);
                    iterator.remove();
                }
            }
            combined.add(defaultConverter);
            if (defaultConverter instanceof AllEncompassingFormHttpMessageConverter) {
                configurePartConverters((AllEncompassingFormHttpMessageConverter) defaultConverter, converters);
            }
        }
        combined.addAll(0, processing);
        return combined;
    }

    private boolean isReplacement(HttpMessageConverter<?> defaultConverter, HttpMessageConverter<?> candidate) {
        for (Class<?> nonReplacingConverter : NON_REPLACING_CONVERTERS) {
            if (nonReplacingConverter.isInstance(candidate)) {
                return false;
            }
        }
        return ClassUtils.isAssignableValue(defaultConverter.getClass(), candidate);
    }

    private void configurePartConverters(AllEncompassingFormHttpMessageConverter formConverter, Collection<HttpMessageConverter<?>> converters) {
        List<HttpMessageConverter<?>> partConverters = extractPartConverters(formConverter);
        List<HttpMessageConverter<?>> combinedConverters = getCombinedConverters(converters, partConverters);
        formConverter.setPartConverters(postProcessPartConverters(combinedConverters));
    }

    private List<HttpMessageConverter<?>> extractPartConverters(FormHttpMessageConverter formConverter) {
        Field field = ReflectionUtils.findField(FormHttpMessageConverter.class, "partConverters");
        ReflectionUtils.makeAccessible(field);
        return (List) ReflectionUtils.getField(field, formConverter);
    }

    protected List<HttpMessageConverter<?>> postProcessConverters(List<HttpMessageConverter<?>> converters) {
        return converters;
    }

    protected List<HttpMessageConverter<?>> postProcessPartConverters(List<HttpMessageConverter<?>> converters) {
        return converters;
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [org.springframework.boot.autoconfigure.http.HttpMessageConverters$1] */
    private List<HttpMessageConverter<?>> getDefaultConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        if (ClassUtils.isPresent("org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport", null)) {
            converters.addAll(new WebMvcConfigurationSupport() { // from class: org.springframework.boot.autoconfigure.http.HttpMessageConverters.1
                public List<HttpMessageConverter<?>> defaultMessageConverters() {
                    return super.getMessageConverters();
                }
            }.defaultMessageConverters());
        } else {
            converters.addAll(new RestTemplate().getMessageConverters());
        }
        reorderXmlConvertersToEnd(converters);
        return converters;
    }

    private void reorderXmlConvertersToEnd(List<HttpMessageConverter<?>> converters) {
        List<HttpMessageConverter<?>> xml = new ArrayList<>();
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if ((converter instanceof AbstractXmlHttpMessageConverter) || (converter instanceof MappingJackson2XmlHttpMessageConverter)) {
                xml.add(converter);
                iterator.remove();
            }
        }
        converters.addAll(xml);
    }

    @Override // java.lang.Iterable
    public Iterator<HttpMessageConverter<?>> iterator() {
        return getConverters().iterator();
    }

    public List<HttpMessageConverter<?>> getConverters() {
        return this.converters;
    }

    private static void addClassIfExists(List<Class<?>> list, String className) {
        try {
            list.add(Class.forName(className));
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
        }
    }
}
