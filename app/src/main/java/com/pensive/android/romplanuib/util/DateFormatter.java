package com.pensive.android.romplanuib.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class DateFormatter {


    public String formatCalendar(Calendar calendar, String format) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
