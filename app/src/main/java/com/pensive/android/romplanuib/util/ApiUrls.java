package com.pensive.android.romplanuib.util;

/**
 * Created by moled on 21.05.2017.
 */

public class ApiUrls {

    public String getApiUrl(String campusCode){

        switch (campusCode){
            case "uib":
                return "https://tp.data.uib.no/";
        default:
            return "kkkukk";
        }

    }

}
