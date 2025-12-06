package com.factory.ml.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility for parsing and formatting dates.
 * 
 * Provides static methods for converting between string and Date objects
 * using a standard date format (yyyy-MM-dd).
 * 
 * <p>Project Context:
 * Used throughout the factory-ml-offline-system for consistent date handling
 * in data processing and feature transformation.
 */
public class DateParser {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Parses a date string into a Date object.
     * 
     * @param dateString Date string in yyyy-MM-dd format
     * @return Parsed Date object
     * @throws ParseException if the string cannot be parsed
     */
    public static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateFormat.parse(dateString);
    }

    /**
     * Formats a Date object into a string.
     * 
     * @param date Date object to format
     * @return Formatted date string in yyyy-MM-dd format
     */
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(date);
    }
}