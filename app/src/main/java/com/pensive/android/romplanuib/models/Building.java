package com.pensive.android.romplanuib.models;



import java.io.Serializable;
import java.util.List;

/**
 * Class for making a building object
 *
 * @author Gaute Gjerløw Remen & Fredrik Heimsæter
 * @version 2.0
 *
 */
public class Building extends Unit implements Serializable{
    private List<Room> listOfRooms;


    public Building(String areaID, String buildingID, String buildingName, List<Room> listOfRooms) {
        super(areaID, buildingID, buildingName);
        this.listOfRooms = listOfRooms;
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
                ", buildingID='" + this.getBuildingID() + '\'' +
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
