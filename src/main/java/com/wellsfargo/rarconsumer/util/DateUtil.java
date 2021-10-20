package com.wellsfargo.rarconsumer.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class to handle various Date operations such as parse, format etc.
 *
 * @author u763036
 * @version 1.0
 */
public class DateUtil {

    public static final String DATE_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String TIME_ISO = "T00:00:00.000+0000";
    public static final String DATE_FORMAT_LOCAL_DATE = "yyyy-MM-dd";
    public static final String RAR_START_DATE = "1900-01-01";

    private DateUtil() {
    }

    /**
     * Parses given string representation of date into {@link Date} object using
     * the given date format. Leniency is specified by method argument
     * isLenient. If string representation of date is to match the given date
     * format isLenient is true else false..
     *
     * @param dateFormat the date format to be used while parsing.
     * @param isLenient  true if leniency is allowed else false
     * @param dateString the string representation of date.
     * @return date object
     * @throws ParseException if error occurs while parsing string to date.
     */

    public static Date parse(String dateFormat, boolean isLenient, String dateString) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setLenient(isLenient);
        return simpleDateFormat.parse(dateString);
    }

    /**
     * Formats the given {@link Date} to string using the given format.
     *
     * @param dateFormat the date format to be used.
     * @param date       the date object for which string is to be returned.
     * @return formatted date string.
     */
    public static String format(String dateFormat, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    public static String dateAfter() {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, 365);
        return df1.format(c1.getTime());
    }

    public static String dateBefore() {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, -365);
        return df1.format(c1.getTime());
    }

    public static Date convertUTC() {
        Instant instant = Instant.now();
        return new Date(instant.toEpochMilli());
    }

    /**
     * Adjust given local date to last date of the month.
     *
     * @param endDate the date that is to be adjusted to last date of month.
     * @return end date adjusted to last date of month.
     */
    public static LocalDate adjustToLastDayOfMonth(LocalDate endDate) {
        boolean isLeapYear = endDate.isLeapYear();
        endDate = endDate.withDayOfMonth(endDate.getMonth().length(isLeapYear));
        return endDate;
    }

    /**
     * Add 7 days to timePeriodEnd.
     *
     * @param date to which days to be added.
     * @param days number of days to be added.
     * @return ISO date string.
     */
    public static String addDays(String date, int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_LOCAL_DATE);
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDate nextWeekDate = localDate.plusDays(days);
        return nextWeekDate.format(formatter) + TIME_ISO;
    }

    /**
     * subtract 7 days to timePeriodEnd.
     *
     * @param date to which days to be subtracted.
     * @param days number of days to be subtracted.
     * @return ISO date string.
     */
    public static String subtractsDays(String date, int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_LOCAL_DATE);
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDate beforeWeekDay = localDate.minusDays(days);
        return beforeWeekDay.format(formatter) + TIME_ISO;
    }

    /**
     * compares the given date with ref date.
     *
     * @param date    to be compared
     * @param pattern date format
     * @param refDate date to be compared with {@code date}
     * @return true if equal else false
     */
    public static boolean compareDate(Date date, String pattern, String refDate) {
        DateFormat formatter = new SimpleDateFormat(pattern);
        return date != null && formatter.format(date).equals(refDate);

    }
}
