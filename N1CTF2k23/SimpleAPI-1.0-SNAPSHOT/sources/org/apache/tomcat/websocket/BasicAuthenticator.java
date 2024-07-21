package org.apache.tomcat.websocket;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:org/apache/tomcat/websocket/BasicAuthenticator.class */
public class BasicAuthenticator extends Authenticator {
    public static final String schemeName = "basic";
    public static final String charsetparam = "charset";

    @Override // org.apache.tomcat.websocket.Authenticator
    public String getAuthorization(String requestUri, String WWWAuthenticate, Map<String, Object> userProperties) throws AuthenticationException {
        Charset charset;
        String userName = (String) userProperties.get(Constants.WS_AUTHENTICATION_USER_NAME);
        String password = (String) userProperties.get(Constants.WS_AUTHENTICATION_PASSWORD);
        if (userName == null || password == null) {
            throw new AuthenticationException("Failed to perform Basic authentication due to  missing user/password");
        }
        Map<String, String> wwwAuthenticate = parseWWWAuthenticateHeader(WWWAuthenticate);
        String userPass = userName + ":" + password;
        if (wwwAuthenticate.get(charsetparam) != null && wwwAuthenticate.get(charsetparam).equalsIgnoreCase("UTF-8")) {
            charset = StandardCharsets.UTF_8;
        } else {
            charset = StandardCharsets.ISO_8859_1;
        }
        String base64 = Base64.getEncoder().encodeToString(userPass.getBytes(charset));
        return " Basic " + base64;
    }

    @Override // org.apache.tomcat.websocket.Authenticator
    public String getSchemeName() {
        return schemeName;
    }
}
