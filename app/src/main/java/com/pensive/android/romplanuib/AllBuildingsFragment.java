package com.pensive.android.romplanuib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.pensive.android.romplanuib.arrayAdapters.BuildingAdapter;
import com.pensive.android.romplanuib.models.University;
import com.pensive.android.romplanuib.util.DataManager;

/**
 * A placeholder fragment containing a simple view.
 *
 * @author Edvard Bjørgen & Fredrik Heimsæter
 * @version 2.0
 */
public class AllBuildingsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    DataManager dataManager;
    String uniCampusCode;


    private static final String ARG_SECTION_NUMBER = "section_number";

    public AllBuildingsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AllBuildingsFragment newInstance(int sectionNumber) {
        AllBuildingsFragment fragment = new AllBuildingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_allbuildings, container, false);

        ListView favBuildListView = (ListView)rootView.findViewById(R.id.test_list);

        dataManager = new DataManager();
        University university = dataManager.getSavedObjectFromSharedPref(getContext(), "university", new TypeToken<University>(){}.getType());
        uniCampusCode = university.getCampusCode();

        BuildingAdapter adapter  = new BuildingAdapter(getActivity(), R.layout.list_building_element, university.getAllBuildings());
        favBuildListView.setAdapter(adapter);
        favBuildListView.setFastScrollEnabled(true);

        return rootView;
    }
}