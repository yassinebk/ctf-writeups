package org.springframework.web.client;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
/* compiled from: RestOperationsExtensions.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 2, d1 = {"��:\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n��\n\u0002\u0010\u0011\n\u0002\u0010��\n��\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u000f\u001a;\u0010��\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\tH\u0086\b\u001aT\u0010��\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\u0006\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\t2\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0\f\"\u00020\rH\u0086\b¢\u0006\u0002\u0010\u000e\u001aM\u0010��\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\u0006\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\t2\u0010\u0010\u000b\u001a\f\u0012\u0004\u0012\u00020\n\u0012\u0002\b\u00030\u000fH\u0086\b\u001a'\u0010��\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\u0010H\u0086\b\u001a#\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0086\b\u001a<\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0\f\"\u00020\rH\u0086\b¢\u0006\u0002\u0010\u0012\u001a5\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\u0010\u0010\u000b\u001a\f\u0012\u0004\u0012\u00020\n\u0012\u0002\b\u00030\u000fH\u0086\b\u001a\"\u0010\u0013\u001a\u0002H\u0002\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0086\b¢\u0006\u0002\u0010\u0014\u001a6\u0010\u0013\u001a\u0002H\u0002\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0\f\"\u00020\rH\u0086\b¢\u0006\u0002\u0010\u0015\u001a8\u0010\u0013\u001a\u0002H\u0002\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\u0014\u0010\u000b\u001a\u0010\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u000fH\u0086\b¢\u0006\u0002\u0010\u0016\u001a.\u0010\u0017\u001a\u0002H\u0002\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\rH\u0086\b¢\u0006\u0002\u0010\u0019\u001aB\u0010\u0017\u001a\u0002H\u0002\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\r2\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0\f\"\u00020\rH\u0086\b¢\u0006\u0002\u0010\u001a\u001a@\u0010\u0017\u001a\u0002H\u0002\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\r2\u0010\u0010\u000b\u001a\f\u0012\u0004\u0012\u00020\n\u0012\u0002\b\u00030\u000fH\u0086\b¢\u0006\u0002\u0010\u001b\u001a/\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\rH\u0086\b\u001aH\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\r2\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0\f\"\u00020\rH\u0086\b¢\u0006\u0002\u0010\u001d\u001aA\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\r2\u0010\u0010\u000b\u001a\f\u0012\u0004\u0012\u00020\n\u0012\u0002\b\u00030\u000fH\u0086\b\u001a.\u0010\u001e\u001a\u0002H\u0002\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\rH\u0086\b¢\u0006\u0002\u0010\u0019\u001aB\u0010\u001e\u001a\u0002H\u0002\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\r2\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0\f\"\u00020\rH\u0086\b¢\u0006\u0002\u0010\u001a\u001a@\u0010\u001e\u001a\u0002H\u0002\"\u0006\b��\u0010\u0002\u0018\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\r2\u0010\u0010\u000b\u001a\f\u0012\u0004\u0012\u00020\n\u0012\u0002\b\u00030\u000fH\u0086\b¢\u0006\u0002\u0010\u001b¨\u0006\u001f"}, d2 = {"exchange", "Lorg/springframework/http/ResponseEntity;", "T", "Lorg/springframework/web/client/RestOperations;", "url", "Ljava/net/URI;", "method", "Lorg/springframework/http/HttpMethod;", "requestEntity", "Lorg/springframework/http/HttpEntity;", "", "uriVariables", "", "", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;Lorg/springframework/http/HttpMethod;Lorg/springframework/http/HttpEntity;[Ljava/lang/Object;)Lorg/springframework/http/ResponseEntity;", "", "Lorg/springframework/http/RequestEntity;", "getForEntity", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;[Ljava/lang/Object;)Lorg/springframework/http/ResponseEntity;", "getForObject", "(Lorg/springframework/web/client/RestOperations;Ljava/net/URI;)Ljava/lang/Object;", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;Ljava/util/Map;)Ljava/lang/Object;", "patchForObject", "request", "(Lorg/springframework/web/client/RestOperations;Ljava/net/URI;Ljava/lang/Object;)Ljava/lang/Object;", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;Ljava/lang/Object;Ljava/util/Map;)Ljava/lang/Object;", "postForEntity", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)Lorg/springframework/http/ResponseEntity;", "postForObject", "spring-web"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/client/RestOperationsExtensionsKt.class */
public final class RestOperationsExtensionsKt {
    public static final /* synthetic */ <T> T getForObject(@NotNull RestOperations $this$getForObject, @NotNull String url, @NotNull Object... uriVariables) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$getForObject, "$this$getForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object forObject = $this$getForObject.getForObject(url, Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
        Intrinsics.reifiedOperationMarker(1, "T");
        return (T) forObject;
    }

    public static final /* synthetic */ <T> T getForObject(@NotNull RestOperations $this$getForObject, @NotNull String url, @NotNull Map<String, ? extends Object> map) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$getForObject, "$this$getForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(map, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object forObject = $this$getForObject.getForObject(url, Object.class, map);
        Intrinsics.reifiedOperationMarker(1, "T");
        return (T) forObject;
    }

    public static final /* synthetic */ <T> T getForObject(@NotNull RestOperations $this$getForObject, @NotNull URI url) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$getForObject, "$this$getForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object forObject = $this$getForObject.getForObject(url, Object.class);
        Intrinsics.reifiedOperationMarker(1, "T");
        return (T) forObject;
    }

    @NotNull
    public static final /* synthetic */ <T> ResponseEntity<T> getForEntity(@NotNull RestOperations $this$getForEntity, @NotNull URI url) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$getForEntity, "$this$getForEntity");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.reifiedOperationMarker(4, "T");
        ResponseEntity<T> forEntity = $this$getForEntity.getForEntity(url, Object.class);
        Intrinsics.checkExpressionValueIsNotNull(forEntity, "getForEntity(url, T::class.java)");
        return forEntity;
    }

    @NotNull
    public static final /* synthetic */ <T> ResponseEntity<T> getForEntity(@NotNull RestOperations $this$getForEntity, @NotNull String url, @NotNull Object... uriVariables) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$getForEntity, "$this$getForEntity");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        ResponseEntity<T> forEntity = $this$getForEntity.getForEntity(url, Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
        Intrinsics.checkExpressionValueIsNotNull(forEntity, "getForEntity(url, T::class.java, *uriVariables)");
        return forEntity;
    }

    @NotNull
    public static final /* synthetic */ <T> ResponseEntity<T> getForEntity(@NotNull RestOperations $this$getForEntity, @NotNull String url, @NotNull Map<String, ?> map) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$getForEntity, "$this$getForEntity");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(map, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        ResponseEntity<T> forEntity = $this$getForEntity.getForEntity(url, Object.class, map);
        Intrinsics.checkExpressionValueIsNotNull(forEntity, "getForEntity(url, T::class.java, uriVariables)");
        return forEntity;
    }

    public static /* synthetic */ Object patchForObject$default(RestOperations $this$patchForObject, String url, Object request, Object[] uriVariables, int i, Object obj) throws RestClientException {
        if ((i & 2) != 0) {
            request = null;
        }
        Intrinsics.checkParameterIsNotNull($this$patchForObject, "$this$patchForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object patchForObject = $this$patchForObject.patchForObject(url, request, Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
        Intrinsics.reifiedOperationMarker(1, "T");
        return patchForObject;
    }

    public static final /* synthetic */ <T> T patchForObject(@NotNull RestOperations $this$patchForObject, @NotNull String url, @Nullable Object request, @NotNull Object... uriVariables) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$patchForObject, "$this$patchForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object patchForObject = $this$patchForObject.patchForObject(url, request, Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
        Intrinsics.reifiedOperationMarker(1, "T");
        return (T) patchForObject;
    }

    public static /* synthetic */ Object patchForObject$default(RestOperations $this$patchForObject, String url, Object request, Map uriVariables, int i, Object obj) throws RestClientException {
        if ((i & 2) != 0) {
            request = null;
        }
        Intrinsics.checkParameterIsNotNull($this$patchForObject, "$this$patchForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object patchForObject = $this$patchForObject.patchForObject(url, request, Object.class, uriVariables);
        Intrinsics.reifiedOperationMarker(1, "T");
        return patchForObject;
    }

    public static final /* synthetic */ <T> T patchForObject(@NotNull RestOperations $this$patchForObject, @NotNull String url, @Nullable Object request, @NotNull Map<String, ?> map) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$patchForObject, "$this$patchForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(map, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object patchForObject = $this$patchForObject.patchForObject(url, request, Object.class, map);
        Intrinsics.reifiedOperationMarker(1, "T");
        return (T) patchForObject;
    }

    public static /* synthetic */ Object patchForObject$default(RestOperations $this$patchForObject, URI url, Object request, int i, Object obj) throws RestClientException {
        if ((i & 2) != 0) {
            request = null;
        }
        Intrinsics.checkParameterIsNotNull($this$patchForObject, "$this$patchForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object patchForObject = $this$patchForObject.patchForObject(url, request, Object.class);
        Intrinsics.reifiedOperationMarker(1, "T");
        return patchForObject;
    }

    public static final /* synthetic */ <T> T patchForObject(@NotNull RestOperations $this$patchForObject, @NotNull URI url, @Nullable Object request) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$patchForObject, "$this$patchForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object patchForObject = $this$patchForObject.patchForObject(url, request, Object.class);
        Intrinsics.reifiedOperationMarker(1, "T");
        return (T) patchForObject;
    }

    public static /* synthetic */ Object postForObject$default(RestOperations $this$postForObject, String url, Object request, Object[] uriVariables, int i, Object obj) throws RestClientException {
        if ((i & 2) != 0) {
            request = null;
        }
        Intrinsics.checkParameterIsNotNull($this$postForObject, "$this$postForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object postForObject = $this$postForObject.postForObject(url, request, Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
        Intrinsics.reifiedOperationMarker(1, "T");
        return postForObject;
    }

    public static final /* synthetic */ <T> T postForObject(@NotNull RestOperations $this$postForObject, @NotNull String url, @Nullable Object request, @NotNull Object... uriVariables) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$postForObject, "$this$postForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object postForObject = $this$postForObject.postForObject(url, request, Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
        Intrinsics.reifiedOperationMarker(1, "T");
        return (T) postForObject;
    }

    public static /* synthetic */ Object postForObject$default(RestOperations $this$postForObject, String url, Object request, Map uriVariables, int i, Object obj) throws RestClientException {
        if ((i & 2) != 0) {
            request = null;
        }
        Intrinsics.checkParameterIsNotNull($this$postForObject, "$this$postForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object postForObject = $this$postForObject.postForObject(url, request, Object.class, uriVariables);
        Intrinsics.reifiedOperationMarker(1, "T");
        return postForObject;
    }

    public static final /* synthetic */ <T> T postForObject(@NotNull RestOperations $this$postForObject, @NotNull String url, @Nullable Object request, @NotNull Map<String, ?> map) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$postForObject, "$this$postForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(map, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object postForObject = $this$postForObject.postForObject(url, request, Object.class, map);
        Intrinsics.reifiedOperationMarker(1, "T");
        return (T) postForObject;
    }

    public static /* synthetic */ Object postForObject$default(RestOperations $this$postForObject, URI url, Object request, int i, Object obj) throws RestClientException {
        if ((i & 2) != 0) {
            request = null;
        }
        Intrinsics.checkParameterIsNotNull($this$postForObject, "$this$postForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object postForObject = $this$postForObject.postForObject(url, request, Object.class);
        Intrinsics.reifiedOperationMarker(1, "T");
        return postForObject;
    }

    public static final /* synthetic */ <T> T postForObject(@NotNull RestOperations $this$postForObject, @NotNull URI url, @Nullable Object request) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$postForObject, "$this$postForObject");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.reifiedOperationMarker(4, "T");
        Object postForObject = $this$postForObject.postForObject(url, request, Object.class);
        Intrinsics.reifiedOperationMarker(1, "T");
        return (T) postForObject;
    }

    public static /* synthetic */ ResponseEntity postForEntity$default(RestOperations $this$postForEntity, String url, Object request, Object[] uriVariables, int i, Object obj) throws RestClientException {
        if ((i & 2) != 0) {
            request = null;
        }
        Intrinsics.checkParameterIsNotNull($this$postForEntity, "$this$postForEntity");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        ResponseEntity postForEntity = $this$postForEntity.postForEntity(url, request, Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
        Intrinsics.checkExpressionValueIsNotNull(postForEntity, "postForEntity(url, reque…lass.java, *uriVariables)");
        return postForEntity;
    }

    @NotNull
    public static final /* synthetic */ <T> ResponseEntity<T> postForEntity(@NotNull RestOperations $this$postForEntity, @NotNull String url, @Nullable Object request, @NotNull Object... uriVariables) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$postForEntity, "$this$postForEntity");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        ResponseEntity<T> postForEntity = $this$postForEntity.postForEntity(url, request, Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
        Intrinsics.checkExpressionValueIsNotNull(postForEntity, "postForEntity(url, reque…lass.java, *uriVariables)");
        return postForEntity;
    }

    public static /* synthetic */ ResponseEntity postForEntity$default(RestOperations $this$postForEntity, String url, Object request, Map uriVariables, int i, Object obj) throws RestClientException {
        if ((i & 2) != 0) {
            request = null;
        }
        Intrinsics.checkParameterIsNotNull($this$postForEntity, "$this$postForEntity");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        ResponseEntity postForEntity = $this$postForEntity.postForEntity(url, request, Object.class, uriVariables);
        Intrinsics.checkExpressionValueIsNotNull(postForEntity, "postForEntity(url, reque…class.java, uriVariables)");
        return postForEntity;
    }

    @NotNull
    public static final /* synthetic */ <T> ResponseEntity<T> postForEntity(@NotNull RestOperations $this$postForEntity, @NotNull String url, @Nullable Object request, @NotNull Map<String, ?> map) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$postForEntity, "$this$postForEntity");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(map, "uriVariables");
        Intrinsics.reifiedOperationMarker(4, "T");
        ResponseEntity<T> postForEntity = $this$postForEntity.postForEntity(url, request, Object.class, map);
        Intrinsics.checkExpressionValueIsNotNull(postForEntity, "postForEntity(url, reque…class.java, uriVariables)");
        return postForEntity;
    }

    public static /* synthetic */ ResponseEntity postForEntity$default(RestOperations $this$postForEntity, URI url, Object request, int i, Object obj) throws RestClientException {
        if ((i & 2) != 0) {
            request = null;
        }
        Intrinsics.checkParameterIsNotNull($this$postForEntity, "$this$postForEntity");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.reifiedOperationMarker(4, "T");
        ResponseEntity postForEntity = $this$postForEntity.postForEntity(url, request, Object.class);
        Intrinsics.checkExpressionValueIsNotNull(postForEntity, "postForEntity(url, request, T::class.java)");
        return postForEntity;
    }

    @NotNull
    public static final /* synthetic */ <T> ResponseEntity<T> postForEntity(@NotNull RestOperations $this$postForEntity, @NotNull URI url, @Nullable Object request) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$postForEntity, "$this$postForEntity");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.reifiedOperationMarker(4, "T");
        ResponseEntity<T> postForEntity = $this$postForEntity.postForEntity(url, request, Object.class);
        Intrinsics.checkExpressionValueIsNotNull(postForEntity, "postForEntity(url, request, T::class.java)");
        return postForEntity;
    }

    public static /* synthetic */ ResponseEntity exchange$default(RestOperations $this$exchange, String url, HttpMethod method, HttpEntity requestEntity, Object[] uriVariables, int i, Object obj) throws RestClientException {
        if ((i & 4) != 0) {
            requestEntity = null;
        }
        Intrinsics.checkParameterIsNotNull($this$exchange, "$this$exchange");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(method, "method");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.needClassReification();
        ResponseEntity exchange = $this$exchange.exchange(url, method, requestEntity, new RestOperationsExtensionsKt$exchange$1(), Arrays.copyOf(uriVariables, uriVariables.length));
        Intrinsics.checkExpressionValueIsNotNull(exchange, "exchange(url, method, re…e<T>() {}, *uriVariables)");
        return exchange;
    }

    @NotNull
    public static final /* synthetic */ <T> ResponseEntity<T> exchange(@NotNull RestOperations $this$exchange, @NotNull String url, @NotNull HttpMethod method, @Nullable HttpEntity<?> httpEntity, @NotNull Object... uriVariables) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$exchange, "$this$exchange");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(method, "method");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.needClassReification();
        ResponseEntity<T> exchange = $this$exchange.exchange(url, method, httpEntity, new RestOperationsExtensionsKt$exchange$1(), Arrays.copyOf(uriVariables, uriVariables.length));
        Intrinsics.checkExpressionValueIsNotNull(exchange, "exchange(url, method, re…e<T>() {}, *uriVariables)");
        return exchange;
    }

    public static /* synthetic */ ResponseEntity exchange$default(RestOperations $this$exchange, String url, HttpMethod method, HttpEntity requestEntity, Map uriVariables, int i, Object obj) throws RestClientException {
        if ((i & 4) != 0) {
            requestEntity = null;
        }
        Intrinsics.checkParameterIsNotNull($this$exchange, "$this$exchange");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(method, "method");
        Intrinsics.checkParameterIsNotNull(uriVariables, "uriVariables");
        Intrinsics.needClassReification();
        ResponseEntity exchange = $this$exchange.exchange(url, method, requestEntity, new RestOperationsExtensionsKt$exchange$2(), uriVariables);
        Intrinsics.checkExpressionValueIsNotNull(exchange, "exchange(url, method, re…ce<T>() {}, uriVariables)");
        return exchange;
    }

    @NotNull
    public static final /* synthetic */ <T> ResponseEntity<T> exchange(@NotNull RestOperations $this$exchange, @NotNull String url, @NotNull HttpMethod method, @Nullable HttpEntity<?> httpEntity, @NotNull Map<String, ?> map) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$exchange, "$this$exchange");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(method, "method");
        Intrinsics.checkParameterIsNotNull(map, "uriVariables");
        Intrinsics.needClassReification();
        ResponseEntity<T> exchange = $this$exchange.exchange(url, method, httpEntity, new RestOperationsExtensionsKt$exchange$2(), map);
        Intrinsics.checkExpressionValueIsNotNull(exchange, "exchange(url, method, re…ce<T>() {}, uriVariables)");
        return exchange;
    }

    public static /* synthetic */ ResponseEntity exchange$default(RestOperations $this$exchange, URI url, HttpMethod method, HttpEntity requestEntity, int i, Object obj) throws RestClientException {
        if ((i & 4) != 0) {
            requestEntity = null;
        }
        Intrinsics.checkParameterIsNotNull($this$exchange, "$this$exchange");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(method, "method");
        Intrinsics.needClassReification();
        ResponseEntity exchange = $this$exchange.exchange(url, method, requestEntity, new RestOperationsExtensionsKt$exchange$3());
        Intrinsics.checkExpressionValueIsNotNull(exchange, "exchange(url, method, re…zedTypeReference<T>() {})");
        return exchange;
    }

    @NotNull
    public static final /* synthetic */ <T> ResponseEntity<T> exchange(@NotNull RestOperations $this$exchange, @NotNull URI url, @NotNull HttpMethod method, @Nullable HttpEntity<?> httpEntity) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$exchange, "$this$exchange");
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(method, "method");
        Intrinsics.needClassReification();
        ResponseEntity<T> exchange = $this$exchange.exchange(url, method, httpEntity, new RestOperationsExtensionsKt$exchange$3());
        Intrinsics.checkExpressionValueIsNotNull(exchange, "exchange(url, method, re…zedTypeReference<T>() {})");
        return exchange;
    }

    @NotNull
    public static final /* synthetic */ <T> ResponseEntity<T> exchange(@NotNull RestOperations $this$exchange, @NotNull RequestEntity<?> requestEntity) throws RestClientException {
        Intrinsics.checkParameterIsNotNull($this$exchange, "$this$exchange");
        Intrinsics.checkParameterIsNotNull(requestEntity, "requestEntity");
        Intrinsics.needClassReification();
        ResponseEntity<T> exchange = $this$exchange.exchange(requestEntity, new ParameterizedTypeReference<T>() { // from class: org.springframework.web.client.RestOperationsExtensionsKt$exchange$4
        });
        Intrinsics.checkExpressionValueIsNotNull(exchange, "exchange(requestEntity, …zedTypeReference<T>() {})");
        return exchange;
    }
}
