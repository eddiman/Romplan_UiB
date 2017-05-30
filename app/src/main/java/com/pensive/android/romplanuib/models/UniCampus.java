package com.pensive.android.romplanuib.models;

/**
 * Created by moled on 28.05.2017.
 */

public class Campus {
    String name;
    String campusCode;

    public Campus(String name, String campusCode) {
        this.name = name;
        this.campusCode = campusCode;
    }

    public String getName() {
        return name;
    }

    public String getCampusCode() {
        return campusCode;
    }
}
