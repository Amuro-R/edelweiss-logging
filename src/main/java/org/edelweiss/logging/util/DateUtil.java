package org.edelweiss.logging.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class DateUtil {

    public static String formatDateDefault(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
