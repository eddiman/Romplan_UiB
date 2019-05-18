package com.pensive.android.romplanuib.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Superclass for Units (Buildings and rooms)
 *
 * @author Fredrik Heimsæter
 * @version 3.0
 */

public abstract class Unit implements Serializable{
    @SerializedName("university")
    private String universityID;
    @SerializedName("area")
    private String areaID;
    @SerializedName("name")
    private String name;
    private String imageURL;

    public Unit(String universityID, String areaID, String name) {
        this.universityID = universityID;
        this.areaID = areaID;
        this.name = name;
    }

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    public String getUniversityID() {
        return universityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "universityID='" + universityID + '\'' +
                ", areaID='" + areaID + '\'' +
                ", name='" + name + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
