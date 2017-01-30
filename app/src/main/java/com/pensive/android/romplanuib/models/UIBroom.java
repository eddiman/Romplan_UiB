package com.pensive.android.romplanuib.models;

import java.io.Serializable;

/**
 * Class for making a room object
 *
 * @author Gaute Gjerløw Remen, Fredrik Heimsæter
 * @version 1.1
 *
 */
public class UIBroom extends UiBunit implements Serializable{
    private String building;


    public UIBroom(String code , String building, String name) {
        super(code,name);
        this.building = building;

    }
    public String getBuilding() {
        return building;
    }


    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UIBroom uiBroom = (UIBroom) o;

        if (this.getBuildingCode() != null ? !this.getBuildingCode().equals(uiBroom.getBuildingCode()) : uiBroom.getBuildingCode() != null) return false;
        return this.getName() != null ? this.getName().equals(uiBroom.getName()) : uiBroom.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = this.getBuildingCode() != null ? this.getBuildingCode().hashCode() : 0;
        result = 31 * result + (this.getName() != null ? this.getName().hashCode() : 0);
        return result;
    }
}
