package org.edelweiss.logging.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author jingyun
 * @date 2022-09-01
 */
public class DateUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER;

    static {
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDateDefault(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatDateDefault(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String formatLocalDateTimeDefault(LocalDateTime date) {
        return date.format(DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTimeDefault(String date) {
        return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    }


    public static String extractDate(Date datetime) {
        long time = datetime.getTime();
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTimeFormatter.format(localDate);
    }


    public static String getTimedFilePath(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        String str = format.format(date);
        return str;
    }

    public static String getTimedFileName(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
        String str = format.format(date);
        return str;
    }


    public static String formatDateByFormat(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static Date parseGolangTime(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'");
        try {
            return formatter.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
