package com.pensive.android.romplanuib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pensive.android.romplanuib.io.BuildingCodeParser;
import com.pensive.android.romplanuib.io.BuildingParser;
import com.pensive.android.romplanuib.io.RoomParser;
import com.pensive.android.romplanuib.models.CalActivity;
import com.pensive.android.romplanuib.models.UIBbuilding;
import com.pensive.android.romplanuib.models.UIBroom;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EddiStat on 24.05.2016.
 */
public class DownloadAndStoreData {
    List<UIBbuilding> allBuildings;
    List<UIBroom> allRooms = new ArrayList<>();
    List<WeekViewEvent> weekViewEvents;



    public DownloadAndStoreData(){

        //Empty for now

    }

    public void setStoreWeekEventsTempDataInArray(List<WeekViewEvent> weekViewEvents){
        this.weekViewEvents = weekViewEvents;
    }

    public List<WeekViewEvent> getStoreWeekEventsTempDataInArray(){
            return weekViewEvents;
    }


    public void setStoreDataAllBuildings(Context context, List<UIBbuilding> temp) {
        System.out.println(temp);
        ArrayList<UIBbuilding> temp2 = new ArrayList<>(temp);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(temp2);
        System.out.println(json);
        editor.putString("all_buildings", json);
        editor.apply();
    }

    public List<UIBbuilding> getStoredDataAllBuildings(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("all_buildings", null);
        Type type = new TypeToken<List<UIBbuilding>>() {}.getType();
        List<UIBbuilding> arrayList = gson.fromJson(json, type);

        return arrayList;
    }


    public void setStoreDataAllRooms(Context context, List<UIBroom> temp) {
        System.out.println(temp);
        ArrayList<UIBroom> temp2 = new ArrayList<>(temp);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(temp2);
        System.out.println(json);
        editor.putString("all_rooms", json);
        editor.apply();
    }

    public List<UIBroom> getStoredDataAllRooms(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("all_rooms", null);
        Type type = new TypeToken<List<UIBroom>>() {}.getType();
        List<UIBroom> arrayList = gson.fromJson(json, type);

        return arrayList;
    }

    public List<UIBbuilding> getAllBuildings(){
        List<UIBbuilding> allBuildings ;
        List<UIBbuilding> allBuildingsTemp = new ArrayList<>();
        List<UIBroom> allRoomsTemp;
        UIBbuilding tempBuilding;
        try {
            BuildingParser buildingParser = new BuildingParser(
                    "http://rom.app.uib.no/ukesoversikt/?entry=byggrom");

            allBuildings = buildingParser.getBuildings();

            for (int i = 0; i < allBuildings.size(); i++ ){

                RoomParser roomParser = new RoomParser(
                        BuildingCodeParser.getBuildingURL(allBuildings.get(i).getName()), allBuildings.get(i).getName());
                allRoomsTemp = roomParser.getRooms();
                tempBuilding = new UIBbuilding(allBuildings.get(i).getName(), allRoomsTemp);

                allBuildingsTemp.add(tempBuilding);

            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("?????");
        }

        return allBuildingsTemp;
    }

    public List<UIBroom> getAllRoomsInUni(){
        List<UIBroom> allRoomsTemp;
        allBuildings = getAllBuildings();

        for (UIBbuilding building : getAllBuildings() ){

            try {
                RoomParser roomParser = new RoomParser(
                        BuildingCodeParser.getBuildingURL(building.getName()), building.getName());
                allRoomsTemp = roomParser.getRooms();

                for (UIBroom room : allRoomsTemp){
                    allRooms.add(room);
                    System.out.println(building.getName() + " : " + room.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return allRooms;
    }

    public Boolean isDataIsStored(Context context){
        if(getStoredDataAllRooms(context) == null && getStoredDataAllBuildings(context) == null){
            System.out.println("isDataStored returns false");
            return false;
        } else {
            System.out.println("isDataStored returns true");
            return true;
        }
    }
}
