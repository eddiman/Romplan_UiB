package com.pensive.android.romplanuib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Class for storing and retrieving stuff from sharedPreferences
 * @author Edvard P. Bjørgen
 */

public class DataManager {

    /**
     * Store a object in sharedPreferences
     * @param context Activity context
     * @param sharedPrefKey The key for where to store the object
     * @param objectToStore The object to store
     */
    public void storeObjectInSharedPref(Context context, String sharedPrefKey, Object objectToStore) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String serializedObject = gson.toJson(objectToStore);
        editor.putString(sharedPrefKey, serializedObject);
        editor.apply();
    }

    /**
     * Retrieve a object from sharedPreferences
     * @param context Activity context
     * @param sharedPrefKey The key for the object to retrieve
     * @param type The type of the object to retrieve
     * @param <GenericClass> The class of the object to retrieve
     * @return The object stored in sharedPreferences
     */
    public <GenericClass> GenericClass getSavedObjectFromSharedPref(Context context, String sharedPrefKey, Type type) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


        if (sharedPreferences.contains(sharedPrefKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(sharedPrefKey, ""), type);
        }

        return null;
    }

    /**
     * Delete a object from sharedPreferences
     * @param context Activity context
     * @param sharedPrefKey the key for the object to delete
     */
    public void deleteSavedObjectFromSharedPref(Context context, String sharedPrefKey){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(sharedPrefKey).apply();
    }

    public void updateSavedObjectInSharedPref(Context context, String sharedPrefKey, Object objectToStore){
        deleteSavedObjectFromSharedPref(context, sharedPrefKey);
        storeObjectInSharedPref(context, sharedPrefKey, objectToStore);
    }

    public boolean checkIfSharedPreferenceKeyExists(Context context, String sharedPrefKey){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.contains(sharedPrefKey);
    }
}
