package com.pensive.android.romplanuib.util.comparators;

import com.pensive.android.romplanuib.models.CalActivity;

import java.util.Comparator;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class CalActivityComparator implements Comparator<CalActivity> {

    /**
     * Method for comparing activites based on date
     */
    @Override
    public int compare(CalActivity activity1, CalActivity activity2) {
        return activity1.getBeginTime().compareTo(activity2.getBeginTime());
    }

}
