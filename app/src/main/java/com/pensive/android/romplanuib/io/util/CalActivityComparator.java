package com.pensive.android.romplanuib.io.util;

import com.pensive.android.romplanuib.models.CalActivity;

import java.util.Comparator;

/**
 * Created by EddiStat on 05.06.2016.
 */
public class CalActivityComparator implements Comparator<CalActivity> {

    /**
     * Method for comparing activites based on date
     */
    @Override
    public int compare(CalActivity activity1, CalActivity activity2) {
        return activity1.getDate().compareTo(activity2.getDate());
    }

}
