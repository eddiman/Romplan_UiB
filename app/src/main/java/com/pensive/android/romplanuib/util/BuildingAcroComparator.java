package com.pensive.android.romplanuib.util;

import com.pensive.android.romplanuib.models.Building;

import java.util.Comparator;

/**
 * Comparator for comparing {@link Building}s
 *
 * @author Fredrik Heimsæter
 * @version 1.0
 */

public class BuildingAcroComparator implements Comparator<Building> {

    /**
     * Compares two {@link Building}s by their name lexicographically
     *
     * @param building1 the first UiBbuilding to be compared
     * @param building2 the second UiBbuilding to be compared
     * @return the value 0 if building1 equals building2,
     * a value less than 0 if building1 is less than building2
     * and a value greater than 0 if building1 is greater than building2
     */
    @Override
    public int compare(Building building1, Building building2) {
        return building1.getBuildingAcronym().compareTo(building2.getBuildingAcronym());
    }
}
