package org.apache.catalina.valves.rewrite;

import ch.qos.logback.classic.spi.CallerData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Pipeline;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.util.URLEncoder;
import org.apache.catalina.valves.ValveBase;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.CharChunk;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.buf.UDecoder;
import org.apache.tomcat.util.buf.UriUtil;
import org.apache.tomcat.util.file.ConfigFileLoader;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.apache.tomcat.util.http.RequestUtil;
import org.springframework.beans.PropertyAccessor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/valves/rewrite/RewriteValve.class */
public class RewriteValve extends ValveBase {
    protected RewriteRule[] rules;
    protected static ThreadLocal<Boolean> invoked = new ThreadLocal<>();
    protected String resourcePath;
    protected boolean context;
    protected boolean enabled;
    protected Map<String, RewriteMap> maps;
    protected ArrayList<String> mapsConfiguration;

    public RewriteValve() {
        super(true);
        this.rules = null;
        this.resourcePath = "rewrite.config";
        this.context = false;
        this.enabled = true;
        this.maps = new Hashtable();
        this.mapsConfiguration = new ArrayList<>();
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.catalina.valves.ValveBase, org.apache.catalina.util.LifecycleMBeanBase, org.apache.catalina.util.LifecycleBase
    public void initInternal() throws LifecycleException {
        super.initInternal();
        this.containerLog = LogFactory.getLog(getContainer().getLogName() + ".rewrite");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.catalina.valves.ValveBase, org.apache.catalina.util.LifecycleBase
    public synchronized void startInternal() throws LifecycleException {
        super.startInternal();
        InputStream is = null;
        if (getContainer() instanceof Context) {
            this.context = true;
            is = ((Context) getContainer()).getServletContext().getResourceAsStream("/WEB-INF/" + this.resourcePath);
            if (this.containerLog.isDebugEnabled()) {
                if (is == null) {
                    this.containerLog.debug("No configuration resource found: /WEB-INF/" + this.resourcePath);
                } else {
                    this.containerLog.debug("Read configuration from: /WEB-INF/" + this.resourcePath);
                }
            }
        } else {
            String resourceName = Container.getConfigPath(getContainer(), this.resourcePath);
            try {
                ConfigurationSource.Resource resource = ConfigFileLoader.getSource().getResource(resourceName);
                is = resource.getInputStream();
            } catch (IOException e) {
                if (this.containerLog.isDebugEnabled()) {
                    this.containerLog.debug("No configuration resource found: " + resourceName, e);
                }
            }
        }
        try {
            if (is == null) {
                return;
            }
            try {
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                Throwable th = null;
                try {
                    BufferedReader reader = new BufferedReader(isr);
                    Throwable th2 = null;
                    try {
                        parse(reader);
                        if (reader != null) {
                            if (0 != 0) {
                                try {
                                    reader.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                reader.close();
                            }
                        }
                        if (isr != null) {
                            if (0 != 0) {
                                try {
                                    isr.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                isr.close();
                            }
                        }
                    } catch (Throwable th5) {
                        try {
                            throw th5;
                        } catch (Throwable th6) {
                            if (reader != null) {
                                if (th5 != null) {
                                    try {
                                        reader.close();
                                    } catch (Throwable th7) {
                                        th5.addSuppressed(th7);
                                    }
                                } else {
                                    reader.close();
                                }
                            }
                            throw th6;
                        }
                    }
                } catch (Throwable th8) {
                    try {
                        throw th8;
                    } catch (Throwable th9) {
                        if (isr != null) {
                            if (th8 != null) {
                                try {
                                    isr.close();
                                } catch (Throwable th10) {
                                    th8.addSuppressed(th10);
                                }
                            } else {
                                isr.close();
                            }
                        }
                        throw th9;
                    }
                }
            } catch (IOException ioe) {
                this.containerLog.error(sm.getString("rewriteValve.closeError"), ioe);
                try {
                    is.close();
                } catch (IOException e2) {
                    this.containerLog.error(sm.getString("rewriteValve.closeError"), e2);
                }
            }
        } finally {
            try {
                is.close();
            } catch (IOException e3) {
                this.containerLog.error(sm.getString("rewriteValve.closeError"), e3);
            }
        }
    }

    public void setConfiguration(String configuration) throws Exception {
        if (this.containerLog == null) {
            this.containerLog = LogFactory.getLog(getContainer().getLogName() + ".rewrite");
        }
        this.maps.clear();
        parse(new BufferedReader(new StringReader(configuration)));
    }

    public String getConfiguration() {
        RewriteRule[] rewriteRuleArr;
        StringBuffer buffer = new StringBuffer();
        Iterator<String> it = this.mapsConfiguration.iterator();
        while (it.hasNext()) {
            String mapConfiguration = it.next();
            buffer.append(mapConfiguration).append("\r\n");
        }
        if (this.mapsConfiguration.size() > 0) {
            buffer.append("\r\n");
        }
        for (RewriteRule rule : this.rules) {
            for (int j = 0; j < rule.getConditions().length; j++) {
                buffer.append(rule.getConditions()[j].toString()).append("\r\n");
            }
            buffer.append(rule.toString()).append("\r\n").append("\r\n");
        }
        return buffer.toString();
    }

    protected void parse(BufferedReader reader) throws LifecycleException {
        String line;
        List<RewriteRule> rules = new ArrayList<>();
        List<RewriteCond> conditions = new ArrayList<>();
        while (true) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                this.containerLog.error(sm.getString("rewriteValve.readError"), e);
            }
            if (line == null) {
                break;
            }
            Object result = parse(line);
            if (result instanceof RewriteRule) {
                RewriteRule rule = (RewriteRule) result;
                if (this.containerLog.isDebugEnabled()) {
                    this.containerLog.debug("Add rule with pattern " + rule.getPatternString() + " and substitution " + rule.getSubstitutionString());
                }
                for (int i = conditions.size() - 1; i > 0; i--) {
                    if (conditions.get(i - 1).isOrnext()) {
                        conditions.get(i).setOrnext(true);
                    }
                }
                for (RewriteCond condition : conditions) {
                    if (this.containerLog.isDebugEnabled()) {
                        this.containerLog.debug("Add condition " + condition.getCondPattern() + " test " + condition.getTestString() + " to rule with pattern " + rule.getPatternString() + " and substitution " + rule.getSubstitutionString() + (condition.isOrnext() ? " [OR]" : "") + (condition.isNocase() ? " [NC]" : ""));
                    }
                    rule.addCondition(condition);
                }
                conditions.clear();
                rules.add(rule);
            } else if (result instanceof RewriteCond) {
                conditions.add((RewriteCond) result);
            } else if (result instanceof Object[]) {
                String mapName = (String) ((Object[]) result)[0];
                RewriteMap map = (RewriteMap) ((Object[]) result)[1];
                this.maps.put(mapName, map);
                this.mapsConfiguration.add(line);
                if (map instanceof Lifecycle) {
                    ((Lifecycle) map).start();
                }
            }
        }
        this.rules = (RewriteRule[]) rules.toArray(new RewriteRule[0]);
        for (RewriteRule rule2 : this.rules) {
            rule2.parse(this.maps);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.catalina.valves.ValveBase, org.apache.catalina.util.LifecycleBase
    public synchronized void stopInternal() throws LifecycleException {
        super.stopInternal();
        for (RewriteMap map : this.maps.values()) {
            if (map instanceof Lifecycle) {
                ((Lifecycle) map).stop();
            }
        }
        this.maps.clear();
        this.rules = null;
    }

    @Override // org.apache.catalina.Valve
    public void invoke(Request request, Response response) throws IOException, ServletException {
        String rewrittenQueryStringDecoded;
        if (!getEnabled() || this.rules == null || this.rules.length == 0) {
            getNext().invoke(request, response);
        } else if (Boolean.TRUE.equals(invoked.get())) {
            try {
                getNext().invoke(request, response);
                invoked.set(null);
            } catch (Throwable th) {
                invoked.set(null);
                throw th;
            }
        } else {
            try {
                Resolver resolver = new ResolverImpl(request);
                invoked.set(Boolean.TRUE);
                Charset uriCharset = request.getConnector().getURICharset();
                String originalQueryStringEncoded = request.getQueryString();
                MessageBytes urlMB = this.context ? request.getRequestPathMB() : request.getDecodedRequestURIMB();
                urlMB.toChars();
                CharSequence urlDecoded = urlMB.getCharChunk();
                CharSequence host = request.getServerName();
                boolean rewritten = false;
                boolean done = false;
                boolean qsa = false;
                boolean qsd = false;
                int i = 0;
                while (true) {
                    if (i >= this.rules.length) {
                        break;
                    }
                    RewriteRule rule = this.rules[i];
                    CharSequence test = rule.isHost() ? host : urlDecoded;
                    CharSequence newtest = rule.evaluate(test, resolver);
                    if (newtest != null && !test.equals(newtest.toString())) {
                        if (this.containerLog.isDebugEnabled()) {
                            this.containerLog.debug("Rewrote " + ((Object) test) + " as " + ((Object) newtest) + " with rule pattern " + rule.getPatternString());
                        }
                        if (rule.isHost()) {
                            host = newtest;
                        } else {
                            urlDecoded = newtest;
                        }
                        rewritten = true;
                    }
                    if (!qsa && newtest != 0 && rule.isQsappend()) {
                        qsa = true;
                    }
                    if (!qsa && newtest != 0 && rule.isQsdiscard()) {
                        qsd = true;
                    }
                    if (rule.isForbidden() && newtest != 0) {
                        response.sendError(403);
                        done = true;
                        break;
                    } else if (rule.isGone() && newtest != 0) {
                        response.sendError(HttpServletResponse.SC_GONE);
                        done = true;
                        break;
                    } else if (rule.isRedirect() && newtest != 0) {
                        String urlStringDecoded = urlDecoded.toString();
                        int index = urlStringDecoded.indexOf(CallerData.NA);
                        if (index == -1) {
                            rewrittenQueryStringDecoded = null;
                        } else {
                            rewrittenQueryStringDecoded = urlStringDecoded.substring(index + 1);
                            urlStringDecoded = urlStringDecoded.substring(0, index);
                        }
                        StringBuffer urlStringEncoded = new StringBuffer(URLEncoder.DEFAULT.encode(urlStringDecoded, uriCharset));
                        if (!qsd && originalQueryStringEncoded != null && originalQueryStringEncoded.length() > 0) {
                            if (rewrittenQueryStringDecoded == null) {
                                urlStringEncoded.append('?');
                                urlStringEncoded.append(originalQueryStringEncoded);
                            } else if (qsa) {
                                urlStringEncoded.append('?');
                                urlStringEncoded.append(URLEncoder.QUERY.encode(rewrittenQueryStringDecoded, uriCharset));
                                urlStringEncoded.append('&');
                                urlStringEncoded.append(originalQueryStringEncoded);
                            } else if (index == urlStringEncoded.length() - 1) {
                                urlStringEncoded.deleteCharAt(index);
                            } else {
                                urlStringEncoded.append('?');
                                urlStringEncoded.append(URLEncoder.QUERY.encode(rewrittenQueryStringDecoded, uriCharset));
                            }
                        } else if (rewrittenQueryStringDecoded != null) {
                            urlStringEncoded.append('?');
                            urlStringEncoded.append(URLEncoder.QUERY.encode(rewrittenQueryStringDecoded, uriCharset));
                        }
                        if (this.context && urlStringEncoded.charAt(0) == '/' && !UriUtil.hasScheme(urlStringEncoded)) {
                            urlStringEncoded.insert(0, request.getContext().getEncodedPath());
                        }
                        if (rule.isNoescape()) {
                            response.sendRedirect(UDecoder.URLDecode(urlStringEncoded.toString(), uriCharset));
                        } else {
                            response.sendRedirect(urlStringEncoded.toString());
                        }
                        response.setStatus(rule.getRedirectCode());
                        done = true;
                    } else {
                        if (rule.isCookie() && newtest != 0) {
                            Cookie cookie = new Cookie(rule.getCookieName(), rule.getCookieResult());
                            cookie.setDomain(rule.getCookieDomain());
                            cookie.setMaxAge(rule.getCookieLifetime());
                            cookie.setPath(rule.getCookiePath());
                            cookie.setSecure(rule.isCookieSecure());
                            cookie.setHttpOnly(rule.isCookieHttpOnly());
                            response.addCookie(cookie);
                        }
                        if (rule.isEnv() && newtest != 0) {
                            for (int j = 0; j < rule.getEnvSize(); j++) {
                                request.setAttribute(rule.getEnvName(j), rule.getEnvResult(j));
                            }
                        }
                        if (rule.isType() && newtest != 0) {
                            request.setContentType(rule.getTypeValue());
                        }
                        if (rule.isChain() && newtest == 0) {
                            int j2 = i;
                            while (true) {
                                if (j2 < this.rules.length) {
                                    if (this.rules[j2].isChain()) {
                                        j2++;
                                    } else {
                                        i = j2;
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }
                        } else if (rule.isLast() && newtest != 0) {
                            break;
                        } else if (rule.isNext() && newtest != 0) {
                            i = 0;
                        } else if (newtest != 0) {
                            i += rule.getSkip();
                        }
                        i++;
                    }
                }
                if (rewritten) {
                    if (!done) {
                        String urlStringDecoded2 = urlDecoded.toString();
                        String queryStringDecoded = null;
                        int queryIndex = urlStringDecoded2.indexOf(63);
                        if (queryIndex != -1) {
                            queryStringDecoded = urlStringDecoded2.substring(queryIndex + 1);
                            urlStringDecoded2 = urlStringDecoded2.substring(0, queryIndex);
                        }
                        String contextPath = null;
                        if (this.context) {
                            contextPath = request.getContextPath();
                        }
                        request.getCoyoteRequest().requestURI().setString(null);
                        CharChunk chunk = request.getCoyoteRequest().requestURI().getCharChunk();
                        chunk.recycle();
                        if (this.context) {
                            chunk.append(contextPath);
                        }
                        chunk.append(URLEncoder.DEFAULT.encode(urlStringDecoded2, uriCharset));
                        request.getCoyoteRequest().requestURI().toChars();
                        String urlStringDecoded3 = RequestUtil.normalize(urlStringDecoded2);
                        request.getCoyoteRequest().decodedURI().setString(null);
                        CharChunk chunk2 = request.getCoyoteRequest().decodedURI().getCharChunk();
                        chunk2.recycle();
                        if (this.context) {
                            chunk2.append(request.getServletContext().getContextPath());
                        }
                        chunk2.append(urlStringDecoded3);
                        request.getCoyoteRequest().decodedURI().toChars();
                        if (queryStringDecoded != null) {
                            request.getCoyoteRequest().queryString().setString(null);
                            CharChunk chunk3 = request.getCoyoteRequest().queryString().getCharChunk();
                            chunk3.recycle();
                            chunk3.append(URLEncoder.QUERY.encode(queryStringDecoded, uriCharset));
                            if (qsa && originalQueryStringEncoded != null && originalQueryStringEncoded.length() > 0) {
                                chunk3.append('&');
                                chunk3.append(originalQueryStringEncoded);
                            }
                            if (!chunk3.isNull()) {
                                request.getCoyoteRequest().queryString().toChars();
                            }
                        }
                        if (!host.equals(request.getServerName())) {
                            request.getCoyoteRequest().serverName().setString(null);
                            CharChunk chunk4 = request.getCoyoteRequest().serverName().getCharChunk();
                            chunk4.recycle();
                            chunk4.append(host.toString());
                            request.getCoyoteRequest().serverName().toChars();
                        }
                        request.getMappingData().recycle();
                        Connector connector = request.getConnector();
                        if (connector.getProtocolHandler().getAdapter().prepare(request.getCoyoteRequest(), response.getCoyoteResponse())) {
                            Pipeline pipeline = connector.getService().getContainer().getPipeline();
                            request.setAsyncSupported(pipeline.isAsyncSupported());
                            pipeline.getFirst().invoke(request, response);
                        } else {
                            invoked.set(null);
                            return;
                        }
                    }
                } else {
                    getNext().invoke(request, response);
                }
                invoked.set(null);
            } finally {
                invoked.set(null);
            }
        }
    }

    public static Object parse(String line) {
        QuotedStringTokenizer tokenizer = new QuotedStringTokenizer(line);
        if (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.equals("RewriteCond")) {
                RewriteCond condition = new RewriteCond();
                if (tokenizer.countTokens() < 2) {
                    throw new IllegalArgumentException(sm.getString("rewriteValve.invalidLine", line));
                }
                condition.setTestString(tokenizer.nextToken());
                condition.setCondPattern(tokenizer.nextToken());
                if (tokenizer.hasMoreTokens()) {
                    String flags = tokenizer.nextToken();
                    condition.setFlagsString(flags);
                    if (flags.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX) && flags.endsWith("]")) {
                        flags = flags.substring(1, flags.length() - 1);
                    }
                    StringTokenizer flagsTokenizer = new StringTokenizer(flags, ",");
                    while (flagsTokenizer.hasMoreElements()) {
                        parseCondFlag(line, condition, flagsTokenizer.nextToken());
                    }
                }
                return condition;
            } else if (token.equals("RewriteRule")) {
                RewriteRule rule = new RewriteRule();
                if (tokenizer.countTokens() < 2) {
                    throw new IllegalArgumentException(sm.getString("rewriteValve.invalidLine", line));
                }
                rule.setPatternString(tokenizer.nextToken());
                rule.setSubstitutionString(tokenizer.nextToken());
                if (tokenizer.hasMoreTokens()) {
                    String flags2 = tokenizer.nextToken();
                    rule.setFlagsString(flags2);
                    if (flags2.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX) && flags2.endsWith("]")) {
                        flags2 = flags2.substring(1, flags2.length() - 1);
                    }
                    StringTokenizer flagsTokenizer2 = new StringTokenizer(flags2, ",");
                    while (flagsTokenizer2.hasMoreElements()) {
                        parseRuleFlag(line, rule, flagsTokenizer2.nextToken());
                    }
                }
                return rule;
            } else if (token.equals("RewriteMap")) {
                if (tokenizer.countTokens() < 2) {
                    throw new IllegalArgumentException(sm.getString("rewriteValve.invalidLine", line));
                }
                String name = tokenizer.nextToken();
                String rewriteMapClassName = tokenizer.nextToken();
                try {
                    RewriteMap map = (RewriteMap) Class.forName(rewriteMapClassName).getConstructor(new Class[0]).newInstance(new Object[0]);
                    if (tokenizer.hasMoreTokens()) {
                        map.setParameters(tokenizer.nextToken());
                    }
                    Object[] result = {name, map};
                    return result;
                } catch (Exception e) {
                    throw new IllegalArgumentException(sm.getString("rewriteValve.invalidMapClassName", line));
                }
            } else if (!token.startsWith("#")) {
                throw new IllegalArgumentException(sm.getString("rewriteValve.invalidLine", line));
            } else {
                return null;
            }
        }
        return null;
    }

    protected static void parseCondFlag(String line, RewriteCond condition, String flag) {
        if (flag.equals("NC") || flag.equals("nocase")) {
            condition.setNocase(true);
        } else if (flag.equals("OR") || flag.equals("ornext")) {
            condition.setOrnext(true);
        } else {
            throw new IllegalArgumentException(sm.getString("rewriteValve.invalidFlags", line, flag));
        }
    }

    protected static void parseRuleFlag(String line, RewriteRule rule, String flag) {
        if (flag.equals("B")) {
            rule.setEscapeBackReferences(true);
        } else if (flag.equals("chain") || flag.equals("C")) {
            rule.setChain(true);
        } else if (flag.startsWith("cookie=") || flag.startsWith("CO=")) {
            rule.setCookie(true);
            if (flag.startsWith("cookie")) {
                flag = flag.substring("cookie=".length());
            } else if (flag.startsWith("CO=")) {
                flag = flag.substring("CO=".length());
            }
            StringTokenizer tokenizer = new StringTokenizer(flag, ":");
            if (tokenizer.countTokens() < 2) {
                throw new IllegalArgumentException(sm.getString("rewriteValve.invalidFlags", line, flag));
            }
            rule.setCookieName(tokenizer.nextToken());
            rule.setCookieValue(tokenizer.nextToken());
            if (tokenizer.hasMoreTokens()) {
                rule.setCookieDomain(tokenizer.nextToken());
            }
            if (tokenizer.hasMoreTokens()) {
                try {
                    rule.setCookieLifetime(Integer.parseInt(tokenizer.nextToken()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(sm.getString("rewriteValve.invalidFlags", line, flag), e);
                }
            }
            if (tokenizer.hasMoreTokens()) {
                rule.setCookiePath(tokenizer.nextToken());
            }
            if (tokenizer.hasMoreTokens()) {
                rule.setCookieSecure(Boolean.parseBoolean(tokenizer.nextToken()));
            }
            if (tokenizer.hasMoreTokens()) {
                rule.setCookieHttpOnly(Boolean.parseBoolean(tokenizer.nextToken()));
            }
        } else if (flag.startsWith("env=") || flag.startsWith("E=")) {
            rule.setEnv(true);
            if (flag.startsWith("env=")) {
                flag = flag.substring("env=".length());
            } else if (flag.startsWith("E=")) {
                flag = flag.substring("E=".length());
            }
            int pos = flag.indexOf(58);
            if (pos == -1 || pos + 1 == flag.length()) {
                throw new IllegalArgumentException(sm.getString("rewriteValve.invalidFlags", line, flag));
            }
            rule.addEnvName(flag.substring(0, pos));
            rule.addEnvValue(flag.substring(pos + 1));
        } else if (flag.startsWith("forbidden") || flag.startsWith("F")) {
            rule.setForbidden(true);
        } else if (flag.startsWith("gone") || flag.startsWith("G")) {
            rule.setGone(true);
        } else if (flag.startsWith("host") || flag.startsWith("H")) {
            rule.setHost(true);
        } else if (flag.startsWith("last") || flag.startsWith("L")) {
            rule.setLast(true);
        } else if (flag.startsWith("nocase") || flag.startsWith("NC")) {
            rule.setNocase(true);
        } else if (flag.startsWith("noescape") || flag.startsWith("NE")) {
            rule.setNoescape(true);
        } else if (flag.startsWith("next") || flag.startsWith("N")) {
            rule.setNext(true);
        } else if (flag.startsWith("qsappend") || flag.startsWith("QSA")) {
            rule.setQsappend(true);
        } else if (flag.startsWith("qsdiscard") || flag.startsWith("QSD")) {
            rule.setQsdiscard(true);
        } else if (!flag.startsWith("redirect") && !flag.startsWith("R")) {
            if (flag.startsWith("skip") || flag.startsWith("S")) {
                if (flag.startsWith("skip=")) {
                    flag = flag.substring("skip=".length());
                } else if (flag.startsWith("S=")) {
                    flag = flag.substring("S=".length());
                }
                rule.setSkip(Integer.parseInt(flag));
            } else if (flag.startsWith("type") || flag.startsWith("T")) {
                if (flag.startsWith("type=")) {
                    flag = flag.substring("type=".length());
                } else if (flag.startsWith("T=")) {
                    flag = flag.substring("T=".length());
                }
                rule.setType(true);
                rule.setTypeValue(flag);
            } else {
                throw new IllegalArgumentException(sm.getString("rewriteValve.invalidFlags", line, flag));
            }
        } else {
            rule.setRedirect(true);
            int redirectCode = 302;
            if (flag.startsWith("redirect=") || flag.startsWith("R=")) {
                if (flag.startsWith("redirect=")) {
                    flag = flag.substring("redirect=".length());
                } else if (flag.startsWith("R=")) {
                    flag = flag.substring("R=".length());
                }
                String str = flag;
                boolean z = true;
                switch (str.hashCode()) {
                    case 3556308:
                        if (str.equals("temp")) {
                            z = false;
                            break;
                        }
                        break;
                    case 668488878:
                        if (str.equals("permanent")) {
                            z = true;
                            break;
                        }
                        break;
                    case 1000898205:
                        if (str.equals("seeother")) {
                            z = true;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        redirectCode = 302;
                        break;
                    case true:
                        redirectCode = 301;
                        break;
                    case true:
                        redirectCode = 303;
                        break;
                    default:
                        redirectCode = Integer.parseInt(flag);
                        break;
                }
            }
            rule.setRedirectCode(redirectCode);
        }
    }
}
