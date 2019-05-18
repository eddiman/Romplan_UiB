package com.pensive.android.romplanuib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.pensive.android.romplanuib.arrayAdapters.FavoriteAdapter;
import com.pensive.android.romplanuib.models.Building;
import com.pensive.android.romplanuib.models.Room;
import com.pensive.android.romplanuib.models.Unit;
import com.pensive.android.romplanuib.models.University;
import com.pensive.android.romplanuib.util.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment containing views for the favorites.
 *
 * @author Edvard Bjørgen & Fredrik Heimsæter
 * @version 2.0
 */
public class FavoritesFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    DataManager dataManager;
    private FavoriteAdapter adapter;
    private View rootView;
    private ListView allFavoritesListView;
    private ArrayList<Unit> favorites;
    String uniCampusCode;

    public FavoritesFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FavoritesFragment newInstance(int sectionNumber) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        allFavoritesListView = (ListView)rootView.findViewById(R.id.fav_list);

        dataManager = new DataManager();
        University university = dataManager.getSavedObjectFromSharedPref(getContext(), "university", new TypeToken<University>(){}.getType());
        uniCampusCode = university.getCampusCode();

        if(dataManager.checkIfSharedPreferenceKeyExists(getContext(), "favorite_buildings") || dataManager.checkIfSharedPreferenceKeyExists(getContext(), "favorite_rooms") ){
            List<Building> favBuilds = dataManager.getSavedObjectFromSharedPref(getContext(), "favorite_buildings", new TypeToken<List<Building>>(){}.getType());
            List<Room> favRooms = dataManager.getSavedObjectFromSharedPref(getContext(), "favorite_rooms", new TypeToken<List<Building>>(){}.getType());
            List<Unit> favorites;
            if(favBuilds != null) {
                favorites = new ArrayList<Unit>(favBuilds);
            }else {
                favorites = new ArrayList<Unit>();
            }
            if (favRooms != null) {
                favorites.addAll(favRooms);
            }

            adapter = new FavoriteAdapter(getActivity(), R.layout.list_favorite_element, favorites);
            allFavoritesListView.setAdapter(adapter);
            allFavoritesListView.setFastScrollEnabled(true);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        allFavoritesListView.setAdapter(null);
        dataManager = new DataManager();

        if(dataManager.checkIfSharedPreferenceKeyExists(getContext(), "favorite_buildings") || dataManager.checkIfSharedPreferenceKeyExists(getContext(), "favorite_rooms") ){
            List<Building> favBuilds = dataManager.getSavedObjectFromSharedPref(getContext(), "favorite_buildings", new TypeToken<List<Building>>(){}.getType());
            List<Room> favRooms = dataManager.getSavedObjectFromSharedPref(getContext(), "favorite_rooms", new TypeToken<List<Building>>(){}.getType());
            List<Unit> favorites;
            if(favBuilds != null) {
                favorites = new ArrayList<Unit>(favBuilds);
            }else {
                favorites = new ArrayList<Unit>();
            }
            if (favRooms != null) {
                favorites.addAll(favRooms);
            }

            adapter = new FavoriteAdapter(getActivity(), R.layout.list_favorite_element, favorites);
            allFavoritesListView.setAdapter(adapter);
            allFavoritesListView.setFastScrollEnabled(true);
            adapter.notifyDataSetChanged();
            allFavoritesListView.refreshDrawableState();
        }
    }

    public void updateListView() {
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

}