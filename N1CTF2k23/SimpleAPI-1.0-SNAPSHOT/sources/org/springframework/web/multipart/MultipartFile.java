package org.springframework.web.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/multipart/MultipartFile.class */
public interface MultipartFile extends InputStreamSource {
    String getName();

    @Nullable
    String getOriginalFilename();

    @Nullable
    String getContentType();

    boolean isEmpty();

    long getSize();

    byte[] getBytes() throws IOException;

    @Override // org.springframework.core.io.InputStreamSource
    InputStream getInputStream() throws IOException;

    void transferTo(File file) throws IOException, IllegalStateException;

    default Resource getResource() {
        return new MultipartFileResource(this);
    }

    default void transferTo(Path dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(getInputStream(), Files.newOutputStream(dest, new OpenOption[0]));
    }
}
