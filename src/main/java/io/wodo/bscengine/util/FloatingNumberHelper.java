package io.wodo.bscengine.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FloatingNumberHelper {

    public static double roundDoubleValue(double value, int scale, RoundingMode roundingMode) {
        final BigDecimal bd = BigDecimal.valueOf(value);
        return bd.setScale(scale, roundingMode).doubleValue();
    }

    public static double addDoubleValues(double value1, double value2) {
        final BigDecimal v1 = BigDecimal.valueOf(value1);
        final BigDecimal v2 = BigDecimal.valueOf(value2);
        return v1.add(v2).doubleValue();
    }

    public static double subtractDoubleValues(double value1, double value2) {
        final BigDecimal v1 = BigDecimal.valueOf(value1);
        final BigDecimal v2 = BigDecimal.valueOf(value2);
        return v1.subtract(v2).doubleValue();
    }

    public static double multiplyDoubleValues(double value1, double value2) {
        final BigDecimal v1 = BigDecimal.valueOf(value1);
        final BigDecimal v2 = BigDecimal.valueOf(value2);
        return v1.multiply(v2).doubleValue();
    }

    public static double divideDoubleValues(double value1, double value2, int scale, RoundingMode roundingMode) {
        if (new BigDecimal(value2).compareTo(BigDecimal.ZERO) == 0)
            throw new ArithmeticException("divide by zero.");

        final BigDecimal v1 = BigDecimal.valueOf(value1);
        final BigDecimal v2 = BigDecimal.valueOf(value2);
        return v1.divide(v2, scale, roundingMode).doubleValue();
    }
}
