package org.apache.tomcat.util.http.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/parser/TokenList.class */
public class TokenList {
    private TokenList() {
    }

    public static boolean parseTokenList(Enumeration<String> inputs, Collection<String> collection) throws IOException {
        boolean result = true;
        while (inputs.hasMoreElements()) {
            String nextHeaderValue = inputs.nextElement();
            if (nextHeaderValue != null && !parseTokenList(new StringReader(nextHeaderValue), collection)) {
                result = false;
            }
        }
        return result;
    }

    public static boolean parseTokenList(Reader input, Collection<String> collection) throws IOException {
        boolean invalid = false;
        boolean valid = false;
        while (true) {
            String fieldName = HttpParser.readToken(input);
            if (fieldName == null) {
                invalid = true;
                HttpParser.skipUntil(input, 0, ',');
            } else if (fieldName.length() == 0) {
                break;
            } else {
                SkipResult skipResult = HttpParser.skipConstant(input, ",");
                if (skipResult == SkipResult.EOF) {
                    valid = true;
                    collection.add(fieldName.toLowerCase(Locale.ENGLISH));
                    break;
                } else if (skipResult == SkipResult.FOUND) {
                    valid = true;
                    collection.add(fieldName.toLowerCase(Locale.ENGLISH));
                } else {
                    invalid = true;
                    HttpParser.skipUntil(input, 0, ',');
                }
            }
        }
        return valid && !invalid;
    }
}
