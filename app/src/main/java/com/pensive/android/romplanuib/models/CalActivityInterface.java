package com.pensive.android.romplanuib.models;

import org.jsoup.nodes.Node;

import java.util.Calendar;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public interface CalActivityInterface {


    /**
     * Returns the value of the field node, which saves the primary node of the
     * activity
     *
     * @return Node the HTML node
     */
    public Node getNode();

    /**
     * Gets the type of activity
     *
     * @return String the type of activity
     */
    public String getType();

    /**
     * Gets the room associated with the Activity
     *
     * @return String the room name
     */
    public String getRoomCode();

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
    public String getDescription();
}
