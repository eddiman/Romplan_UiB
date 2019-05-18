package com.pensive.android.romplanuib.util;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.pensive.android.romplanuib.R;
import com.pensive.android.romplanuib.models.Building;
import com.pensive.android.romplanuib.models.Room;
import com.pensive.android.romplanuib.models.University;

import java.util.ArrayList;
import java.util.List;

public class FavoriteHandler {

    public boolean isBuildingInFavorites(Context context, Building building){
        DataManager dataManager = new DataManager();
        if(dataManager.checkIfSharedPreferenceKeyExists(context, "favorite_buildings")) {
            return getFavoriteBuildings(context).contains(building);
        }
        return false;
    }

    public boolean isRoomInFavorites(Context context, Room room){
        DataManager dataManager = new DataManager();
        if(dataManager.checkIfSharedPreferenceKeyExists(context, "favorite_rooms")) {
            return getFavoriteRooms(context).contains(room);
        }
        return false;
    }

    public void addBuildingToFavorites(Context context, Building building){
        List<Building> favorite_buildings;
        DataManager dataManager = new DataManager();
        if(dataManager.checkIfSharedPreferenceKeyExists(context, "favorite_buildings")) {
            favorite_buildings = getFavoriteBuildings(context);
        }else{
            favorite_buildings = new ArrayList<>();
        }
        favorite_buildings.add(building);
        dataManager.storeObjectInSharedPref(context, "favorite_buildings", favorite_buildings);
    }

    public void addRoomToFavorites(Context context, Room room){
        List<Room> favorite_rooms;
        DataManager dataManager = new DataManager();
        if(dataManager.checkIfSharedPreferenceKeyExists(context, "favorite_rooms")) {
            favorite_rooms = getFavoriteRooms(context);
        }else{
            favorite_rooms = new ArrayList<>();
        }
        favorite_rooms.add(room);
        dataManager.storeObjectInSharedPref(context, "favorite_rooms", favorite_rooms);

    }

    public void removeBuildingFromFavorites(Context context, Building building){
        DataManager dataManager = new DataManager();
        List<Building> favorite_buildings = getFavoriteBuildings(context);
        favorite_buildings.remove(building);
        dataManager.updateSavedObjectInSharedPref(context, "favorite_buildings", favorite_buildings);
    }

    public void removeRoomFromFavorites(Context context, Room room){
        DataManager dataManager = new DataManager();
        List<Room> favorite_rooms = getFavoriteRooms(context);
        favorite_rooms.remove(room);
        dataManager.updateSavedObjectInSharedPref(context, "favorite_rooms", favorite_rooms);
    }

    public List<Building> getFavoriteBuildings(Context context){
        DataManager dataManager = new DataManager();
        List<Building> favorite_buildings = dataManager.getSavedObjectFromSharedPref(context, "favorite_buildings", new TypeToken<List<Building>>(){}.getType());
        return favorite_buildings;
    }

    public List<Room> getFavoriteRooms(Context context){
        DataManager dataManager = new DataManager();
        List<Room> favorite_rooms = dataManager.getSavedObjectFromSharedPref(context, "favorite_rooms", new TypeToken<List<Room>>(){}.getType());
        return favorite_rooms;
    }

}
