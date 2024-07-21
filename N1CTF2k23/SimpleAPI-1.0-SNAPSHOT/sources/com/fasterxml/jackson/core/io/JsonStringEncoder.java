package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import java.util.Arrays;
import org.springframework.asm.Opcodes;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-core-2.11.0.jar:com/fasterxml/jackson/core/io/JsonStringEncoder.class */
public final class JsonStringEncoder {
    private static final int SURR1_FIRST = 55296;
    private static final int SURR1_LAST = 56319;
    private static final int SURR2_FIRST = 56320;
    private static final int SURR2_LAST = 57343;
    private static final int INITIAL_CHAR_BUFFER_SIZE = 120;
    private static final int INITIAL_BYTE_BUFFER_SIZE = 200;
    private static final char[] HC = CharTypes.copyHexChars();
    private static final byte[] HB = CharTypes.copyHexBytes();
    private static final JsonStringEncoder instance = new JsonStringEncoder();

    public static JsonStringEncoder getInstance() {
        return instance;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0075, code lost:
        if (r15 != null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0078, code lost:
        r15 = _qbuf();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x007e, code lost:
        r1 = r11;
        r11 = r11 + 1;
        r0 = r7.charAt(r1);
        r0 = r0[r0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0091, code lost:
        if (r0 >= 0) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0094, code lost:
        r0 = _appendNumeric(r0, r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x009f, code lost:
        r0 = _appendNamed(r0, r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00a7, code lost:
        r18 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00b0, code lost:
        if ((r14 + r18) <= r8.length) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00b3, code lost:
        r0 = r8.length - r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00bc, code lost:
        if (r0 <= 0) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00bf, code lost:
        java.lang.System.arraycopy(r15, 0, r8, r14, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00cc, code lost:
        if (r13 != null) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00cf, code lost:
        r13 = com.fasterxml.jackson.core.util.TextBuffer.fromInitial(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d5, code lost:
        r8 = r13.finishCurrentSegment();
        r0 = r18 - r0;
        java.lang.System.arraycopy(r15, r0, r8, 0, r0);
        r14 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00f4, code lost:
        java.lang.System.arraycopy(r15, 0, r8, r14, r18);
        r14 = r14 + r18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public char[] quoteAsString(java.lang.String r7) {
        /*
            Method dump skipped, instructions count: 291
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.io.JsonStringEncoder.quoteAsString(java.lang.String):char[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0088, code lost:
        if (r15 != null) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x008b, code lost:
        r15 = _qbuf();
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0091, code lost:
        r1 = r12;
        r12 = r12 + 1;
        r0 = r7.charAt(r1);
        r0 = r0[r0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00a7, code lost:
        if (r0 >= 0) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00aa, code lost:
        r0 = _appendNumeric(r0, r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00b5, code lost:
        r0 = _appendNamed(r0, r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00bd, code lost:
        r18 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00c6, code lost:
        if ((r14 + r18) <= r9.length) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00c9, code lost:
        r0 = r9.length - r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00d2, code lost:
        if (r0 <= 0) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d5, code lost:
        java.lang.System.arraycopy(r15, 0, r9, r14, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00e1, code lost:
        if (r8 != null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00e4, code lost:
        r8 = com.fasterxml.jackson.core.util.TextBuffer.fromInitial(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00e9, code lost:
        r9 = r8.finishCurrentSegment();
        r0 = r18 - r0;
        java.lang.System.arraycopy(r15, r0, r9, 0, r0);
        r14 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0107, code lost:
        java.lang.System.arraycopy(r15, 0, r9, r14, r18);
        r14 = r14 + r18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public char[] quoteAsString(java.lang.CharSequence r7) {
        /*
            Method dump skipped, instructions count: 307
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.io.JsonStringEncoder.quoteAsString(java.lang.CharSequence):char[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0051, code lost:
        if (r12 != null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0054, code lost:
        r12 = _qbuf();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x005a, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r0 = r6.charAt(r1);
        r0 = r0[r0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x006f, code lost:
        if (r0 >= 0) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0072, code lost:
        r0 = _appendNumeric(r0, r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x007d, code lost:
        r0 = _appendNamed(r0, r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0085, code lost:
        r15 = r0;
        r7.append(r12, 0, r15);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void quoteAsString(java.lang.CharSequence r6, java.lang.StringBuilder r7) {
        /*
            r5 = this;
            int[] r0 = com.fasterxml.jackson.core.io.CharTypes.get7BitOutputEscapes()
            r8 = r0
            r0 = r8
            int r0 = r0.length
            r9 = r0
            r0 = 0
            r10 = r0
            r0 = r6
            int r0 = r0.length()
            r11 = r0
            r0 = 0
            r12 = r0
        L16:
            r0 = r10
            r1 = r11
            if (r0 >= r1) goto L94
        L1d:
            r0 = r6
            r1 = r10
            char r0 = r0.charAt(r1)
            r13 = r0
            r0 = r13
            r1 = r9
            if (r0 >= r1) goto L38
            r0 = r8
            r1 = r13
            r0 = r0[r1]
            if (r0 == 0) goto L38
            goto L4f
        L38:
            r0 = r7
            r1 = r13
            java.lang.StringBuilder r0 = r0.append(r1)
            int r10 = r10 + 1
            r0 = r10
            r1 = r11
            if (r0 < r1) goto L4c
            goto L94
        L4c:
            goto L1d
        L4f:
            r0 = r12
            if (r0 != 0) goto L5a
            r0 = r5
            char[] r0 = r0._qbuf()
            r12 = r0
        L5a:
            r0 = r6
            r1 = r10
            int r10 = r10 + 1
            char r0 = r0.charAt(r1)
            r13 = r0
            r0 = r8
            r1 = r13
            r0 = r0[r1]
            r14 = r0
            r0 = r14
            if (r0 >= 0) goto L7d
            r0 = r5
            r1 = r13
            r2 = r12
            int r0 = r0._appendNumeric(r1, r2)
            goto L85
        L7d:
            r0 = r5
            r1 = r14
            r2 = r12
            int r0 = r0._appendNamed(r1, r2)
        L85:
            r15 = r0
            r0 = r7
            r1 = r12
            r2 = 0
            r3 = r15
            java.lang.StringBuilder r0 = r0.append(r1, r2, r3)
            goto L16
        L94:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.io.JsonStringEncoder.quoteAsString(java.lang.CharSequence, java.lang.StringBuilder):void");
    }

    public byte[] quoteAsUTF8(String text) {
        int outputPtr;
        int ch2;
        int inputPtr = 0;
        int inputEnd = text.length();
        int outputPtr2 = 0;
        byte[] outputBuffer = new byte[200];
        ByteArrayBuilder bb = null;
        loop0: while (inputPtr < inputEnd) {
            int[] escCodes = CharTypes.get7BitOutputEscapes();
            while (true) {
                int ch3 = text.charAt(inputPtr);
                if (ch3 <= 127 && escCodes[ch3] == 0) {
                    if (outputPtr2 >= outputBuffer.length) {
                        if (bb == null) {
                            bb = ByteArrayBuilder.fromInitial(outputBuffer, outputPtr2);
                        }
                        outputBuffer = bb.finishCurrentSegment();
                        outputPtr2 = 0;
                    }
                    int i = outputPtr2;
                    outputPtr2++;
                    outputBuffer[i] = (byte) ch3;
                    inputPtr++;
                    if (inputPtr >= inputEnd) {
                        break loop0;
                    }
                } else {
                    break;
                }
            }
            if (bb == null) {
                bb = ByteArrayBuilder.fromInitial(outputBuffer, outputPtr2);
            }
            if (outputPtr2 >= outputBuffer.length) {
                outputBuffer = bb.finishCurrentSegment();
                outputPtr2 = 0;
            }
            int i2 = inputPtr;
            inputPtr++;
            int ch4 = text.charAt(i2);
            if (ch4 <= 127) {
                int escape = escCodes[ch4];
                outputPtr2 = _appendByte(ch4, escape, bb, outputPtr2);
                outputBuffer = bb.getCurrentSegment();
            } else {
                if (ch4 <= 2047) {
                    int i3 = outputPtr2;
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[i3] = (byte) (192 | (ch4 >> 6));
                    ch2 = 128 | (ch4 & 63);
                } else if (ch4 < 55296 || ch4 > 57343) {
                    int i4 = outputPtr2;
                    int outputPtr3 = outputPtr2 + 1;
                    outputBuffer[i4] = (byte) (224 | (ch4 >> 12));
                    if (outputPtr3 >= outputBuffer.length) {
                        outputBuffer = bb.finishCurrentSegment();
                        outputPtr3 = 0;
                    }
                    int i5 = outputPtr3;
                    outputPtr = outputPtr3 + 1;
                    outputBuffer[i5] = (byte) (128 | ((ch4 >> 6) & 63));
                    ch2 = 128 | (ch4 & 63);
                } else {
                    if (ch4 > 56319) {
                        _illegal(ch4);
                    }
                    if (inputPtr >= inputEnd) {
                        _illegal(ch4);
                    }
                    inputPtr++;
                    int ch5 = _convert(ch4, text.charAt(inputPtr));
                    if (ch5 > 1114111) {
                        _illegal(ch5);
                    }
                    int i6 = outputPtr2;
                    int outputPtr4 = outputPtr2 + 1;
                    outputBuffer[i6] = (byte) (240 | (ch5 >> 18));
                    if (outputPtr4 >= outputBuffer.length) {
                        outputBuffer = bb.finishCurrentSegment();
                        outputPtr4 = 0;
                    }
                    int i7 = outputPtr4;
                    int outputPtr5 = outputPtr4 + 1;
                    outputBuffer[i7] = (byte) (128 | ((ch5 >> 12) & 63));
                    if (outputPtr5 >= outputBuffer.length) {
                        outputBuffer = bb.finishCurrentSegment();
                        outputPtr5 = 0;
                    }
                    int i8 = outputPtr5;
                    outputPtr = outputPtr5 + 1;
                    outputBuffer[i8] = (byte) (128 | ((ch5 >> 6) & 63));
                    ch2 = 128 | (ch5 & 63);
                }
                if (outputPtr >= outputBuffer.length) {
                    outputBuffer = bb.finishCurrentSegment();
                    outputPtr = 0;
                }
                int i9 = outputPtr;
                outputPtr2 = outputPtr + 1;
                outputBuffer[i9] = (byte) ch2;
            }
        }
        if (bb == null) {
            return Arrays.copyOfRange(outputBuffer, 0, outputPtr2);
        }
        return bb.completeAndCoalesce(outputPtr2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0075, code lost:
        if (r13 != null) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0078, code lost:
        r13 = com.fasterxml.jackson.core.util.ByteArrayBuilder.fromInitial(r11, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0085, code lost:
        if (r10 < r12) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0088, code lost:
        r11 = r13.finishCurrentSegment();
        r12 = r11.length;
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x009c, code lost:
        if (r14 >= 2048) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x009f, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (192 | (r14 >> 6));
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00b8, code lost:
        if (r14 < 55296) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00bf, code lost:
        if (r14 <= 57343) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00c2, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (224 | (r14 >> 12));
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00d8, code lost:
        if (r10 < r12) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00db, code lost:
        r11 = r13.finishCurrentSegment();
        r12 = r11.length;
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00ea, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (128 | ((r14 >> 6) & 63));
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0106, code lost:
        if (r14 <= 56319) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0109, code lost:
        _illegal(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0110, code lost:
        if (r8 < r0) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0113, code lost:
        _illegal(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0118, code lost:
        r2 = r8;
        r8 = r8 + 1;
        r14 = _convert(r14, r7.charAt(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x012b, code lost:
        if (r14 <= 1114111) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x012e, code lost:
        _illegal(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0133, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (240 | (r14 >> 18));
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0149, code lost:
        if (r10 < r12) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x014c, code lost:
        r11 = r13.finishCurrentSegment();
        r12 = r11.length;
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x015b, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (128 | ((r14 >> 12) & 63));
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0174, code lost:
        if (r10 < r12) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0177, code lost:
        r11 = r13.finishCurrentSegment();
        r12 = r11.length;
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0186, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (128 | ((r14 >> 6) & 63));
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x019f, code lost:
        if (r10 < r12) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x01a2, code lost:
        r11 = r13.finishCurrentSegment();
        r12 = r11.length;
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01b1, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (128 | (r14 & 63));
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public byte[] encodeAsUTF8(java.lang.String r7) {
        /*
            Method dump skipped, instructions count: 476
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.io.JsonStringEncoder.encodeAsUTF8(java.lang.String):byte[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x007b, code lost:
        if (r13 != null) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x007e, code lost:
        r13 = com.fasterxml.jackson.core.util.ByteArrayBuilder.fromInitial(r11, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x008b, code lost:
        if (r10 < r12) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x008e, code lost:
        r11 = r13.finishCurrentSegment();
        r12 = r11.length;
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00a2, code lost:
        if (r14 >= 2048) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00a5, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (192 | (r14 >> 6));
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00be, code lost:
        if (r14 < 55296) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00c5, code lost:
        if (r14 <= 57343) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00c8, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (224 | (r14 >> 12));
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00de, code lost:
        if (r10 < r12) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00e1, code lost:
        r11 = r13.finishCurrentSegment();
        r12 = r11.length;
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00f0, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (128 | ((r14 >> 6) & 63));
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x010c, code lost:
        if (r14 <= 56319) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x010f, code lost:
        _illegal(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0116, code lost:
        if (r8 < r0) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0119, code lost:
        _illegal(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x011e, code lost:
        r2 = r8;
        r8 = r8 + 1;
        r14 = _convert(r14, r7.charAt(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0133, code lost:
        if (r14 <= 1114111) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0136, code lost:
        _illegal(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x013b, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (240 | (r14 >> 18));
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0151, code lost:
        if (r10 < r12) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0154, code lost:
        r11 = r13.finishCurrentSegment();
        r12 = r11.length;
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0163, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (128 | ((r14 >> 12) & 63));
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x017c, code lost:
        if (r10 < r12) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x017f, code lost:
        r11 = r13.finishCurrentSegment();
        r12 = r11.length;
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x018e, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (128 | ((r14 >> 6) & 63));
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x01a7, code lost:
        if (r10 < r12) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x01aa, code lost:
        r11 = r13.finishCurrentSegment();
        r12 = r11.length;
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01b9, code lost:
        r1 = r10;
        r10 = r10 + 1;
        r11[r1] = (byte) (128 | (r14 & 63));
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public byte[] encodeAsUTF8(java.lang.CharSequence r7) {
        /*
            Method dump skipped, instructions count: 484
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.io.JsonStringEncoder.encodeAsUTF8(java.lang.CharSequence):byte[]");
    }

    private char[] _qbuf() {
        char[] qbuf = {'\\', 0, '0', '0'};
        return qbuf;
    }

    private int _appendNumeric(int value, char[] qbuf) {
        qbuf[1] = 'u';
        qbuf[4] = HC[value >> 4];
        qbuf[5] = HC[value & 15];
        return 6;
    }

    private int _appendNamed(int esc, char[] qbuf) {
        qbuf[1] = (char) esc;
        return 2;
    }

    private int _appendByte(int ch2, int esc, ByteArrayBuilder bb, int ptr) {
        bb.setCurrentSegmentLength(ptr);
        bb.append(92);
        if (esc < 0) {
            bb.append(Opcodes.LNEG);
            if (ch2 > 255) {
                int hi = ch2 >> 8;
                bb.append(HB[hi >> 4]);
                bb.append(HB[hi & 15]);
                ch2 &= 255;
            } else {
                bb.append(48);
                bb.append(48);
            }
            bb.append(HB[ch2 >> 4]);
            bb.append(HB[ch2 & 15]);
        } else {
            bb.append((byte) esc);
        }
        return bb.getCurrentSegmentLength();
    }

    private static int _convert(int p1, int p2) {
        if (p2 < 56320 || p2 > 57343) {
            throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(p1) + ", second 0x" + Integer.toHexString(p2) + "; illegal combination");
        }
        return 65536 + ((p1 - 55296) << 10) + (p2 - 56320);
    }

    private static void _illegal(int c) {
        throw new IllegalArgumentException(UTF8Writer.illegalSurrogateDesc(c));
    }
}
