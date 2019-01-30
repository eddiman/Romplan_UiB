package com.pensive.android.romplanuib.models;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Edvard Bjørgen & Fredrik Heimsæter
 * @version 2.0
 */
public class CalActivity implements CalActivityInterface {
    @SerializedName("semesterid")
    private String semesterID;
    @SerializedName("courseid")
    private String courseID;
    @SerializedName("actid")
    private String activityID;
    @SerializedName("weeknr")
    private int weekNumber;
    @SerializedName("teaching-method")
    private String teachingMethod;
    @SerializedName("teaching-method-name")
    private String teachingMethodName;
    @SerializedName("teaching-title")
    private String teachingTitle;
    @SerializedName("dtstart")
    private String beginTime;
    @SerializedName("dtend")
    private String endTime;
    @SerializedName("summary")
    private String summary;

    public CalActivity(String semesterID, String courseID, String activityID, int weekNumber, String teachingMethod, String teachingMethodName, String teachingTitle, String beginTime, String endTime, String summary) {
        this.semesterID = semesterID;
        this.courseID = courseID;
        this.activityID = activityID;
        this.weekNumber = weekNumber;
        this.teachingMethod = teachingMethod;
        this.teachingMethodName = teachingMethodName;
        this.teachingTitle = teachingTitle;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.summary = summary;
    }

    /**
     *
     * @param timeDateString
     *            String containing the date for the activity in a yyyy-MM-dd'T'HH:mm:ssZ format
     * @return A Calendar object with the time and date input, or null if either parameter is invalid
     */
        private Calendar parseCalendarDate(String timeDateString) {

        SimpleDateFormat dateformater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

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
        return parseCalendarDate(beginTime);
    }

    /**
     * Returns the date and time as a Calendar at which the activity ends
     */
    @Override
    public Calendar getEndTime() {
        return parseCalendarDate(endTime);
    }



}
