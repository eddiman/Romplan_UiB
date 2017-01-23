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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UIBroom uiBroom = (UIBroom) o;

        if (code != null ? !code.equals(uiBroom.code) : uiBroom.code != null) return false;
        return name != null ? name.equals(uiBroom.name) : uiBroom.name == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
