package com.pensive.android.romplanuib.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Fredrik Heimsæter
 * @version 1.0
 */
public class Area extends Unit {
    @SerializedName("buildings")
    private List<Building> buildings;

    public Area(String universityID, String areaID, String name) {
        super(universityID, areaID, name);
    }

    public List<Building> getBuildings() {
        return buildings;
    }
}
