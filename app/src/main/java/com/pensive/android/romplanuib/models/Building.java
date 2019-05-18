package com.pensive.android.romplanuib.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Class for making a building object
 *
 * @author Gaute Gjerløw Remen & Fredrik Heimsæter
 * @version 3.0
 *
 */
public class Building extends Unit implements Serializable{
    @SerializedName("id")
    private String buildingID;
    @SerializedName("rooms")
    private List<Room> listOfRooms;

    public Building(String universityID, String areaID, String name) {
        super(universityID, areaID, name);
    }

    public String getBuildingID() {
        return buildingID;
    }

    public void setListOfRooms(List<Room> listOfRooms) {
        this.listOfRooms = listOfRooms;
    }

    public List<Room> getListOfRooms() {
        return listOfRooms;
    }

    @Override
    public String toString() {
        return "Building{" +
                "areaID='" + this.getAreaID() + '\'' +
                ", name='" + this.getName() + '\'' +
                "listOfRooms=" + listOfRooms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Building that = (Building) o;

        return this.getName().equals(that.getName());

    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
