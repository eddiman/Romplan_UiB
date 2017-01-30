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
public class UIBbuilding extends UiBunit implements Serializable{
    private List<UIBroom> listOfRooms;


    public UIBbuilding(String name, String buildingCode, List<UIBroom> listOfRooms) {
        super(buildingCode, name);
        this.listOfRooms = listOfRooms;
    }

    public void setListOfRooms(List<UIBroom> listOfRooms) {
        this.listOfRooms = listOfRooms;
    }

    public List<UIBroom> getListOfRooms() {
        return listOfRooms;
    }

    @Override
    public String toString() {
        return "UIBbuilding{" +
                "name='" + this.getName() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UIBbuilding that = (UIBbuilding) o;

        return this.getName().equals(that.getName());

    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
