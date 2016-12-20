package com.pensive.android.romplanuib.models;

import java.io.Serializable;

/**
 * Class for making a room object
 *
 * @author Gaute Gjerløw Remen, Fredrik Heimsæter
 * @version 1.1
 *
 */
public class UIBroom implements Serializable{
    private String code;
    private String building;
    private String name;


    public UIBroom(String code , String building, String name) {
        this.code = code;
        this.building = building;
        this.name = name;

    }
    public String getBuilding() {
        return building;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

}
