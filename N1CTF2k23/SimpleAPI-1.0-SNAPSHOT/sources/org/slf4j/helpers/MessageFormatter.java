package org.slf4j.helpers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/slf4j-api-1.7.25.jar:org/slf4j/helpers/MessageFormatter.class */
public final class MessageFormatter {
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    public static final FormattingTuple format(String messagePattern, Object arg) {
        return arrayFormat(messagePattern, new Object[]{arg});
    }

    public static final FormattingTuple format(String messagePattern, Object arg1, Object arg2) {
        return arrayFormat(messagePattern, new Object[]{arg1, arg2});
    }

    static final Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable) lastEntry;
        }
        return null;
    }

    public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
        Throwable throwableCandidate = getThrowableCandidate(argArray);
        Object[] args = argArray;
        if (throwableCandidate != null) {
            args = trimmedCopy(argArray);
        }
        return arrayFormat(messagePattern, args, throwableCandidate);
    }

    private static Object[] trimmedCopy(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
        int trimemdLen = argArray.length - 1;
        Object[] trimmed = new Object[trimemdLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
        return trimmed;
    }

    public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray, Throwable throwable) {
        int i;
        int i2;
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwable);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }
        int i3 = 0;
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        int L = 0;
        while (L < argArray.length) {
            int j = messagePattern.indexOf(DELIM_STR, i3);
            if (j == -1) {
                if (i3 == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwable);
                }
                sbuf.append((CharSequence) messagePattern, i3, messagePattern.length());
                return new FormattingTuple(sbuf.toString(), argArray, throwable);
            }
            if (isEscapedDelimeter(messagePattern, j)) {
                if (!isDoubleEscaped(messagePattern, j)) {
                    L--;
                    sbuf.append((CharSequence) messagePattern, i3, j - 1);
                    sbuf.append('{');
                    i = j;
                    i2 = 1;
                } else {
                    sbuf.append((CharSequence) messagePattern, i3, j - 1);
                    deeplyAppendParameter(sbuf, argArray[L], new HashMap());
                    i = j;
                    i2 = 2;
                }
            } else {
                sbuf.append((CharSequence) messagePattern, i3, j);
                deeplyAppendParameter(sbuf, argArray[L], new HashMap());
                i = j;
                i2 = 2;
            }
            i3 = i + i2;
            L++;
        }
        sbuf.append((CharSequence) messagePattern, i3, messagePattern.length());
        return new FormattingTuple(sbuf.toString(), argArray, throwable);
    }

    static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        if (potentialEscape == '\\') {
            return true;
        }
        return false;
    }

    static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\') {
            return true;
        }
        return false;
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append(BeanDefinitionParserDelegate.NULL_ELEMENT);
        } else if (!o.getClass().isArray()) {
            safeObjectAppend(sbuf, o);
        } else if (o instanceof boolean[]) {
            booleanArrayAppend(sbuf, (boolean[]) o);
        } else if (o instanceof byte[]) {
            byteArrayAppend(sbuf, (byte[]) o);
        } else if (o instanceof char[]) {
            charArrayAppend(sbuf, (char[]) o);
        } else if (o instanceof short[]) {
            shortArrayAppend(sbuf, (short[]) o);
        } else if (o instanceof int[]) {
            intArrayAppend(sbuf, (int[]) o);
        } else if (o instanceof long[]) {
            longArrayAppend(sbuf, (long[]) o);
        } else if (o instanceof float[]) {
            floatArrayAppend(sbuf, (float[]) o);
        } else if (o instanceof double[]) {
            doubleArrayAppend(sbuf, (double[]) o);
        } else {
            objectArrayAppend(sbuf, (Object[]) o, seenMap);
        }
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        } catch (Throwable t) {
            Util.report("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]", t);
            sbuf.append("[FAILED toString()]");
        }
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            int len = a.length;
            for (int i = 0; i < len; i++) {
                deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            seenMap.remove(a);
        } else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }

    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append((int) a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append((int) a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void intArrayAppend(StringBuilder sbuf, int[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void longArrayAppend(StringBuilder sbuf, long[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
}
