package com.pensive.android.romplanuib.models;

import java.util.Calendar;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public interface CalActivityInterface {


    String getCourseID();
    int getWeekNumber();
    String getTeachingMethod();
    String getTeachingMethodName();
    String getTeachingTitle();
    String getSummary();

    /**
     * Gets the begin date and time of the Activity saved as a Calendar object
     *
     * @return Calendar the begin time and date
     */
    Calendar getBeginTime();

    /**
     * Gets the end time and date of the activity as a Calendar object
     *
     * @return Calendar the end time and date
     */
    Calendar getEndTime();

    /**
     * Returns the description of the Activity
     *
     * @return String the description
     */
}
