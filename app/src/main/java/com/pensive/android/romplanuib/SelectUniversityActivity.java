package com.pensive.android.romplanuib;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.pensive.android.romplanuib.arrayAdapters.UniversityAdapter;
import com.pensive.android.romplanuib.models.University;
import com.pensive.android.romplanuib.util.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fredrik Heimsæter & Edvard Bjørgen
 * @version 2.0
 */
public class SelectUniversityActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DataManager dataManager;
    List<University> universityArray = new ArrayList<>();
    GridView uniCampusGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = new DataManager();
        setContentView(R.layout.activity_campus);
        toolbar = (Toolbar) findViewById(R.id.toolbar_campus);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        University university = dataManager.getSavedObjectFromSharedPref(this, "university", new TypeToken<University>(){}.getType());

        if (university != null && university.getAllBuildings() != null){
            Intent i = new Intent(this, BuildingMainActivity.class);
            this.startActivity(i);
        } else {
            generateAvailableCampus();
            initGui(this);
        }
    }

    private void generateAvailableCampus() {
        University uib = new University(getResources().getString(R.string.uni_bergen), "uib","@drawable/uib_logo_2",
                "@drawable/splash_uib_card", "UiB" );
        University uio = new University(getResources().getString(R.string.uni_oslo), "uio","@drawable/uio_logo_2",
                "@drawable/splash_uio_card", "UiO" );
        University uit = new University(getResources().getString(R.string.uni_tromso), "uit","@drawable/uit_logo",
                "@drawable/splash_uio_card", "UiT" );
        University oslomet = new University(getResources().getString(R.string.oslomet), "oslomet","@drawable/oslomet_logo",
                "@drawable/splash_uio_card", "OsloMet" );
        University ntnu = new University(getResources().getString(R.string.ntnu), "ntnu","@drawable/ntnu_logo",
                "@drawable/splash_uio_card", "NTNU" );
        universityArray.add(uib);
        universityArray.add(uio);
        universityArray.add(ntnu);
        universityArray.add(oslomet);
        universityArray.add(uit);

    }

    private void initGui(final Context context) {
        toolbar.setTitle("");
        uniCampusGrid = (GridView) findViewById(R.id.campusList);

        UniversityAdapter adapter  = new UniversityAdapter(context, R.layout.list_unicampus_layout, universityArray);

        uniCampusGrid.setAdapter(adapter);

    }
    public void onBackPressed() {
        this.finishAffinity();


    }





}
