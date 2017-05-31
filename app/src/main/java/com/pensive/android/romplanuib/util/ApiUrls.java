package com.pensive.android.romplanuib.util;

/**
 * Created by moled on 21.05.2017.
 */

public class ApiUrls {

    ApiKeys key = new ApiKeys();


    public String getApiUrl(String campusCode){

        switch (campusCode){
            case "uib":
                return "https://tp.data.uib.no/";

            case "uio":

                return "http://tp.freheims.xyz/api/";
        default:
            return "";
        }

    }

    public String getTpApiUrl(String type){

        switch (type){
            case "uib-areas":
                return "/ws/room/2.0/areas.php";

            case "uib-buildings":
                return "/ws/room/2.0/buildings.php?id=";

        default:
            return "";
        }

    }

}
