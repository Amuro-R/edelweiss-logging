package org.edelweiss.logging.util;

public class StringUtil {

    public static String nullConvert(String origin) {
        if (origin == null) {
            return "null";
        }
        return origin;
    }
}
