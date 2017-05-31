package com.pensive.android.romplanuib.models;

/**
 * Class for making a university campus object
 *
 * @author Edvard Pires Bjørgen
 * @version 2.0
 *
 */
public class UniCampus {
    private String name;
    private String campusCode;
    private String logoUrl;
    private String bgUrl;
    private String acronym;

    /**
     *
     * @param name the full name of the campus
     * @param campusCode the lowercase lettering for use in API
     * @param logoUrl url of logo
     * @param bgUrl url of splash screen in loading
     * @param acronym actual acronym NB: NOT to be confused with campusCode, this is for use only in GUI
     */
    public UniCampus(String name, String campusCode, String logoUrl, String bgUrl, String acronym) {
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
}
