package com.pensive.android.romplanuib.models;

import java.io.Serializable;

/**
 * Class for making a room object
 *
 * @author Gaute Gjerløw Remen
 * @version 1.0
 *
 */
public class UIBroom implements Serializable{
    private String name;
    private String building;



    public UIBroom(String name ,String building) {
        this.name = name;
        this.building = building;

    }
    public String getBuilding() {
        return building;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

}
