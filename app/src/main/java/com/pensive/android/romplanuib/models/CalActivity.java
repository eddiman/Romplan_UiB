package com.pensive.android.romplanuib.models;

import org.jsoup.nodes.Node;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class CalActivity implements CalActivityInterface {

    // Node is not serializable, drop from serialization
    private Node activityNode;

    private String roomCode;
    private String buildingCode;

    private String subject;
    private String type;
    private String weekday;
    private String time;
    private String date;

    private Calendar startCalendar;
    private Calendar endCalendar;

    /**
     *
     * @param activityNode
     *            Node containing the activity itself
     * @param subject
     *            String representing the subject
     * @param time
     *            String representation of start and endtime of the activity
     * @param type
     *            String type of the activity (Forelesning/Seminar ect)
     * @param room
     *            String representation of the code for the room the activity
     *            takes place in
     * @param building
     *            String represenatation of the code of the building the
     *            activity takes place in
     * @param date
     *            String representation of the date for the activity
     */

    public CalActivity(Node activityNode, String subject, String time,
                       String type, String building, String room, String date) {

        String calendarDate = "";

        Pattern pattern = Pattern.compile("[0-9.]+");
        Matcher matcher = pattern.matcher(date);

        if (matcher.find())
            calendarDate = matcher.group(0);

        setWeekday(date.substring(0, 7));
        setSubject(subject);
        setTime(time);
        setType(type);
        setDate(calendarDate);

        this.startCalendar = parseCalendarDate(calendarDate,
                time.substring(0, 5));
        this.endCalendar = parseCalendarDate(calendarDate,
                time.substring(6, 11));

        this.activityNode = activityNode;
        this.roomCode = room;
        this.buildingCode = building;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     *
     * @param dateString
     *            String containing the date for the activity in a dd.mm.yyyy
     *            format
     * @param timeString
     *            String containing the start or endtime for the activity in a
     *            hh:mm format
     * @return A Calendar object with the time and date input, or null if either
     *         parameter is invalid
     */
    private Calendar parseCalendarDate(String dateString, String timeString) {

        String formatedDateString = timeString + "." + dateString;
        SimpleDateFormat dateformater = new SimpleDateFormat("HH:mm.dd.MM.yyyy");

        try {
            Date date = dateformater.parse(formatedDateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to assist in serializing activitites. Do note that information
     * about which Node this activity belongs to is lost during serialization.
     *
     * @return a serializable representation of this activity To deserialize
     */
    public CalActivitySerializationHelper getSerializableObject() {
        return new CalActivitySerializationHelper(getWeekday(), getTime(),
                getDate(), getSubject(), getType(), getBuildingCode(),
                getRoomCode());
    }

    /**
     * Returns a string representation of the object with the date, time and
     * room code
     */
    @Override
    public String toString() {
        return getWeekday() + " " + getTime() + " " + getDate() + " "
                + getSubject() + " " + getType() + " " + getRoomCode();
    }

    /**
     * @return the Node object containing the information about this activity Do
     *         note that this will return null if the object has loaded from a
     *         file
     */
    @Override
    public Node getNode() {
        return activityNode;
    }

    /**
     * Returns what type of activity this is (Lecture/Seminar ect..)
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Returns the room code for the room where the activity takes place
     */
    @Override
    public String getRoomCode() {
        return roomCode;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    /**
     * Returns the date and time as a Calendar at which the activity starts
     */
    @Override
    public Calendar getBeginTime() {
        return startCalendar;
    }

    /**
     * Returns the date and time as a Calendar at which the activity ends
     */
    @Override
    public Calendar getEndTime() {
        return endCalendar;
    }

    /**
     * Returns a string description of the activity (same as getType())
     */
    @Override
    public String getDescription() {
        return type;
    }


}
