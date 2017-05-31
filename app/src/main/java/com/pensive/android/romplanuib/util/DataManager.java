package com.pensive.android.romplanuib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import com.pensive.android.romplanuib.Exceptions.DownloadException;
import com.pensive.android.romplanuib.models.Building;
import com.pensive.android.romplanuib.models.CalActivity;
import com.pensive.android.romplanuib.models.Room;
import com.pensive.android.romplanuib.models.UniCampus;

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
    private List<Building> allBuildings;
    private List<Building> favoriteBuildings;
    private List<Room> favoriteRoom;
    private ApiKeys api = new ApiKeys();
    private String uniCampusCode;
    private ApiUrls apiUrls = new ApiUrls();
    private Context context;

    /**
     * If the data is already stored it loads it.
     * If not it downloads it.
     */
    public DataManager(Context context) {
        this.context = context;

    }

    /**
     * Checks if all data has been downloaded, if not downloads the data from all areaes in uni campus, if not "uib" downloads from specified area code
     * @param uniCampusCode university campus,
     *
     */
    public void checkIfDataHasBeenLoadedBefore(String uniCampusCode) {
        if(isDataStored(context)){
            this.allBuildings = loadBuildingData(context);
            this.favoriteBuildings = loadFavoriteBuildings(context);
            this.favoriteRoom = loadFavoriteRooms(context);
        }else if(uniCampusCode.equals("uib")){
            downloadAllBuildingsInUniCampus();
        } else{
            //TODO: have method to take in a List of areas to download, for multiple selections of areas
            downloadAllBuildingsInCampusArea(getSelectedAreaCode(context));
        }
    }

    /**
     *
     * Downloads all buildings in a specific area
     * @param areaCode the area to download from
     */
    public void downloadAllBuildingsInCampusArea(String areaCode) {

        String error = "";
        try {
            ArrayList<Building> downloadedBuildings = new ArrayList<>();
            downloadedBuildings.addAll(downloadBuildingsInArea(areaCode));

            Collections.sort(downloadedBuildings,new BuildingComparator());
            this.allBuildings = downloadedBuildings;

        }catch (DownloadException downloadException){
            error += "download_error";

        }
        storeData(context, error);

    }

    private void downloadAllBuildingsInUniCampus() {
        uniCampusCode = loadCurrentUniCampusSharedPref().getCampusCode();
        String error = "";
        try {
            List<String> areaIDs = downloadAreas(uniCampusCode);
            ArrayList<Building> downloadedBuildings = new ArrayList<>();
            for (String ac : areaIDs) {
                downloadedBuildings.addAll(downloadBuildingsInArea(ac));

            }
            Collections.sort(downloadedBuildings,new BuildingComparator());
            this.allBuildings = downloadedBuildings;

        }catch (DownloadException downloadException){
            error += "download_error ";

        }
        storeData(context, error);
    }


    /**
     * Downloads the list of areaIds
     * @return List of Ids
     */
    public List<String> downloadAreas(String uniCampusCode){
        List<String> areaIDs = new ArrayList<>();
        Document doc = null;
        try {
            //TODO: Endre dette til noe mer generaliserbart, men nå er det kun uib som vi har tilgang til tp-api
            if(uniCampusCode.equals("uib")){
                doc = Jsoup.connect(apiUrls.getApiUrl(uniCampusCode) + api.getApiKey(uniCampusCode) + "/ws/room/2.0/areas.php").ignoreContentType(true).get();}
            else {
                doc = Jsoup.connect(apiUrls.getApiUrl(uniCampusCode) + "areas/" + uniCampusCode).ignoreContentType(true).get();
            }
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
    public List<Building> downloadBuildingsInArea(String areaID) throws DownloadException{
        uniCampusCode = loadCurrentUniCampusSharedPref().getCampusCode();

        List<Building> buildingsInArea = new ArrayList<>();
        try {
            Document doc;
            //TODO: Endre dette til noe mer generaliserbart, men nå er det kun uib som vi har tilgang til tp
            if(uniCampusCode.equals("uib")){
                doc = Jsoup.connect(apiUrls.getApiUrl(uniCampusCode) + api.getApiKey(uniCampusCode) + "/ws/room/2.0/buildings.php?id="+areaID).ignoreContentType(true).get();}
            else {
                doc = Jsoup.connect(apiUrls.getApiUrl(uniCampusCode) + "buildings/" + uniCampusCode + "/" + areaID).ignoreContentType(true).get();
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject json = jsonParser.parse(doc.body().text()).getAsJsonObject();

            for(JsonElement buildingJson: json.getAsJsonArray("data")){
                String id = buildingJson.getAsJsonObject().get("id").getAsString();
                String name = buildingJson.getAsJsonObject().get("name").getAsString();
                List<Room> roomsInBuilding = downloadRoomsInBuilding(areaID, id);
                Building building = new Building(areaID, id, name, roomsInBuilding);
                if(roomsInBuilding.size()>0) {
                    building.setBuildingAcronym(roomsInBuilding.get(0).getBuildingAcronym());
                }else{
                    building.setBuildingAcronym(uniCampusCode.toUpperCase());
                }
                buildingsInArea.add(building);
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
    public List<Room> downloadRoomsInBuilding(String areaID, String buildingID) throws DownloadException{
        uniCampusCode = loadCurrentUniCampusSharedPref().getCampusCode();

        List<Room> roomsInBuilding = new ArrayList<>();
        try {
            Document doc;
            //TODO: Endre dette til noe mer generaliserbart, men nå er det kun uib som vi har tilgang til tp
            if (uniCampusCode.equals("uib")){
                doc = Jsoup.connect(apiUrls.getApiUrl(uniCampusCode) + api.getApiKey(uniCampusCode) + "/ws/room/2.0/rooms.php?id=" + buildingID).ignoreContentType(true).get();}
            else {
                doc = Jsoup.connect(apiUrls.getApiUrl(uniCampusCode) + "rooms/" + uniCampusCode + "/" + areaID + "/" + buildingID).ignoreContentType(true).get();

            }
            JsonParser jsonParser = new JsonParser();
            JsonObject json = jsonParser.parse(doc.body().text()).getAsJsonObject();

            for(JsonElement roomJson: json.getAsJsonArray("data")){
                String id = roomJson.getAsJsonObject().get("id").getAsString();
                String name = roomJson.getAsJsonObject().get("name").getAsString();
                String type = roomJson.getAsJsonObject().get("typeid").getAsString();

                int size = roomJson.getAsJsonObject().get("size").getAsInt();



                if (uniCampusCode.equals("uib")){
                    doc = Jsoup.connect(apiUrls.getApiUrl(uniCampusCode) + api.getApiKey(uniCampusCode) + "/ws/room/2.0/?id=" + id).ignoreContentType(true).get();}
                else {
                    doc = Jsoup.connect(apiUrls.getApiUrl(uniCampusCode) + "rooms/" + uniCampusCode + "/" + areaID + "/" + buildingID + "/" + id).ignoreContentType(true).get();

                }
                Log.d("ROOM", "area: " + areaID + " building: " + buildingID +" room id:" + id + " room:" + name);

                //TODO: Hvis try'en her fjernes, får en funky error, ingen anlse hva det kan være:
                //com.google.gson.stream.MalformedJsonException: Unterminated object at line 1 column 346 path $.data.description
                try{
                    json = jsonParser.parse(doc.body().text()).getAsJsonObject().get("data").getAsJsonObject();
                    String buildingAcronym = json.get("buildingacronym").getAsString();
                    String imageURL;
                    try {
                        imageURL = json.get("roomimg_url").getAsString();
                    }catch (UnsupportedOperationException e){
                        imageURL = "defaultImage";//Todo fix
                    }


                    Room room = new Room(areaID, buildingID, id, name, type, size);
                    room.setBuildingAcronym(buildingAcronym);
                    room.setImageURL(imageURL);
                    roomsInBuilding.add(room);}
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return roomsInBuilding;
    }

    public List<CalActivity> fetchCalendarActivities(String roomID, String fromDate, String toDate){
        uniCampusCode = loadCurrentUniCampusSharedPref().getCampusCode();
        List<CalActivity> calActivities = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(apiUrls.getApiUrl(uniCampusCode) + api.getApiKey(uniCampusCode) + "/ws/1.4/room.php?id=" + roomID + "&fromdate=" + fromDate + "&todate=" + toDate + "&lang=nn").ignoreContentType(true).get();
            JsonParser jsonParser = new JsonParser();
            JsonObject json = jsonParser.parse(doc.body().text()).getAsJsonObject();

            for(JsonElement event: json.getAsJsonArray("events")){
                String courseID = event.getAsJsonObject().get("courseid").getAsString();
                int weekNumber = event.getAsJsonObject().get("weeknr").getAsInt();
                String teachingMethod = event.getAsJsonObject().get("teaching-method").getAsString();
                String teachingMethodName = event.getAsJsonObject().get("teaching-method-name").getAsString();
                String teachingTitle = event.getAsJsonObject().get("teaching-title").getAsString();
                String beginTime = event.getAsJsonObject().get("dtstart").getAsString();
                String endTime = event.getAsJsonObject().get("dtend").getAsString();
                String summary = event.getAsJsonObject().get("summary").getAsString();
                CalActivity calActivity = new CalActivity(courseID, weekNumber, teachingMethod, teachingMethodName, teachingTitle, beginTime, endTime, summary);
                calActivities.add(calActivity);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return calActivities;
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
     * @return a list of {@link Building}s
     */
    public List<Building> loadBuildingData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("all_buildings_v2", null);
        Type type = new TypeToken<List<Building>>(){}.getType();
        List<Building> buildings = gson.fromJson(json,type);
        return buildings;
    }

    public List<Building> loadFavoriteBuildings(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite_buildings_v2",null);
        Type type = new TypeToken<List<Building>>(){}.getType();
        List<Building> favorites = gson.fromJson(json,type);
        if (favorites==null){
            favorites = new ArrayList<>();
        }
        return favorites;
    }
    public List<Room> loadFavoriteRooms(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite_rooms_v2",null);
        Type type = new TypeToken<List<Room>>(){}.getType();
        List<Room> favorites = gson.fromJson(json,type);
        if (favorites == null){
            favorites = new ArrayList<>();
        }
        return favorites;
    }

    /**
     * Gets all buildings.
     * @return a list of all buildings.
     */
    public List<Building> getAllBuildings() {
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

    public void addFavoriteBuilding(Building newFavoriteBuilding, Context context) {
        this.favoriteBuildings.add(newFavoriteBuilding);
        storeData(context,"");
    }
    public void removeFavoriteBuilding(Building oldFavoriteBuilding, Context context){
        this.favoriteBuildings.remove(oldFavoriteBuilding);
        storeData(context,"");
    }

    public void addFavoriteRoom(Room newFavoriteRoom, Context context) {
        this.favoriteRoom.add(newFavoriteRoom);
        storeData(context, "");
    }

    public void removeFavoriteRoom(Room oldFavoriteRoom, Context context){
        this.favoriteRoom.remove(oldFavoriteRoom);
        storeData(context,"");
    }

    public List<Building> getFavoriteBuildings(){
        return favoriteBuildings;
    }

    public List<Room> getFavoriteRoom() {
        return favoriteRoom;
    }

    /**
     * Writes an area code in to sharedPref for future and easier usage.
     * @param areaCode
     */
    public void setSelectedAreaCode(String areaCode) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("area_code", areaCode);
        editor.apply();

    }

    public String getSelectedAreaCode(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("area_code", null);
    }

    /**
     * Writes the selected university as an object ,into the shared pref for storing.
     *
     * @param uniCampus the campus to be written in
     */
    public void writeCurrentUniCampusSharedPref(UniCampus uniCampus){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String uniCampusString = gson.toJson(uniCampus);
        editor.putString("current_campus", uniCampusString);
        editor.apply();
    }

    /**
     * Loads the selected university as an object ,into the shared pref for storing.
     *
     * @return  The saved campus
     */
    public UniCampus loadCurrentUniCampusSharedPref(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("current_campus",null);
        Type type = new TypeToken<UniCampus>(){}.getType();

        return gson.fromJson(json,type);
    }
}