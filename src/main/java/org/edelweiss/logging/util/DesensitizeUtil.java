package org.edelweiss.logging.util;

import org.springframework.util.StringUtils;

/**
 * @author jingyun
 * @date 2022-09-08
 */
public class DesensitizeUtil {

    public static String phoneDesensitize(String phone, char symbol) {
        return DesensitizeUtil.stringDesensitize(phone, 4, 7, symbol);
    }

    public static String stringDesensitize(String str, int start, int end, char symbol) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        if (start < 0 || end < 0 || start + end > str.length()) {
            throw new RuntimeException("脱敏格式错误");
        }
        char[] chars = str.toCharArray();
        for (int i = start - 1; i < end; i++) {
            chars[i] = symbol;
        }
        return String.valueOf(chars);
    }


}
