package com.hackerrank.weather.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String format(LocalDate date, String pattern){
        if (date == null){
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate stringToDate(String dateString) {
        return LocalDate.parse(dateString, formatter);
    }
}
