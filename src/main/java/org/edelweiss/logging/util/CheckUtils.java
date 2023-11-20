package org.edelweiss.logging.util;

import org.edelweiss.logging.exception.LoggingException;
import org.springframework.util.ObjectUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jingyun
 * @date 2022-08-19
 */
public class CheckUtils {

    private static final Pattern PHONE_PATTERN;
    private static final Pattern IPV4_PATTERN;
    private static final Pattern IPV4_EXTRACT_PATTERN;
    private static final Pattern FACE_PATTERN;

    static {
        PHONE_PATTERN = Pattern.compile("[0-9]{11}");
        IPV4_PATTERN = Pattern.compile("^(([1-9]?\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.){3}([1-9]?\\d|1\\d{2}|2[0-4]\\d|25[0-5])$");
        IPV4_EXTRACT_PATTERN = Pattern.compile("((([1-9]?\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.){3}([1-9]?\\d|1\\d{2}|2[0-4]\\d|25[0-5]))");
        FACE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa50-9a-zA-Z]+_[0-9]{11}");
        ;
        ;
    }

    public static void emptyCheck(Object obj, String msg) {
        if (ObjectUtils.isEmpty(obj)) {
            throw new LoggingException(msg);
        }
    }

    public static void phoneCheck(String phone) {
        Matcher matcher = PHONE_PATTERN.matcher(phone);
        if (!matcher.matches()) {
            throw new LoggingException("手机号格式错误");
        }
    }

    public static void ipV4Check(String str) {
        emptyCheck(str, "ip地址为空");
        Matcher matcher = IPV4_PATTERN.matcher(str);
        if (!matcher.matches()) {
            throw new LoggingException("ipv4格式错误");
        }
    }

    public static void faceFileCheck(String fileName) {
        Matcher matcher = FACE_PATTERN.matcher(fileName);
        if (!matcher.matches()) {
            throw new LoggingException("文件名格式错误，姓名_手机号，姓名支持中英文数字，手机号仅限11位数字");
        }
    }

    public static String ipV4AddressExtract(String str) {
        Matcher matcher = IPV4_EXTRACT_PATTERN.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    public static void positiveCheck(int num, String msg) {
        if (num <= 0) {
            throw new LoggingException(msg);
        }
    }

    public static void nollSafeEqualCheck(Object o1, Object o2, String msg) {
        boolean equals = ObjectUtils.nullSafeEquals(o1, o2);
        if (!equals) {
            throw new LoggingException(msg);
        }

    }

    public static void unNegativeCheck(int num, String msg) {
        if (num < 0) {
            throw new LoggingException(msg);
        }
    }
}
