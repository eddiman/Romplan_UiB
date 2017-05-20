package com.pensive.android.romplanuib.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class CalActivity implements CalActivityInterface {

    private String courseID;
    private int weekNumber;
    private String teachingMethod;
    private String teachingMethodName;
    private String teachingTitle;
    private Calendar beginTime;
    private Calendar endTime;
    private String summary;

    public CalActivity(String courseID, int weekNumber, String teachingMethod, String teachingMethodName, String teachingTitle, String beginTime, String endTime, String summary) {
        this.courseID = courseID;
        this.weekNumber = weekNumber;
        this.teachingMethod = teachingMethod;
        this.teachingMethodName = teachingMethodName;
        this.teachingTitle = teachingTitle;
        this.beginTime = parseCalendarDate(beginTime);
        this.endTime = parseCalendarDate(endTime);
        this.summary = summary;
    }



    /**
     *
     * @param timeDateString
     *            String containing the date for the activity in a yyyy-MM-dd'T'HH:mm:ssZ format
     * @return A Calendar object with the time and date input, or null if either parameter is invalid
     */
    private Calendar parseCalendarDate(String timeDateString) {

        SimpleDateFormat dateformater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

        try {
            Date date = dateformater.parse(timeDateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String toString() {
        return "CalActivity{" +
                "courseID='" + courseID + '\'' +
                ", weekNumber=" + weekNumber +
                ", teachingMethod='" + teachingMethod + '\'' +
                ", teachingMethodName='" + teachingMethodName + '\'' +
                ", teachingTitle='" + teachingTitle + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", summary='" + summary + '\'' +
                '}';
    }

    /**
     * @return the Node object containing the information about this activity Do
     *         note that this will return null if the object has loaded from a
     *         file
     */

    @Override
    public String getCourseID() {
        return courseID;
    }

    @Override
    public int getWeekNumber() {
        return weekNumber;
    }

    @Override
    public String getTeachingMethod() {
        return teachingMethod;
    }

    @Override
    public String getTeachingMethodName() {
        return teachingMethodName;
    }

    @Override
    public String getTeachingTitle() {
        return teachingTitle;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    /**
     * Returns the date and time as a Calendar at which the activity starts
     */
    @Override
    public Calendar getBeginTime() {
        return beginTime;
    }

    /**
     * Returns the date and time as a Calendar at which the activity ends
     */
    @Override
    public Calendar getEndTime() {
        return endTime;
    }



}
