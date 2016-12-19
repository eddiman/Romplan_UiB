package com.pensive.android.romplanuib.util;

import com.pensive.android.romplanuib.models.UIBbuilding;

import java.util.Comparator;

/**
 * Comparator for comparing {@link UIBbuilding}s
 *
 * @author Fredrik Heimsæter
 * @version 1.0
 */

public class UiBBuildingComparator implements Comparator<UIBbuilding>{

    /**
     * Compares two {@link UIBbuilding}s by their name lexicographically
     * @param building1 the first UiBbuilding to be compared
     * @param building2 the second UiBbuilding to be compared
     * @return the value 0 if building1 equals building2,
     *         a value less than 0 if building1 is less than building2
     *         and a value greater than 0 if building1 is greater than building2
     */
    @Override
    public int compare(UIBbuilding building1, UIBbuilding building2) {
        return building1.getName().compareTo(building2.getName());
    }
}
