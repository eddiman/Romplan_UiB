package com.pensive.android.romplanuib.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for making a university campus object
 *
 * @author Edvard Pires Bjørgen & Fredrik Heimsæter
 * @version 3.0
 *
 */
public class University {
    private String name;
    private String campusCode;
    private String logoUrl;
    private String bgUrl;
    private String acronym;
    private List<Area> areas;


    /**
     *
     * @param name the full name of the campus
     * @param campusCode the lowercase lettering for use in API
     * @param logoUrl url of logo
     * @param bgUrl url of splash screen in loading
     * @param acronym actual acronym NB: NOT to be confused with campusCode, this is for use only in GUI
     */
    public University(String name, String campusCode, String logoUrl, String bgUrl, String acronym) {
        this.name = name;
        this.campusCode = campusCode;
        this.logoUrl = logoUrl;
        this.bgUrl = bgUrl;
        this.acronym = acronym;

    }


    public String getName() {
        return name;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public String getAcronym() {
        return acronym;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<Building> getAllBuildings(){
        ArrayList<Building> buildings = new ArrayList<>();
        for(Area area : areas){
            buildings.addAll(area.getBuildings());
        }
        return buildings;
    }
}
