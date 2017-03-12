package com.pensive.android.romplanuib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.pensive.android.romplanuib.Exceptions.DownloadException;
import com.pensive.android.romplanuib.models.UIBbuilding;
import com.pensive.android.romplanuib.models.UIBroom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DataManager takes care of downloading and storing data about buildings and rooms.
 *
 * @author Fredrik Heimsæter & Edvard Bjørgen
 * @version 2.0
 */
public class DataManager {
    List<UIBbuilding> allBuildings;
    List<UIBbuilding> favoriteBuildings;
    List<UIBroom> favoriteRoom;

    /**
     * If the data is already stored it loads it.
     * If not it downloads it.
     */
    public DataManager(Context context) {
        if(isDataStored(context)){
            this.allBuildings = loadBuildingData(context);
            this.favoriteBuildings = loadFavoriteBuildings(context);
            this.favoriteRoom = loadFavoriteRooms(context);
        }else{
            String error = "";
            try {
                List<String> areaIDs = downloadAreas();
                ArrayList<UIBbuilding> downloadedBuildings = new ArrayList<>();
                for (String ac : areaIDs) {
                    downloadedBuildings.addAll(downloadBuildingsInArea(ac));

                }
                Collections.sort(downloadedBuildings,new UiBBuildingComparator());
                this.allBuildings = downloadedBuildings;

            }catch (DownloadException downloadException){
                error += "download_error ";

            }
            storeData(context, error);
        }
    }

    /**
     * Downloads the list of areaIds
     * @return List of Ids
     */
    public List<String> downloadAreas(){
        List<String> areaIDs = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect("https://tp.data.uib.no/KEY.../ws/room/2.0/areas.php").ignoreContentType(true).get();
            JsonParser jsonParser = new JsonParser();
            JsonObject json = jsonParser.parse(doc.body().text()).getAsJsonObject();

            for(JsonElement area: json.getAsJsonArray("data")){
                areaIDs.add(area.getAsJsonObject().get("id").getAsString());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return areaIDs;
    }

    /**
     * Downloads information about buildings, and creates the buildingObjects
     * @param areaID the are to download buildings for
     * @return A List of buildings
     * @throws DownloadException
     */
    public List<UIBbuilding> downloadBuildingsInArea(String areaID) throws DownloadException{
        List<UIBbuilding> buildingsInArea = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://tp.data.uib.no/KEY.../ws/room/2.0/buildings.php?id="+areaID).ignoreContentType(true).get();
            JsonParser jsonParser = new JsonParser();
            JsonObject json = jsonParser.parse(doc.body().text()).getAsJsonObject();

            for(JsonElement buildingJson: json.getAsJsonArray("data")){
                String id = buildingJson.getAsJsonObject().get("id").getAsString();
                String name = buildingJson.getAsJsonObject().get("name").getAsString();
                List<UIBroom> roomsInBuilding = downloadRoomsInBuilding(areaID, id);
                UIBbuilding building = new UIBbuilding(areaID, id, name, roomsInBuilding);
                if(roomsInBuilding.size()>0) {
                    building.setBuildingAcronym(roomsInBuilding.get(0).getBuildingAcronym());
                }else{
                    building.setBuildingAcronym("??");
                }
                buildingsInArea.add(building);
                System.out.println(building);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return buildingsInArea;
    }

    /**
     * Downloads the rooms for a building
     * @param areaID the area the building is located in
     * @param buildingID the id of the building to get rooms for
     * @return a list of rooms
     * @throws DownloadException
     */
    public List<UIBroom> downloadRoomsInBuilding(String areaID, String buildingID) throws DownloadException{
        List<UIBroom> roomsInBuilding = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://tp.data.uib.no/KEY.../ws/room/2.0/rooms.php?id="+buildingID).ignoreContentType(true).get();
            JsonParser jsonParser = new JsonParser();
            JsonObject json = jsonParser.parse(doc.body().text()).getAsJsonObject();

            for(JsonElement roomJson: json.getAsJsonArray("data")){
                String id = roomJson.getAsJsonObject().get("id").getAsString();
                String name = roomJson.getAsJsonObject().get("name").getAsString();
                String type = roomJson.getAsJsonObject().get("typeid").getAsString();
                int size = roomJson.getAsJsonObject().get("size").getAsInt();

                doc = Jsoup.connect("https://tp.data.uib.no/KEY.../ws/room/2.0/?id="+id).ignoreContentType(true).get();
                json = jsonParser.parse(doc.body().text()).getAsJsonObject().get("data").getAsJsonObject();
                String buildingAcronym = json.get("buildingacronym").getAsString();
                String imageURL;
                try {
                    imageURL = json.get("roomimg_url").getAsString();
                }catch (UnsupportedOperationException e){
                    imageURL = "defaultImage";//Todo fix
                }
                UIBroom room = new UIBroom(areaID, buildingID, id, name, type, size);
                room.setBuildingAcronym(buildingAcronym);
                room.setImageURL(imageURL);
                roomsInBuilding.add(room);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return roomsInBuilding;
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

        String allBuildingsString = gson.toJson(allBuildings);
        String favoriteBuildingsString = gson.toJson(favoriteBuildings);
        String favoriteRoomsString = gson.toJson(favoriteRoom);
        editor.putString("error_v2", error);
        editor.putString("all_buildings_v2",allBuildingsString);
        editor.putString("favorite_buildings_v2",favoriteBuildingsString);
        editor.putString("favorite_rooms_v2",favoriteRoomsString);
        editor.apply();
    }


    /**
     * Loads the building data from the storage.
     * @param context the context of the activity
     * @return a list of {@link UIBbuilding}s
     */
    public List<UIBbuilding> loadBuildingData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("all_buildings_v2", null);
        Type type = new TypeToken<List<UIBbuilding>>(){}.getType();
        List<UIBbuilding> buildings = gson.fromJson(json,type);
        return buildings;
    }

    public List<UIBbuilding> loadFavoriteBuildings(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite_buildings_v2",null);
        Type type = new TypeToken<List<UIBbuilding>>(){}.getType();
        List<UIBbuilding> favorites = gson.fromJson(json,type);
        if (favorites==null){
            favorites = new ArrayList<>();
        }
        return favorites;
    }
    public List<UIBroom> loadFavoriteRooms(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite_rooms_v2",null);
        Type type = new TypeToken<List<UIBroom>>(){}.getType();
        List<UIBroom> favorites = gson.fromJson(json,type);
        if (favorites==null){
            favorites = new ArrayList<>();
        }
        return favorites;
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
        if(checkError(context) || loadBuildingData(context)==null){
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
        String error = sharedPreferences.getString("error_v2", null);
        return error != null && !error.equals("none");
    }

    public void addFavoriteBuilding(UIBbuilding newFavoriteBuilding,Context context) {
        this.favoriteBuildings.add(newFavoriteBuilding);
        storeData(context,"");
    }
    public void removeFavoriteBuilding(UIBbuilding oldFavoriteBuilding,Context context){
        this.favoriteBuildings.remove(oldFavoriteBuilding);
        storeData(context,"");
    }

    public void addFavoriteRoom(UIBroom newFavoriteRoom,Context context) {
        this.favoriteRoom.add(newFavoriteRoom);
        storeData(context, "");
    }

    public void removeFavoriteRoom(UIBroom oldFavoriteRoom, Context context){
        this.favoriteRoom.remove(oldFavoriteRoom);
        storeData(context,"");
    }

    public List<UIBbuilding> getFavoriteBuildings(){
        return favoriteBuildings;
    }

    public List<UIBroom> getFavoriteRoom() {
        return favoriteRoom;
    }
}
