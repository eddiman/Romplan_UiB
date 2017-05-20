package com.pensive.android.romplanuib.models;

import org.jsoup.nodes.Node;

import java.util.Calendar;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public interface CalActivityInterface {


    public String getCourseID();
    public int getWeekNumber();
    public String getTeachingMethod();
    public String getTeachingMethodName();
    public String getTeachingTitle();
    public String getSummary();

    /**
     * Gets the begin date and time of the Activity saved as a Calendar object
     *
     * @return Calendar the begin time and date
     */
    public Calendar getBeginTime();

    /**
     * Gets the end time and date of the activity as a Calendar object
     *
     * @return Calendar the end time and date
     */
    public Calendar getEndTime();

    /**
     * Returns the description of the Activity
     *
     * @return String the description
     */
}
