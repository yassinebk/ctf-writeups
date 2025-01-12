package org.springframework.boot.autoconfigure.mustache;

import com.samskivert.mustache.Mustache;
import java.io.InputStreamReader;
import java.io.Reader;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mustache/MustacheResourceTemplateLoader.class */
public class MustacheResourceTemplateLoader implements Mustache.TemplateLoader, ResourceLoaderAware {
    private String prefix;
    private String suffix;
    private String charSet;
    private ResourceLoader resourceLoader;

    public MustacheResourceTemplateLoader() {
        this.prefix = "";
        this.suffix = "";
        this.charSet = "UTF-8";
        this.resourceLoader = new DefaultResourceLoader(getClass().getClassLoader());
    }

    public MustacheResourceTemplateLoader(String prefix, String suffix) {
        this.prefix = "";
        this.suffix = "";
        this.charSet = "UTF-8";
        this.resourceLoader = new DefaultResourceLoader(getClass().getClassLoader());
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public void setCharset(String charSet) {
        this.charSet = charSet;
    }

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Reader getTemplate(String name) throws Exception {
        return new InputStreamReader(this.resourceLoader.getResource(this.prefix + name + this.suffix).getInputStream(), this.charSet);
    }
}
