package org.edelweiss.logging.util;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class StringUtil {

    public static String nullConvert(String origin) {
        if (origin == null) {
            return "null";
        }
        return origin;
    }
}
