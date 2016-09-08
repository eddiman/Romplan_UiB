package com.pensive.android.romplanuib.models;

import java.io.Serializable;
import java.util.List;

/**
 * Class for making a building object
 *
 * @author Gaute Gjerløw Remen
 * @version 1.0
 *
 */
public class UIBbuilding implements Serializable{

    private String name;

    private List<UIBroom> listOfRooms;


    public UIBbuilding(String name, List<UIBroom> listOfRooms) {
        this.name= name;
        this.listOfRooms = listOfRooms;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setListOfRooms(List<UIBroom> listOfRooms) {
        this.listOfRooms = listOfRooms;
    }

    public List<UIBroom> getListOfRooms() {
        return listOfRooms;
    }

}
