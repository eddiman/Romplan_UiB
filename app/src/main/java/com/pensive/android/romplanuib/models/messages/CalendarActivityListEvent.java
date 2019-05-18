package com.pensive.android.romplanuib.models.messages;

import com.pensive.android.romplanuib.models.CalActivity;

import java.util.List;

/**
 * @author Fredrik Heimsæter
 * @version 1.0
 */
public class CalendarActivityListEvent {
    private final List<CalActivity> listOfCalendarActivities;

    public CalendarActivityListEvent(List<CalActivity> listOfCalendarActivities) {
        this.listOfCalendarActivities = listOfCalendarActivities;
    }

    public List<CalActivity> getListOfCalendarActivities() {
        return listOfCalendarActivities;
    }
}
