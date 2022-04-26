package io.wodo.bscengine.util;

import io.wodo.bscengine.enumtype.ValueEnum;

public final class EnumUtils {


    private EnumUtils() {
    }

    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name, T defaultEnumTypeIfNotFound) {
        try {
            return Enum.valueOf(enumType, name);
        } catch (Exception e) {
            return defaultEnumTypeIfNotFound;
        }
    }

    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
        return valueOf(enumType, name, null);
    }

    public static <T extends Enum<T> & ValueEnum<V>, V> T getValueEnum(Class<T> enumType, V value, T defaultEnumTypeIfNotFound) {
        T t = null;
        T[] enumConstants = enumType.getEnumConstants();

        if (enumConstants != null && enumConstants.length > 0) {
            for (T enumConstant : enumConstants) {
                if (value instanceof String) {
                    if (((String) enumConstant.getValue()).equalsIgnoreCase((String) value)) {
                        t = enumConstant;
                        break;
                    }
                } else if (enumConstant.getValue().equals(value)) {
                    t = enumConstant;
                    break;
                }
            }
        }
        return (t == null) ? defaultEnumTypeIfNotFound : t;
    }

    public static <T extends Enum<T> & ValueEnum<V>, V> T getValueEnum(Class<T> enumType, V value) {
        return getValueEnum(enumType, value, null);
    }

    public static <T extends Enum<T> & ValueEnum<V>, V> boolean isValueEnum(Class<T> enumType, V value) {
        return getValueEnum(enumType, value) != null;
    }

    public static <T extends Enum<T> & ValueEnum<V>, V> boolean isNotValueEnum(Class<T> enumType, V value) {
        return !isValueEnum(enumType, value);
    }
}
