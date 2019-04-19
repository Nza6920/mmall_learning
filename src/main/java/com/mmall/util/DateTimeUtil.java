package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串转 Date
     * @param dateTimeStr 时间字符串
     * @param formatStr 时间格式
     * @return Date
     */
    public static Date strToDate(String dateTimeStr, String formatStr)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);

        return dateTime.toDate();
    }

    /**
     * Date 转字符串
     * @param date 时间
     * @param formatStr 转换格式
     * @return String
     */
    public static String dateToStr(Date date, String formatStr)
    {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        DateTime dateTime = new DateTime(date);

        return dateTime.toString(formatStr);
    }

    /**
     * 字符串转 Date
     * @param dateTimeStr 时间字符串
     * @return Date
     */
    public static Date strToDate(String dateTimeStr)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);

        return dateTime.toDate();
    }

    /**
     * Date 转字符串
     * @param date 时间
     * @return String
     */
    public static String dateToStr(Date date)
    {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
}
