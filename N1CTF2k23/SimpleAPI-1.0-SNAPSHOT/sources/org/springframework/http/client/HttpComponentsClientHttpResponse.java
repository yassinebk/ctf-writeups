package org.springframework.http.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/HttpComponentsClientHttpResponse.class */
final class HttpComponentsClientHttpResponse extends AbstractClientHttpResponse {
    private final HttpResponse httpResponse;
    @Nullable
    private HttpHeaders headers;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HttpComponentsClientHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override // org.springframework.http.client.ClientHttpResponse
    public int getRawStatusCode() throws IOException {
        return this.httpResponse.getStatusLine().getStatusCode();
    }

    @Override // org.springframework.http.client.ClientHttpResponse
    public String getStatusText() throws IOException {
        return this.httpResponse.getStatusLine().getReasonPhrase();
    }

    @Override // org.springframework.http.HttpMessage
    public HttpHeaders getHeaders() {
        Header[] allHeaders;
        if (this.headers == null) {
            this.headers = new HttpHeaders();
            for (Header header : this.httpResponse.getAllHeaders()) {
                this.headers.add(header.getName(), header.getValue());
            }
        }
        return this.headers;
    }

    @Override // org.springframework.http.HttpInputMessage
    public InputStream getBody() throws IOException {
        HttpEntity entity = this.httpResponse.getEntity();
        return entity != null ? entity.getContent() : StreamUtils.emptyInput();
    }

    @Override // org.springframework.http.client.ClientHttpResponse, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            EntityUtils.consume(this.httpResponse.getEntity());
            if (this.httpResponse instanceof Closeable) {
                this.httpResponse.close();
            }
        } catch (IOException e) {
        }
    }
}
