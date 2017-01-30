package com.pensive.android.romplanuib.models;

import java.io.Serializable;

/**
 * Created by fredrik on 1/26/17.
 */

public abstract class UiBunit implements Serializable{
    private String buildingCode;
    private String name;

    public UiBunit(String buildingCode, String name) {
        this.buildingCode = buildingCode;
        this.name = name;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
