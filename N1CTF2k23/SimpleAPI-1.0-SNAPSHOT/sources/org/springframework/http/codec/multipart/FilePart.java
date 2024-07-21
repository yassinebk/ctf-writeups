package org.springframework.http.codec.multipart;

import java.io.File;
import java.nio.file.Path;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/multipart/FilePart.class */
public interface FilePart extends Part {
    String filename();

    Mono<Void> transferTo(Path path);

    default Mono<Void> transferTo(File dest) {
        return transferTo(dest.toPath());
    }
}
