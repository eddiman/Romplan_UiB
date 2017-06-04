package com.pensive.android.romplanuib.util;

/**
 * @author Edvard P. Bjørgen
 * @version 1.0
 */

public class ApiKeys {

    public String getApiKey(String campusCode){
        switch (campusCode){
            case "uib":
                return "Add your API-key here";
            case "uio":
                return "";
            default:
                return "";
        }

    }
}