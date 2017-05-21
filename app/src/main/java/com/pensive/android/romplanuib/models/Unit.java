package com.pensive.android.romplanuib.models;

import java.io.Serializable;

/**
 * Superclass for Units (Buildings and rooms)
 *
 * @author Fredrik Heimsæter
 * @version 2.0
 */

public abstract class Unit implements Serializable{
    private String areaID;
    private String buildingID;
    private String buildingAcronym;
    private String name;
    private String imageURL;

    public Unit(String areaID, String buildingID, String name) {
        this.areaID = areaID;
        this.buildingID = buildingID;
        this.name = name;
    }

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String buildingID) {
        this.buildingID = buildingID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildingAcronym() {
        return buildingAcronym;
    }

    public void setBuildingAcronym(String buildingAcronym) {
        this.buildingAcronym = buildingAcronym;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "areaID='" + areaID + '\'' +
                ", buildingID='" + buildingID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
