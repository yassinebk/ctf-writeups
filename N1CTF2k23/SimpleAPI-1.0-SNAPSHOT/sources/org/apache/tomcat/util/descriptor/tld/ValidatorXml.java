package org.apache.tomcat.util.descriptor.tld;

import java.util.HashMap;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/descriptor/tld/ValidatorXml.class */
public class ValidatorXml {
    private String validatorClass;
    private final Map<String, String> initParams = new HashMap();

    public String getValidatorClass() {
        return this.validatorClass;
    }

    public void setValidatorClass(String validatorClass) {
        this.validatorClass = validatorClass;
    }

    public void addInitParam(String name, String value) {
        this.initParams.put(name, value);
    }

    public Map<String, String> getInitParams() {
        return this.initParams;
    }
}
