package com.zh.snmp.snmpweb.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Golyo
 */
public class DateUtils {
    public static String parseDate(Date date, int style, Locale local, boolean withTime) {
        if (date == null) {
            return null;
        } else {
            DateFormat ft = withTime ? DateFormat.getDateTimeInstance(style, style, local) : DateFormat.getDateInstance(style, local);
            return ft.format(date);
        }
    }    
}
