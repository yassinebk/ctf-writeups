package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.Reader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/propertyeditors/ReaderEditor.class */
public class ReaderEditor extends PropertyEditorSupport {
    private final ResourceEditor resourceEditor;

    public ReaderEditor() {
        this.resourceEditor = new ResourceEditor();
    }

    public ReaderEditor(ResourceEditor resourceEditor) {
        Assert.notNull(resourceEditor, "ResourceEditor must not be null");
        this.resourceEditor = resourceEditor;
    }

    public void setAsText(String text) throws IllegalArgumentException {
        Reader reader;
        this.resourceEditor.setAsText(text);
        Resource resource = (Resource) this.resourceEditor.getValue();
        if (resource != null) {
            try {
                reader = new EncodedResource(resource).getReader();
            } catch (IOException ex) {
                throw new IllegalArgumentException("Failed to retrieve Reader for " + resource, ex);
            }
        } else {
            reader = null;
        }
        setValue(reader);
    }

    @Nullable
    public String getAsText() {
        return null;
    }
}
