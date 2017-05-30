package com.pensive.android.romplanuib.util;

import com.pensive.android.romplanuib.models.Room;

import java.util.Comparator;

/**
 * Comparator for comparing {@link Room}s
 *
 * @author Fredrik Heimsæter
 * @version 1.0
 */

public class RoomComparator implements Comparator<Room>{

    /**
     * Compares two {@link Room}s by their name lexicographically
     * @param room1 the first UiBroom to be compared
     * @param room2 the second UiBroom to be compared
     * @return the value 0 if room1 equals room2,
     *         a value less than 0 if room1 is less than room2
     *         and a value greater than 0 if room1 is greater than room2
     */
    @Override
    public int compare(Room room1, Room room2) {
        return room1.getName().compareTo(room2.getName());
    }
}
