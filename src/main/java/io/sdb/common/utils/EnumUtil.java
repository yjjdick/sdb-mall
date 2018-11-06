package io.sdb.common.utils;

import io.sdb.enums.IEnum;

public class EnumUtil {

    public static <T extends IEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }

    public static <T extends IEnum> T getByMsg(String msg, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (msg.equalsIgnoreCase(each.getMessage())) {
                return each;
            }
        }
        return null;
    }
}
