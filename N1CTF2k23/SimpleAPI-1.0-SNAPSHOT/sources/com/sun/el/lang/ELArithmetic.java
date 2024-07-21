package com.sun.el.lang;

import com.sun.el.util.MessageFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/ELArithmetic.class */
public abstract class ELArithmetic {
    public static final BigDecimalDelegate BIGDECIMAL = new BigDecimalDelegate();
    public static final BigIntegerDelegate BIGINTEGER = new BigIntegerDelegate();
    public static final DoubleDelegate DOUBLE = new DoubleDelegate();
    public static final LongDelegate LONG = new LongDelegate();
    private static final Long ZERO = 0L;

    protected abstract Number add(Number number, Number number2);

    protected abstract Number multiply(Number number, Number number2);

    protected abstract Number subtract(Number number, Number number2);

    protected abstract Number mod(Number number, Number number2);

    protected abstract Number coerce(Number number);

    protected abstract Number coerce(String str);

    protected abstract Number divide(Number number, Number number2);

    protected abstract boolean matches(Object obj, Object obj2);

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/ELArithmetic$BigDecimalDelegate.class */
    public static final class BigDecimalDelegate extends ELArithmetic {
        @Override // com.sun.el.lang.ELArithmetic
        protected Number add(Number num0, Number num1) {
            return ((BigDecimal) num0).add((BigDecimal) num1);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number coerce(Number num) {
            if (num instanceof BigDecimal) {
                return num;
            }
            if (num instanceof BigInteger) {
                return new BigDecimal((BigInteger) num);
            }
            return new BigDecimal(num.doubleValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number coerce(String str) {
            return new BigDecimal(str);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number divide(Number num0, Number num1) {
            return ((BigDecimal) num0).divide((BigDecimal) num1, 4);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number subtract(Number num0, Number num1) {
            return ((BigDecimal) num0).subtract((BigDecimal) num1);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number mod(Number num0, Number num1) {
            return Double.valueOf(num0.doubleValue() % num1.doubleValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number multiply(Number num0, Number num1) {
            return ((BigDecimal) num0).multiply((BigDecimal) num1);
        }

        @Override // com.sun.el.lang.ELArithmetic
        public boolean matches(Object obj0, Object obj1) {
            return (obj0 instanceof BigDecimal) || (obj1 instanceof BigDecimal);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/ELArithmetic$BigIntegerDelegate.class */
    public static final class BigIntegerDelegate extends ELArithmetic {
        @Override // com.sun.el.lang.ELArithmetic
        protected Number add(Number num0, Number num1) {
            return ((BigInteger) num0).add((BigInteger) num1);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number coerce(Number num) {
            if (num instanceof BigInteger) {
                return num;
            }
            return new BigInteger(num.toString());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number coerce(String str) {
            return new BigInteger(str);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number divide(Number num0, Number num1) {
            return new BigDecimal((BigInteger) num0).divide(new BigDecimal((BigInteger) num1), 4);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number multiply(Number num0, Number num1) {
            return ((BigInteger) num0).multiply((BigInteger) num1);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number mod(Number num0, Number num1) {
            return ((BigInteger) num0).mod((BigInteger) num1);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number subtract(Number num0, Number num1) {
            return ((BigInteger) num0).subtract((BigInteger) num1);
        }

        @Override // com.sun.el.lang.ELArithmetic
        public boolean matches(Object obj0, Object obj1) {
            return (obj0 instanceof BigInteger) || (obj1 instanceof BigInteger);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/ELArithmetic$DoubleDelegate.class */
    public static final class DoubleDelegate extends ELArithmetic {
        @Override // com.sun.el.lang.ELArithmetic
        protected Number add(Number num0, Number num1) {
            if (num0 instanceof BigDecimal) {
                return ((BigDecimal) num0).add(new BigDecimal(num1.doubleValue()));
            }
            if (num1 instanceof BigDecimal) {
                return new BigDecimal(num0.doubleValue()).add((BigDecimal) num1);
            }
            return Double.valueOf(num0.doubleValue() + num1.doubleValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number coerce(Number num) {
            if (num instanceof Double) {
                return num;
            }
            if (num instanceof BigInteger) {
                return new BigDecimal((BigInteger) num);
            }
            return Double.valueOf(num.doubleValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number coerce(String str) {
            return Double.valueOf(str);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number divide(Number num0, Number num1) {
            return Double.valueOf(num0.doubleValue() / num1.doubleValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number mod(Number num0, Number num1) {
            return Double.valueOf(num0.doubleValue() % num1.doubleValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number subtract(Number num0, Number num1) {
            if (num0 instanceof BigDecimal) {
                return ((BigDecimal) num0).subtract(new BigDecimal(num1.doubleValue()));
            }
            if (num1 instanceof BigDecimal) {
                return new BigDecimal(num0.doubleValue()).subtract((BigDecimal) num1);
            }
            return Double.valueOf(num0.doubleValue() - num1.doubleValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number multiply(Number num0, Number num1) {
            if (num0 instanceof BigDecimal) {
                return ((BigDecimal) num0).multiply(new BigDecimal(num1.doubleValue()));
            }
            if (num1 instanceof BigDecimal) {
                return new BigDecimal(num0.doubleValue()).multiply((BigDecimal) num1);
            }
            return Double.valueOf(num0.doubleValue() * num1.doubleValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        public boolean matches(Object obj0, Object obj1) {
            return (obj0 instanceof Double) || (obj1 instanceof Double) || (obj0 instanceof Float) || (obj1 instanceof Float) || (obj0 != null && (Double.TYPE == obj0.getClass() || Float.TYPE == obj0.getClass())) || ((obj1 != null && (Double.TYPE == obj1.getClass() || Float.TYPE == obj1.getClass())) || (((obj0 instanceof String) && ELSupport.isStringFloat((String) obj0)) || ((obj1 instanceof String) && ELSupport.isStringFloat((String) obj1))));
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/ELArithmetic$LongDelegate.class */
    public static final class LongDelegate extends ELArithmetic {
        @Override // com.sun.el.lang.ELArithmetic
        protected Number add(Number num0, Number num1) {
            return Long.valueOf(num0.longValue() + num1.longValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number coerce(Number num) {
            if (num instanceof Long) {
                return num;
            }
            return Long.valueOf(num.longValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number coerce(String str) {
            return Long.valueOf(str);
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number divide(Number num0, Number num1) {
            return Long.valueOf(num0.longValue() / num1.longValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number mod(Number num0, Number num1) {
            return Long.valueOf(num0.longValue() % num1.longValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number subtract(Number num0, Number num1) {
            return Long.valueOf(num0.longValue() - num1.longValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        protected Number multiply(Number num0, Number num1) {
            return Long.valueOf(num0.longValue() * num1.longValue());
        }

        @Override // com.sun.el.lang.ELArithmetic
        public boolean matches(Object obj0, Object obj1) {
            return (obj0 instanceof Long) || (obj1 instanceof Long);
        }
    }

    public static final Number add(Object obj0, Object obj1) {
        ELArithmetic delegate;
        if (obj0 == null && obj1 == null) {
            return 0L;
        }
        if (BIGDECIMAL.matches(obj0, obj1)) {
            delegate = BIGDECIMAL;
        } else if (DOUBLE.matches(obj0, obj1)) {
            delegate = DOUBLE;
        } else if (BIGINTEGER.matches(obj0, obj1)) {
            delegate = BIGINTEGER;
        } else {
            delegate = LONG;
        }
        Number num0 = delegate.coerce(obj0);
        Number num1 = delegate.coerce(obj1);
        return delegate.add(num0, num1);
    }

    public static final Number mod(Object obj0, Object obj1) {
        ELArithmetic delegate;
        if (obj0 == null && obj1 == null) {
            return 0L;
        }
        if (BIGDECIMAL.matches(obj0, obj1)) {
            delegate = BIGDECIMAL;
        } else if (DOUBLE.matches(obj0, obj1)) {
            delegate = DOUBLE;
        } else if (BIGINTEGER.matches(obj0, obj1)) {
            delegate = BIGINTEGER;
        } else {
            delegate = LONG;
        }
        Number num0 = delegate.coerce(obj0);
        Number num1 = delegate.coerce(obj1);
        return delegate.mod(num0, num1);
    }

    public static final Number subtract(Object obj0, Object obj1) {
        ELArithmetic delegate;
        if (obj0 == null && obj1 == null) {
            return 0L;
        }
        if (BIGDECIMAL.matches(obj0, obj1)) {
            delegate = BIGDECIMAL;
        } else if (DOUBLE.matches(obj0, obj1)) {
            delegate = DOUBLE;
        } else if (BIGINTEGER.matches(obj0, obj1)) {
            delegate = BIGINTEGER;
        } else {
            delegate = LONG;
        }
        Number num0 = delegate.coerce(obj0);
        Number num1 = delegate.coerce(obj1);
        return delegate.subtract(num0, num1);
    }

    public static final Number divide(Object obj0, Object obj1) {
        ELArithmetic delegate;
        if (obj0 == null && obj1 == null) {
            return ZERO;
        }
        if (BIGDECIMAL.matches(obj0, obj1)) {
            delegate = BIGDECIMAL;
        } else if (BIGINTEGER.matches(obj0, obj1)) {
            delegate = BIGDECIMAL;
        } else {
            delegate = DOUBLE;
        }
        Number num0 = delegate.coerce(obj0);
        Number num1 = delegate.coerce(obj1);
        return delegate.divide(num0, num1);
    }

    public static final Number multiply(Object obj0, Object obj1) {
        ELArithmetic delegate;
        if (obj0 == null && obj1 == null) {
            return 0L;
        }
        if (BIGDECIMAL.matches(obj0, obj1)) {
            delegate = BIGDECIMAL;
        } else if (DOUBLE.matches(obj0, obj1)) {
            delegate = DOUBLE;
        } else if (BIGINTEGER.matches(obj0, obj1)) {
            delegate = BIGINTEGER;
        } else {
            delegate = LONG;
        }
        Number num0 = delegate.coerce(obj0);
        Number num1 = delegate.coerce(obj1);
        return delegate.multiply(num0, num1);
    }

    public static final boolean isNumber(Object obj) {
        return obj != null && isNumberType(obj.getClass());
    }

    public static final boolean isNumberType(Class type) {
        return type == Long.TYPE || type == Double.TYPE || type == Byte.TYPE || type == Short.TYPE || type == Integer.TYPE || type == Float.TYPE || Number.class.isAssignableFrom(type);
    }

    protected ELArithmetic() {
    }

    protected final Number coerce(Object obj) {
        if (isNumber(obj)) {
            return coerce((Number) obj);
        }
        if (obj instanceof String) {
            return coerce((String) obj);
        }
        if (obj == null || "".equals(obj)) {
            return coerce((Number) ZERO);
        }
        Class objType = obj.getClass();
        if (Character.class.equals(objType) || Character.TYPE == objType) {
            return coerce((Number) Short.valueOf((short) ((Character) obj).charValue()));
        }
        throw new IllegalArgumentException(MessageFactory.get("el.convert", obj, objType));
    }
}
