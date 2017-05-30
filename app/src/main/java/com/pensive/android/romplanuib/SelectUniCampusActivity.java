package com.pensive.android.romplanuib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pensive.android.romplanuib.ArrayAdapters.BuildingAdapter;
import com.pensive.android.romplanuib.ArrayAdapters.CampusAdapter;
import com.pensive.android.romplanuib.models.Campus;

import java.util.ArrayList;
import java.util.List;

public class SelectCampusActivity extends AppCompatActivity {
    String campusCode;
    private Toolbar toolbar;
    List<Campus> campusArray = new ArrayList<>();
    GridView campusGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);
        toolbar = (Toolbar) findViewById(R.id.toolbar_campus);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        campusCode = sharedPreferences.getString("campus_code", null);
        checkIfCampusSelect();

        generateAvailableCampus();

        initGui(this);

    }

    private void checkIfCampusSelect() {
        if (campusCode == null){
            initGui(this);
        } else{
            Intent i = new Intent(this, LoadActivity.class);
            this.startActivity(i);
        }
    }


    private void generateAvailableCampus() {
        Campus uib = new Campus(getResources().getString(R.string.uni_bergen), "uib");
        Campus uio = new Campus(getResources().getString(R.string.uni_oslo), "uio");
        campusArray.add(uib);
        campusArray.add(uio);

    }

    private void initGui(final Context context) {
        toolbar.setTitle("Romplan");
        campusGrid = (GridView) findViewById(R.id.campusList);

        CampusAdapter adapter  = new CampusAdapter(context, R.layout.list_campus_layout, campusArray);

        campusGrid.setAdapter(adapter);

    }



}
