package com.pensive.android.romplanuib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pensive.android.romplanuib.ArrayAdapters.BuildingAdapter;
import com.pensive.android.romplanuib.models.UIBbuilding;
import com.pensive.android.romplanuib.util.DataManager;
import com.pensive.android.romplanuib.util.UiBBuildingComparator;

import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 * TODO: Implement favorites
 *
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class FavoritesFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */


    private static final String ARG_SECTION_NUMBER = "section_number";
    DataManager dataManager;

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
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        ListView allBuildListView = (ListView)rootView.findViewById(R.id.fav_list);
        dataManager = new DataManager(rootView.getContext());
        if(dataManager.getFavoriteBuildings().size()>0) {
            System.out.println("crap " + dataManager.getFavoriteBuildings());
            List<UIBbuilding> favBuilds = dataManager.getFavoriteBuildings();
            Collections.sort(favBuilds,new UiBBuildingComparator());
            BuildingAdapter adapter = new BuildingAdapter(getActivity(), R.layout.list_building_element, favBuilds);

            allBuildListView.setAdapter(adapter);

            allBuildListView.setFastScrollEnabled(true);
        }
        return rootView;
    }
}