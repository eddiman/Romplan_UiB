package com.pensive.android.romplanuib.util;

/**
 * Created by EddiStat on 05.06.2016.
 */
public class StringCleaner {

    public String createBuildingName(String buildingName) {
        String tempName = buildingName;
        tempName = tempName.replaceAll("(^[^:]+:.)", "");
        tempName = tempName.substring(1);
        return tempName;
    }

    public String createBuildingCode(String buildingName) {
        String tempCode = buildingName;
        tempCode = tempCode.replaceAll("(:.*)", "");
        tempCode = tempCode.substring(1);
        return tempCode;
    }

}
