package org.springframework.web.servlet.view.script;

import java.nio.charset.Charset;
import java.util.function.Supplier;
import javax.script.ScriptEngine;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/view/script/ScriptTemplateConfig.class */
public interface ScriptTemplateConfig {
    @Nullable
    ScriptEngine getEngine();

    @Nullable
    Supplier<ScriptEngine> getEngineSupplier();

    @Nullable
    String getEngineName();

    @Nullable
    Boolean isSharedEngine();

    @Nullable
    String[] getScripts();

    @Nullable
    String getRenderObject();

    @Nullable
    String getRenderFunction();

    @Nullable
    String getContentType();

    @Nullable
    Charset getCharset();

    @Nullable
    String getResourceLoaderPath();
}
