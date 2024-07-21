package org.springframework.web.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.PropertyAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/client/DefaultResponseErrorHandler.class */
public class DefaultResponseErrorHandler implements ResponseErrorHandler {
    @Override // org.springframework.web.client.ResponseErrorHandler
    public boolean hasError(ClientHttpResponse response) throws IOException {
        int rawStatusCode = response.getRawStatusCode();
        HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
        return statusCode != null ? hasError(statusCode) : hasError(rawStatusCode);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasError(HttpStatus statusCode) {
        return statusCode.isError();
    }

    protected boolean hasError(int unknownStatusCode) {
        HttpStatus.Series series = HttpStatus.Series.resolve(unknownStatusCode);
        return series == HttpStatus.Series.CLIENT_ERROR || series == HttpStatus.Series.SERVER_ERROR;
    }

    @Override // org.springframework.web.client.ResponseErrorHandler
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        if (statusCode == null) {
            byte[] body = getResponseBody(response);
            String message = getErrorMessage(response.getRawStatusCode(), response.getStatusText(), body, getCharset(response));
            throw new UnknownHttpStatusCodeException(message, response.getRawStatusCode(), response.getStatusText(), response.getHeaders(), body, getCharset(response));
        }
        handleError(response, statusCode);
    }

    private String getErrorMessage(int rawStatusCode, String statusText, @Nullable byte[] responseBody, @Nullable Charset charset) {
        String preface = rawStatusCode + " " + statusText + ": ";
        if (ObjectUtils.isEmpty(responseBody)) {
            return preface + "[no body]";
        }
        Charset charset2 = charset == null ? StandardCharsets.UTF_8 : charset;
        if (responseBody.length < 200 * 2) {
            return preface + PropertyAccessor.PROPERTY_KEY_PREFIX + new String(responseBody, charset2) + "]";
        }
        try {
            Reader reader = new InputStreamReader(new ByteArrayInputStream(responseBody), charset2);
            CharBuffer buffer = CharBuffer.allocate(200);
            reader.read(buffer);
            reader.close();
            buffer.flip();
            return preface + PropertyAccessor.PROPERTY_KEY_PREFIX + buffer.toString() + "... (" + responseBody.length + " bytes)]";
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        String statusText = response.getStatusText();
        HttpHeaders headers = response.getHeaders();
        byte[] body = getResponseBody(response);
        Charset charset = getCharset(response);
        String message = getErrorMessage(statusCode.value(), statusText, body, charset);
        switch (statusCode.series()) {
            case CLIENT_ERROR:
                throw HttpClientErrorException.create(message, statusCode, statusText, headers, body, charset);
            case SERVER_ERROR:
                throw HttpServerErrorException.create(message, statusCode, statusText, headers, body, charset);
            default:
                throw new UnknownHttpStatusCodeException(message, statusCode.value(), statusText, headers, body, charset);
        }
    }

    @Deprecated
    protected HttpStatus getHttpStatusCode(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        if (statusCode == null) {
            throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
        }
        return statusCode;
    }

    protected byte[] getResponseBody(ClientHttpResponse response) {
        try {
            return FileCopyUtils.copyToByteArray(response.getBody());
        } catch (IOException e) {
            return new byte[0];
        }
    }

    @Nullable
    protected Charset getCharset(ClientHttpResponse response) {
        HttpHeaders headers = response.getHeaders();
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            return contentType.getCharset();
        }
        return null;
    }
}
