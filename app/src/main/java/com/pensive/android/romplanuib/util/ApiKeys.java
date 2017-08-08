package com.pensive.android.romplanuib.util;

/**
 * @author Edvard P. Bjørgen
>>>>>>> 173552467a0732234dcc0b4e658cc4ed8b00cd99
 * @version 1.0
 */

public class ApiKeys {

    public String getApiKey(String campusCode){
        switch (campusCode){
            case "uib":
                return "KEY6ytu6esu9";
            case "uio":
                return "";
            default:
                return "";
        }

    }
}