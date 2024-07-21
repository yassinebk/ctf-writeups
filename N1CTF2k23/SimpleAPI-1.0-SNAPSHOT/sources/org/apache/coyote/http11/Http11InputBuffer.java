package org.apache.coyote.http11;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.coyote.CloseNowException;
import org.apache.coyote.InputBuffer;
import org.apache.coyote.Request;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.HeaderUtil;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.apache.tomcat.util.net.ApplicationBufferHandler;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http11/Http11InputBuffer.class */
public class Http11InputBuffer implements InputBuffer, ApplicationBufferHandler {
    private static final Log log = LogFactory.getLog(Http11InputBuffer.class);
    private static final StringManager sm = StringManager.getManager(Http11InputBuffer.class);
    private static final byte[] CLIENT_PREFACE_START = "PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n".getBytes(StandardCharsets.ISO_8859_1);
    private final Request request;
    private final MimeHeaders headers;
    private final boolean rejectIllegalHeader;
    private ByteBuffer byteBuffer;
    private int end;
    private SocketWrapperBase<?> wrapper;
    private int parsingRequestLinePhase;
    private boolean parsingRequestLineEol;
    private int parsingRequestLineStart;
    private int parsingRequestLineQPos;
    private final HttpParser httpParser;
    private final int headerBufferSize;
    private int socketReadBufferSize;
    private byte prevChr = 0;
    private byte chr = 0;
    private final HeaderParseData headerData = new HeaderParseData();
    private InputFilter[] filterLibrary = new InputFilter[0];
    private InputFilter[] activeFilters = new InputFilter[0];
    private int lastActiveFilter = -1;
    private boolean parsingHeader = true;
    private boolean parsingRequestLine = true;
    private HeaderParsePosition headerParsePos = HeaderParsePosition.HEADER_START;
    private boolean swallowInput = true;
    private InputBuffer inputStreamInputBuffer = new SocketInputBuffer();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http11/Http11InputBuffer$HeaderParsePosition.class */
    public enum HeaderParsePosition {
        HEADER_START,
        HEADER_NAME,
        HEADER_VALUE_START,
        HEADER_VALUE,
        HEADER_MULTI_LINE,
        HEADER_SKIPLINE
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http11/Http11InputBuffer$HeaderParseStatus.class */
    public enum HeaderParseStatus {
        DONE,
        HAVE_MORE_HEADERS,
        NEED_MORE_DATA
    }

    public Http11InputBuffer(Request request, int headerBufferSize, boolean rejectIllegalHeader, HttpParser httpParser) {
        this.parsingRequestLinePhase = 0;
        this.parsingRequestLineEol = false;
        this.parsingRequestLineStart = 0;
        this.parsingRequestLineQPos = -1;
        this.request = request;
        this.headers = request.getMimeHeaders();
        this.headerBufferSize = headerBufferSize;
        this.rejectIllegalHeader = rejectIllegalHeader;
        this.httpParser = httpParser;
        this.parsingRequestLinePhase = 0;
        this.parsingRequestLineEol = false;
        this.parsingRequestLineStart = 0;
        this.parsingRequestLineQPos = -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addFilter(InputFilter filter) {
        if (filter == null) {
            throw new NullPointerException(sm.getString("iib.filter.npe"));
        }
        InputFilter[] newFilterLibrary = (InputFilter[]) Arrays.copyOf(this.filterLibrary, this.filterLibrary.length + 1);
        newFilterLibrary[this.filterLibrary.length] = filter;
        this.filterLibrary = newFilterLibrary;
        this.activeFilters = new InputFilter[this.filterLibrary.length];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputFilter[] getFilters() {
        return this.filterLibrary;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addActiveFilter(InputFilter filter) {
        if (this.lastActiveFilter == -1) {
            filter.setBuffer(this.inputStreamInputBuffer);
        } else {
            for (int i = 0; i <= this.lastActiveFilter; i++) {
                if (this.activeFilters[i] == filter) {
                    return;
                }
            }
            filter.setBuffer(this.activeFilters[this.lastActiveFilter]);
        }
        InputFilter[] inputFilterArr = this.activeFilters;
        int i2 = this.lastActiveFilter + 1;
        this.lastActiveFilter = i2;
        inputFilterArr[i2] = filter;
        filter.setRequest(this.request);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSwallowInput(boolean swallowInput) {
        this.swallowInput = swallowInput;
    }

    @Override // org.apache.coyote.InputBuffer
    public int doRead(ApplicationBufferHandler handler) throws IOException {
        if (this.lastActiveFilter == -1) {
            return this.inputStreamInputBuffer.doRead(handler);
        }
        return this.activeFilters[this.lastActiveFilter].doRead(handler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recycle() {
        this.wrapper = null;
        this.request.recycle();
        for (int i = 0; i <= this.lastActiveFilter; i++) {
            this.activeFilters[i].recycle();
        }
        this.byteBuffer.limit(0).position(0);
        this.lastActiveFilter = -1;
        this.parsingHeader = true;
        this.swallowInput = true;
        this.chr = (byte) 0;
        this.prevChr = (byte) 0;
        this.headerParsePos = HeaderParsePosition.HEADER_START;
        this.parsingRequestLine = true;
        this.parsingRequestLinePhase = 0;
        this.parsingRequestLineEol = false;
        this.parsingRequestLineStart = 0;
        this.parsingRequestLineQPos = -1;
        this.headerData.recycle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void nextRequest() {
        this.request.recycle();
        if (this.byteBuffer.position() > 0) {
            if (this.byteBuffer.remaining() > 0) {
                this.byteBuffer.compact();
                this.byteBuffer.flip();
            } else {
                this.byteBuffer.position(0).limit(0);
            }
        }
        for (int i = 0; i <= this.lastActiveFilter; i++) {
            this.activeFilters[i].recycle();
        }
        this.lastActiveFilter = -1;
        this.parsingHeader = true;
        this.swallowInput = true;
        this.headerParsePos = HeaderParsePosition.HEADER_START;
        this.parsingRequestLine = true;
        this.parsingRequestLinePhase = 0;
        this.parsingRequestLineEol = false;
        this.parsingRequestLineStart = 0;
        this.parsingRequestLineQPos = -1;
        this.headerData.recycle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean parseRequestLine(boolean keptAlive, int connectionTimeout, int keepAliveTimeout) throws IOException {
        if (!this.parsingRequestLine) {
            return true;
        }
        if (this.parsingRequestLinePhase < 2) {
            while (true) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit()) {
                    if (keptAlive) {
                        this.wrapper.setReadTimeout(keepAliveTimeout);
                    }
                    if (!fill(false)) {
                        this.parsingRequestLinePhase = 1;
                        return false;
                    }
                    this.wrapper.setReadTimeout(connectionTimeout);
                }
                if (!keptAlive && this.byteBuffer.position() == 0 && this.byteBuffer.limit() >= CLIENT_PREFACE_START.length - 1) {
                    boolean prefaceMatch = true;
                    for (int i = 0; i < CLIENT_PREFACE_START.length && prefaceMatch; i++) {
                        if (CLIENT_PREFACE_START[i] != this.byteBuffer.get(i)) {
                            prefaceMatch = false;
                        }
                    }
                    if (prefaceMatch) {
                        this.parsingRequestLinePhase = -1;
                        return false;
                    }
                }
                if (this.request.getStartTime() < 0) {
                    this.request.setStartTime(System.currentTimeMillis());
                }
                this.chr = this.byteBuffer.get();
                if (this.chr != 13 && this.chr != 10) {
                    this.byteBuffer.position(this.byteBuffer.position() - 1);
                    this.parsingRequestLineStart = this.byteBuffer.position();
                    this.parsingRequestLinePhase = 2;
                    if (log.isDebugEnabled()) {
                        log.debug("Received [" + new String(this.byteBuffer.array(), this.byteBuffer.position(), this.byteBuffer.remaining(), StandardCharsets.ISO_8859_1) + "]");
                    }
                }
            }
        }
        if (this.parsingRequestLinePhase == 2) {
            boolean space = false;
            while (!space) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                    return false;
                }
                int pos = this.byteBuffer.position();
                this.chr = this.byteBuffer.get();
                if (this.chr == 32 || this.chr == 9) {
                    space = true;
                    this.request.method().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, pos - this.parsingRequestLineStart);
                } else if (!HttpParser.isToken(this.chr)) {
                    this.request.protocol().setString(Constants.HTTP_11);
                    String invalidMethodValue = parseInvalid(this.parsingRequestLineStart, this.byteBuffer);
                    throw new IllegalArgumentException(sm.getString("iib.invalidmethod", invalidMethodValue));
                }
            }
            this.parsingRequestLinePhase = 3;
        }
        if (this.parsingRequestLinePhase == 3) {
            boolean space2 = true;
            while (space2) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                    return false;
                }
                this.chr = this.byteBuffer.get();
                if (this.chr != 32 && this.chr != 9) {
                    space2 = false;
                    this.byteBuffer.position(this.byteBuffer.position() - 1);
                }
            }
            this.parsingRequestLineStart = this.byteBuffer.position();
            this.parsingRequestLinePhase = 4;
        }
        if (this.parsingRequestLinePhase == 4) {
            int end = 0;
            boolean space3 = false;
            while (!space3) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                    return false;
                }
                int pos2 = this.byteBuffer.position();
                this.prevChr = this.chr;
                this.chr = this.byteBuffer.get();
                if (this.prevChr == 13 && this.chr != 10) {
                    this.request.protocol().setString(Constants.HTTP_11);
                    String invalidRequestTarget = parseInvalid(this.parsingRequestLineStart, this.byteBuffer);
                    throw new IllegalArgumentException(sm.getString("iib.invalidRequestTarget", invalidRequestTarget));
                } else if (this.chr == 32 || this.chr == 9) {
                    space3 = true;
                    end = pos2;
                } else if (this.chr == 13) {
                    continue;
                } else if (this.chr == 10) {
                    space3 = true;
                    this.request.protocol().setString("");
                    this.parsingRequestLinePhase = 7;
                    if (this.prevChr == 13) {
                        end = pos2 - 1;
                    } else {
                        end = pos2;
                    }
                } else if (this.chr == 63 && this.parsingRequestLineQPos == -1) {
                    this.parsingRequestLineQPos = pos2;
                } else if (this.parsingRequestLineQPos != -1 && !this.httpParser.isQueryRelaxed(this.chr)) {
                    this.request.protocol().setString(Constants.HTTP_11);
                    String invalidRequestTarget2 = parseInvalid(this.parsingRequestLineStart, this.byteBuffer);
                    throw new IllegalArgumentException(sm.getString("iib.invalidRequestTarget", invalidRequestTarget2));
                } else if (this.httpParser.isNotRequestTargetRelaxed(this.chr)) {
                    this.request.protocol().setString(Constants.HTTP_11);
                    String invalidRequestTarget3 = parseInvalid(this.parsingRequestLineStart, this.byteBuffer);
                    throw new IllegalArgumentException(sm.getString("iib.invalidRequestTarget", invalidRequestTarget3));
                }
            }
            if (this.parsingRequestLineQPos >= 0) {
                this.request.queryString().setBytes(this.byteBuffer.array(), this.parsingRequestLineQPos + 1, (end - this.parsingRequestLineQPos) - 1);
                this.request.requestURI().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, this.parsingRequestLineQPos - this.parsingRequestLineStart);
            } else {
                this.request.requestURI().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, end - this.parsingRequestLineStart);
            }
            if (this.parsingRequestLinePhase == 4) {
                this.parsingRequestLinePhase = 5;
            }
        }
        if (this.parsingRequestLinePhase == 5) {
            boolean space4 = true;
            while (space4) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                    return false;
                }
                byte chr = this.byteBuffer.get();
                if (chr != 32 && chr != 9) {
                    space4 = false;
                    this.byteBuffer.position(this.byteBuffer.position() - 1);
                }
            }
            this.parsingRequestLineStart = this.byteBuffer.position();
            this.parsingRequestLinePhase = 6;
            this.end = 0;
        }
        if (this.parsingRequestLinePhase == 6) {
            while (!this.parsingRequestLineEol) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                    return false;
                }
                int pos3 = this.byteBuffer.position();
                this.prevChr = this.chr;
                this.chr = this.byteBuffer.get();
                if (this.chr != 13) {
                    if (this.prevChr == 13 && this.chr == 10) {
                        this.end = pos3 - 1;
                        this.parsingRequestLineEol = true;
                    } else if (!HttpParser.isHttpProtocol(this.chr)) {
                        String invalidProtocol = parseInvalid(this.parsingRequestLineStart, this.byteBuffer);
                        throw new IllegalArgumentException(sm.getString("iib.invalidHttpProtocol", invalidProtocol));
                    }
                }
            }
            if (this.end - this.parsingRequestLineStart > 0) {
                this.request.protocol().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, this.end - this.parsingRequestLineStart);
                this.parsingRequestLinePhase = 7;
            }
        }
        if (this.parsingRequestLinePhase == 7) {
            this.parsingRequestLine = false;
            this.parsingRequestLinePhase = 0;
            this.parsingRequestLineEol = false;
            this.parsingRequestLineStart = 0;
            return true;
        }
        throw new IllegalStateException(sm.getString("iib.invalidPhase", Integer.valueOf(this.parsingRequestLinePhase)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean parseHeaders() throws IOException {
        HeaderParseStatus status;
        if (!this.parsingHeader) {
            throw new IllegalStateException(sm.getString("iib.parseheaders.ise.error"));
        }
        HeaderParseStatus headerParseStatus = HeaderParseStatus.HAVE_MORE_HEADERS;
        do {
            status = parseHeader();
            if (this.byteBuffer.position() > this.headerBufferSize || this.byteBuffer.capacity() - this.byteBuffer.position() < this.socketReadBufferSize) {
                throw new IllegalArgumentException(sm.getString("iib.requestheadertoolarge.error"));
            }
        } while (status == HeaderParseStatus.HAVE_MORE_HEADERS);
        if (status == HeaderParseStatus.DONE) {
            this.parsingHeader = false;
            this.end = this.byteBuffer.position();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getParsingRequestLinePhase() {
        return this.parsingRequestLinePhase;
    }

    private String parseInvalid(int startPos, ByteBuffer buffer) {
        byte b;
        byte b2 = 0;
        while (true) {
            b = b2;
            if (!buffer.hasRemaining() || b == 32) {
                break;
            }
            b2 = buffer.get();
        }
        String result = HeaderUtil.toPrintableString(buffer.array(), buffer.arrayOffset() + startPos, (buffer.position() - startPos) - 1);
        if (b != 32) {
            result = result + "...";
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endRequest() throws IOException {
        if (this.swallowInput && this.lastActiveFilter != -1) {
            int extraBytes = (int) this.activeFilters[this.lastActiveFilter].end();
            this.byteBuffer.position(this.byteBuffer.position() - extraBytes);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int available(boolean read) {
        int available = this.byteBuffer.remaining();
        if (available == 0 && this.lastActiveFilter >= 0) {
            for (int i = 0; available == 0 && i <= this.lastActiveFilter; i++) {
                available = this.activeFilters[i].available();
            }
        }
        if (available > 0 || !read) {
            return available;
        }
        try {
            if (this.wrapper.hasDataToRead()) {
                fill(false);
                available = this.byteBuffer.remaining();
            }
        } catch (IOException ioe) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("iib.available.readFail"), ioe);
            }
            available = 1;
        }
        return available;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFinished() {
        if (this.byteBuffer.limit() <= this.byteBuffer.position() && this.lastActiveFilter >= 0) {
            return this.activeFilters[this.lastActiveFilter].isFinished();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteBuffer getLeftover() {
        int available = this.byteBuffer.remaining();
        if (available > 0) {
            return ByteBuffer.wrap(this.byteBuffer.array(), this.byteBuffer.position(), available);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isChunking() {
        for (int i = 0; i < this.lastActiveFilter; i++) {
            if (this.activeFilters[i] == this.filterLibrary[1]) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(SocketWrapperBase<?> socketWrapper) {
        this.wrapper = socketWrapper;
        this.wrapper.setAppReadBufHandler(this);
        int bufLength = this.headerBufferSize + this.wrapper.getSocketBufferHandler().getReadBuffer().capacity();
        if (this.byteBuffer == null || this.byteBuffer.capacity() < bufLength) {
            this.byteBuffer = ByteBuffer.allocate(bufLength);
            this.byteBuffer.position(0).limit(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean fill(boolean block) throws IOException {
        if (this.parsingHeader) {
            if (this.byteBuffer.limit() >= this.headerBufferSize) {
                if (this.parsingRequestLine) {
                    this.request.protocol().setString(Constants.HTTP_11);
                }
                throw new IllegalArgumentException(sm.getString("iib.requestheadertoolarge.error"));
            }
        } else {
            this.byteBuffer.limit(this.end).position(this.end);
        }
        this.byteBuffer.mark();
        if (this.byteBuffer.position() < this.byteBuffer.limit()) {
            this.byteBuffer.position(this.byteBuffer.limit());
        }
        this.byteBuffer.limit(this.byteBuffer.capacity());
        SocketWrapperBase<?> socketWrapper = this.wrapper;
        if (socketWrapper != null) {
            int nRead = socketWrapper.read(block, this.byteBuffer);
            this.byteBuffer.limit(this.byteBuffer.position()).reset();
            if (nRead > 0) {
                return true;
            }
            if (nRead == -1) {
                throw new EOFException(sm.getString("iib.eof.error"));
            }
            return false;
        }
        throw new CloseNowException(sm.getString("iib.eof.error"));
    }

    /* JADX WARN: Code restructure failed: missing block: B:123:0x03cd, code lost:
        r7.headerData.headerValue.setBytes(r7.byteBuffer.array(), r7.headerData.start, r7.headerData.lastSignificantChar - r7.headerData.start);
        r7.headerData.recycle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x03fe, code lost:
        return org.apache.coyote.http11.Http11InputBuffer.HeaderParseStatus.HAVE_MORE_HEADERS;
     */
    /* JADX WARN: Removed duplicated region for block: B:129:0x01aa A[EDGE_INSN: B:129:0x01aa->B:49:0x01aa ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:4:0x000a  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01b4  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01b9 A[LOOP:2: B:53:0x01b9->B:148:0x01b9, LOOP_START] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private org.apache.coyote.http11.Http11InputBuffer.HeaderParseStatus parseHeader() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1023
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.coyote.http11.Http11InputBuffer.parseHeader():org.apache.coyote.http11.Http11InputBuffer$HeaderParseStatus");
    }

    private HeaderParseStatus skipLine() throws IOException {
        this.headerParsePos = HeaderParsePosition.HEADER_SKIPLINE;
        boolean eol = false;
        while (!eol) {
            if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                return HeaderParseStatus.NEED_MORE_DATA;
            }
            int pos = this.byteBuffer.position();
            this.prevChr = this.chr;
            this.chr = this.byteBuffer.get();
            if (this.chr != 13) {
                if (this.prevChr == 13 && this.chr == 10) {
                    eol = true;
                } else {
                    this.headerData.lastSignificantChar = pos;
                }
            }
        }
        if (this.rejectIllegalHeader || log.isDebugEnabled()) {
            String message = sm.getString("iib.invalidheader", HeaderUtil.toPrintableString(this.byteBuffer.array(), this.headerData.lineStart, (this.headerData.lastSignificantChar - this.headerData.lineStart) + 1));
            if (this.rejectIllegalHeader) {
                throw new IllegalArgumentException(message);
            }
            log.debug(message);
        }
        this.headerParsePos = HeaderParsePosition.HEADER_START;
        return HeaderParseStatus.HAVE_MORE_HEADERS;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http11/Http11InputBuffer$HeaderParseData.class */
    public static class HeaderParseData {
        int lineStart;
        int start;
        int realPos;
        int lastSignificantChar;
        MessageBytes headerValue;

        private HeaderParseData() {
            this.lineStart = 0;
            this.start = 0;
            this.realPos = 0;
            this.lastSignificantChar = 0;
            this.headerValue = null;
        }

        public void recycle() {
            this.lineStart = 0;
            this.start = 0;
            this.realPos = 0;
            this.lastSignificantChar = 0;
            this.headerValue = null;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http11/Http11InputBuffer$SocketInputBuffer.class */
    private class SocketInputBuffer implements InputBuffer {
        private SocketInputBuffer() {
        }

        @Override // org.apache.coyote.InputBuffer
        public int doRead(ApplicationBufferHandler handler) throws IOException {
            if (Http11InputBuffer.this.byteBuffer.position() < Http11InputBuffer.this.byteBuffer.limit() || Http11InputBuffer.this.fill(true)) {
                int length = Http11InputBuffer.this.byteBuffer.remaining();
                handler.setByteBuffer(Http11InputBuffer.this.byteBuffer.duplicate());
                Http11InputBuffer.this.byteBuffer.position(Http11InputBuffer.this.byteBuffer.limit());
                return length;
            }
            return -1;
        }
    }

    @Override // org.apache.tomcat.util.net.ApplicationBufferHandler
    public void setByteBuffer(ByteBuffer buffer) {
        this.byteBuffer = buffer;
    }

    @Override // org.apache.tomcat.util.net.ApplicationBufferHandler
    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    @Override // org.apache.tomcat.util.net.ApplicationBufferHandler
    public void expand(int size) {
        if (this.byteBuffer.capacity() >= size) {
            this.byteBuffer.limit(size);
        }
        ByteBuffer temp = ByteBuffer.allocate(size);
        temp.put(this.byteBuffer);
        this.byteBuffer = temp;
        this.byteBuffer.mark();
    }
}
