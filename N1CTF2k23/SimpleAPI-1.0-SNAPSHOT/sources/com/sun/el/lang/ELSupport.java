package com.sun.el.lang;

import com.sun.el.util.MessageFactory;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import org.springframework.asm.TypeReference;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/ELSupport.class */
public class ELSupport {
    private static final Long ZERO = 0L;

    public static final void throwUnhandled(Object base, Object property) throws ELException {
        if (base == null) {
            throw new PropertyNotFoundException(MessageFactory.get("error.resolver.unhandled.null", property));
        }
        throw new PropertyNotFoundException(MessageFactory.get("error.resolver.unhandled", base.getClass(), property));
    }

    public static final int compare(Object obj0, Object obj1) throws ELException {
        if (obj0 == obj1 || equals(obj0, obj1)) {
            return 0;
        }
        if (isBigDecimalOp(obj0, obj1)) {
            BigDecimal bd0 = (BigDecimal) coerceToNumber(obj0, BigDecimal.class);
            BigDecimal bd1 = (BigDecimal) coerceToNumber(obj1, BigDecimal.class);
            return bd0.compareTo(bd1);
        } else if (isDoubleOp(obj0, obj1)) {
            Double d0 = (Double) coerceToNumber(obj0, Double.class);
            Double d1 = (Double) coerceToNumber(obj1, Double.class);
            return d0.compareTo(d1);
        } else if (isBigIntegerOp(obj0, obj1)) {
            BigInteger bi0 = (BigInteger) coerceToNumber(obj0, BigInteger.class);
            BigInteger bi1 = (BigInteger) coerceToNumber(obj1, BigInteger.class);
            return bi0.compareTo(bi1);
        } else if (isLongOp(obj0, obj1)) {
            Long l0 = (Long) coerceToNumber(obj0, Long.class);
            Long l1 = (Long) coerceToNumber(obj1, Long.class);
            return l0.compareTo(l1);
        } else if ((obj0 instanceof String) || (obj1 instanceof String)) {
            return coerceToString(obj0).compareTo(coerceToString(obj1));
        } else {
            if (obj0 instanceof Comparable) {
                Comparable<Object> cobj0 = (Comparable) obj0;
                if (obj1 != null) {
                    return cobj0.compareTo(obj1);
                }
                return 1;
            } else if (obj1 instanceof Comparable) {
                Comparable<Object> cobj1 = (Comparable) obj1;
                if (obj0 != null) {
                    return -cobj1.compareTo(obj0);
                }
                return -1;
            } else {
                throw new ELException(MessageFactory.get("error.compare", obj0, obj1));
            }
        }
    }

    public static final boolean equals(Object obj0, Object obj1) throws ELException {
        if (obj0 == obj1) {
            return true;
        }
        if (obj0 == null || obj1 == null) {
            return false;
        }
        if ((obj0 instanceof Boolean) || (obj1 instanceof Boolean)) {
            return coerceToBoolean(obj0).equals(coerceToBoolean(obj1));
        }
        if (obj0.getClass().isEnum()) {
            return obj0.equals(coerceToEnum(obj1, obj0.getClass()));
        }
        if (obj1.getClass().isEnum()) {
            return obj1.equals(coerceToEnum(obj0, obj1.getClass()));
        }
        if ((obj0 instanceof String) || (obj1 instanceof String)) {
            return coerceToString(obj0).equals(coerceToString(obj1));
        }
        if (isBigDecimalOp(obj0, obj1)) {
            BigDecimal bd0 = (BigDecimal) coerceToNumber(obj0, BigDecimal.class);
            BigDecimal bd1 = (BigDecimal) coerceToNumber(obj1, BigDecimal.class);
            return bd0.equals(bd1);
        } else if (isDoubleOp(obj0, obj1)) {
            Double d0 = (Double) coerceToNumber(obj0, Double.class);
            Double d1 = (Double) coerceToNumber(obj1, Double.class);
            return d0.equals(d1);
        } else if (isBigIntegerOp(obj0, obj1)) {
            BigInteger bi0 = (BigInteger) coerceToNumber(obj0, BigInteger.class);
            BigInteger bi1 = (BigInteger) coerceToNumber(obj1, BigInteger.class);
            return bi0.equals(bi1);
        } else if (isLongOp(obj0, obj1)) {
            Long l0 = (Long) coerceToNumber(obj0, Long.class);
            Long l1 = (Long) coerceToNumber(obj1, Long.class);
            return l0.equals(l1);
        } else {
            return obj0.equals(obj1);
        }
    }

    public static final Boolean coerceToBoolean(Object obj) throws IllegalArgumentException {
        if (obj == null || "".equals(obj)) {
            return Boolean.FALSE;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            return Boolean.valueOf((String) obj);
        }
        throw new IllegalArgumentException(MessageFactory.get("error.convert", obj, obj.getClass(), Boolean.class));
    }

    public static final Enum coerceToEnum(Object obj, Class type) {
        if (obj == null || "".equals(obj)) {
            return null;
        }
        if (obj.getClass().isEnum()) {
            return (Enum) obj;
        }
        return Enum.valueOf(type, obj.toString());
    }

    public static final Character coerceToCharacter(Object obj) throws IllegalArgumentException {
        if (obj == null || "".equals(obj)) {
            return (char) 0;
        }
        if (obj instanceof String) {
            return Character.valueOf(((String) obj).charAt(0));
        }
        if (ELArithmetic.isNumber(obj)) {
            return Character.valueOf((char) ((Number) obj).shortValue());
        }
        Class objType = obj.getClass();
        if (obj instanceof Character) {
            return (Character) obj;
        }
        throw new IllegalArgumentException(MessageFactory.get("error.convert", obj, objType, Character.class));
    }

    public static final Number coerceToNumber(Object obj) {
        if (obj == null) {
            return ZERO;
        }
        if (obj instanceof Number) {
            return (Number) obj;
        }
        String str = coerceToString(obj);
        if (isStringFloat(str)) {
            return toFloat(str);
        }
        return toNumber(str);
    }

    protected static final Number coerceToNumber(Number number, Class type) throws IllegalArgumentException {
        if (Long.TYPE == type || Long.class.equals(type)) {
            return Long.valueOf(number.longValue());
        }
        if (Double.TYPE == type || Double.class.equals(type)) {
            return Double.valueOf(number.doubleValue());
        }
        if (Integer.TYPE == type || Integer.class.equals(type)) {
            return Integer.valueOf(number.intValue());
        }
        if (BigInteger.class.equals(type)) {
            if (number instanceof BigDecimal) {
                return ((BigDecimal) number).toBigInteger();
            }
            if (number instanceof BigInteger) {
                return number;
            }
            return BigInteger.valueOf(number.longValue());
        } else if (BigDecimal.class.equals(type)) {
            if (number instanceof BigDecimal) {
                return number;
            }
            if (number instanceof BigInteger) {
                return new BigDecimal((BigInteger) number);
            }
            if (number instanceof Long) {
                return new BigDecimal(((Long) number).longValue());
            }
            return new BigDecimal(number.doubleValue());
        } else if (Byte.TYPE == type || Byte.class.equals(type)) {
            return Byte.valueOf(number.byteValue());
        } else {
            if (Short.TYPE == type || Short.class.equals(type)) {
                return Short.valueOf(number.shortValue());
            }
            if (Float.TYPE == type || Float.class.equals(type)) {
                return Float.valueOf(number.floatValue());
            }
            throw new IllegalArgumentException(MessageFactory.get("error.convert", number, number.getClass(), type));
        }
    }

    public static final Number coerceToNumber(Object obj, Class type) throws IllegalArgumentException {
        if (obj == null || "".equals(obj)) {
            return coerceToNumber((Number) ZERO, type);
        }
        if (obj instanceof String) {
            return coerceToNumber((String) obj, type);
        }
        if (ELArithmetic.isNumber(obj)) {
            if (obj.getClass().equals(type)) {
                return (Number) obj;
            }
            return coerceToNumber((Number) obj, type);
        } else if (obj instanceof Character) {
            return coerceToNumber((Number) Short.valueOf((short) ((Character) obj).charValue()), type);
        } else {
            throw new IllegalArgumentException(MessageFactory.get("error.convert", obj, obj.getClass(), type));
        }
    }

    protected static final Number coerceToNumber(String val, Class type) throws IllegalArgumentException {
        if (Long.TYPE == type || Long.class.equals(type)) {
            return Long.valueOf(val);
        }
        if (Integer.TYPE == type || Integer.class.equals(type)) {
            return Integer.valueOf(val);
        }
        if (Double.TYPE == type || Double.class.equals(type)) {
            return Double.valueOf(val);
        }
        if (BigInteger.class.equals(type)) {
            return new BigInteger(val);
        }
        if (BigDecimal.class.equals(type)) {
            return new BigDecimal(val);
        }
        if (Byte.TYPE == type || Byte.class.equals(type)) {
            return Byte.valueOf(val);
        }
        if (Short.TYPE == type || Short.class.equals(type)) {
            return Short.valueOf(val);
        }
        if (Float.TYPE == type || Float.class.equals(type)) {
            return Float.valueOf(val);
        }
        throw new IllegalArgumentException(MessageFactory.get("error.convert", val, String.class, type));
    }

    public static final String coerceToString(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Enum) {
            return ((Enum) obj).name();
        }
        return obj.toString();
    }

    public static final void checkType(Object obj, Class<?> type) throws IllegalArgumentException {
        if (String.class.equals(type)) {
            coerceToString(obj);
        }
        if (ELArithmetic.isNumberType(type)) {
            coerceToNumber(obj, type);
        }
        if (Character.class.equals(type) || Character.TYPE == type) {
            coerceToCharacter(obj);
        }
        if (Boolean.class.equals(type) || Boolean.TYPE == type) {
            coerceToBoolean(obj);
        }
        if (type.isEnum()) {
            coerceToEnum(obj, type);
        }
    }

    public static final Object coerceToType(Object obj, Class<?> type) throws IllegalArgumentException {
        return coerceToType(obj, type, false);
    }

    public static final Object coerceToType(Object obj, Class<?> type, boolean isEL22Compatible) throws IllegalArgumentException {
        if (type == null || Object.class.equals(type) || (obj != null && type.isAssignableFrom(obj.getClass()))) {
            return obj;
        }
        if (!isEL22Compatible && obj == null && !type.isPrimitive() && !String.class.equals(type)) {
            return null;
        }
        if (String.class.equals(type)) {
            return coerceToString(obj);
        }
        if (ELArithmetic.isNumberType(type)) {
            return coerceToNumber(obj, type);
        }
        if (Character.class.equals(type) || Character.TYPE == type) {
            return coerceToCharacter(obj);
        }
        if (Boolean.class.equals(type) || Boolean.TYPE == type) {
            return coerceToBoolean(obj);
        }
        if (type.isEnum()) {
            return coerceToEnum(obj, type);
        }
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            if ("".equals(obj)) {
                return null;
            }
            PropertyEditor editor = PropertyEditorManager.findEditor(type);
            if (editor != null) {
                editor.setAsText((String) obj);
                return editor.getValue();
            }
        }
        throw new IllegalArgumentException(MessageFactory.get("error.convert", obj, obj.getClass(), type));
    }

    public static final boolean containsNulls(Object[] obj) {
        for (int i = 0; i < obj.length; i++) {
            if (obj[0] == null) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isBigDecimalOp(Object obj0, Object obj1) {
        return (obj0 instanceof BigDecimal) || (obj1 instanceof BigDecimal);
    }

    public static final boolean isBigIntegerOp(Object obj0, Object obj1) {
        return (obj0 instanceof BigInteger) || (obj1 instanceof BigInteger);
    }

    public static final boolean isDoubleOp(Object obj0, Object obj1) {
        return (obj0 instanceof Double) || (obj1 instanceof Double) || (obj0 instanceof Float) || (obj1 instanceof Float);
    }

    public static final boolean isDoubleStringOp(Object obj0, Object obj1) {
        return isDoubleOp(obj0, obj1) || ((obj0 instanceof String) && isStringFloat((String) obj0)) || ((obj1 instanceof String) && isStringFloat((String) obj1));
    }

    public static final boolean isLongOp(Object obj0, Object obj1) {
        return (obj0 instanceof Long) || (obj1 instanceof Long) || (obj0 instanceof Integer) || (obj1 instanceof Integer) || (obj0 instanceof Character) || (obj1 instanceof Character) || (obj0 instanceof Short) || (obj1 instanceof Short) || (obj0 instanceof Byte) || (obj1 instanceof Byte);
    }

    public static final boolean isStringFloat(String str) {
        int len = str.length();
        if (len > 1) {
            for (int i = 0; i < len; i++) {
                switch (str.charAt(i)) {
                    case '.':
                        return true;
                    case TypeReference.CONSTRUCTOR_REFERENCE /* 69 */:
                        return true;
                    case 'e':
                        return true;
                    default:
                }
            }
            return false;
        }
        return false;
    }

    public static final Number toFloat(String value) {
        try {
            if (Double.parseDouble(value) > Double.MAX_VALUE) {
                return new BigDecimal(value);
            }
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            return new BigDecimal(value);
        }
    }

    public static final Number toNumber(String value) {
        try {
            return Integer.valueOf(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            try {
                return Long.valueOf(Long.parseLong(value));
            } catch (NumberFormatException e2) {
                return new BigInteger(value);
            }
        }
    }
}
