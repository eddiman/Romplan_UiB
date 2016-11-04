package com.pensive.android.romplanuib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pensive.android.romplanuib.Exceptions.DownloadException;
import com.pensive.android.romplanuib.io.BuildingCodeParser;
import com.pensive.android.romplanuib.io.BuildingParser;
import com.pensive.android.romplanuib.io.RoomParser;
import com.pensive.android.romplanuib.models.UIBbuilding;
import com.pensive.android.romplanuib.models.UIBroom;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DataManager takes care of downloading and storing data about buildings and rooms.
 */

public class DataManager {
    List<UIBbuilding> allBuildings;

    /**
     * If the data is already stored it loads it.
     * If not it downloads it.
     */
    public DataManager(Context context) {
        if(isDataStored(context)){
            this.allBuildings = loadData(context);
        }else{
            String error = "";
            try {
                List<String> buildingNames = downloadBuildings();
                ArrayList<UIBbuilding> downloadedBuildings = new ArrayList<>();
                for (String name : buildingNames) {
                    List<UIBroom> rooms = downloadRooms(name);
                    UIBbuilding building = new UIBbuilding(name, rooms);
                    downloadedBuildings.add(building);
                }
                Collections.sort(downloadedBuildings,new UiBBuildingComparator());
                this.allBuildings = downloadedBuildings;

            }catch (DownloadException downloadException){
                error += "download_error ";

            }
            storeData(context, error);
        }
        System.out.println("Succsess");

    }

    /**
     * Downloads the name off all buildings
     * @return a list of buildingnames
     */
    public List<String> downloadBuildings() throws DownloadException{
        List<String> buildingNames;
        try {
            BuildingParser buildingParser = new BuildingParser("http://rom.app.uib.no/ukesoversikt/?entry=byggrom");
            buildingNames = buildingParser.getBuildings();
        }catch (IOException e){
            throw new DownloadException();
        }
        return buildingNames;
    }

    /**
     * Downloads the rooms for a building
     * @param buildingName the name of the building to fetch rooms for
     * @return a list of rooms
     */
    public List<UIBroom> downloadRooms(String buildingName) throws DownloadException{
        List<UIBroom> rooms;
        try{
            String buildingURL = BuildingCodeParser.getBuildingURL(buildingName);
            RoomParser roomParser = new RoomParser(buildingURL,buildingName);
            rooms = roomParser.getRooms();
        }catch (IOException e){
            throw new DownloadException();
        }
        return rooms;
    }

    /**
     * Stores all the data.
     * @param context the context of the activity
     * @param error error message if any
     */
    public void storeData(Context context, String error){
        if (error.equals("")) error = "none";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(allBuildings);
        editor.putString("error", error);
        editor.putString("all_buildings",json);
        editor.apply();
    }


    /**
     * Loads the data from the storage.
     * @param context the context of the activity
     * @return a list of {@link UIBbuilding}s
     */
    public List<UIBbuilding> loadData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("all_buildings", null);
        Type type = new TypeToken<List<UIBbuilding>>(){}.getType();
        List<UIBbuilding> buildings = gson.fromJson(json,type);
        return buildings;
    }

    /**
     * Gets all buildings.
     * @return a list of all buildings.
     */
    public List<UIBbuilding> getAllBuildings() {
        return allBuildings;
    }

    /**
     * Check if data already is stored.
     * @return true if data is stored, false if it isn't
     */
    public boolean isDataStored(Context context){
        if(checkError(context) || loadData(context)==null){
            return false;
        }else {
            return true;
        }
    }

    /**
     * Checks the storage for errors
     * @return true if error exists, else false
     */
    private boolean checkError(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("error", null);
        Type type = new TypeToken<String>(){}.getType();
        String error = gson.fromJson(json,type);


        return error != null && !error.equals("none");
    }
}
