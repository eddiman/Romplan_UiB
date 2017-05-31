package com.pensive.android.romplanuib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.widget.GridView;

import com.pensive.android.romplanuib.ArrayAdapters.UniCampusAdapter;
import com.pensive.android.romplanuib.models.UniCampus;

import java.util.ArrayList;
import java.util.List;

public class SelectUniCampusActivity extends AppCompatActivity {
    String selectedCampusString;
    private Toolbar toolbar;
    List<UniCampus> uniCampusArray = new ArrayList<>();
    GridView uniCampusGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);
        toolbar = (Toolbar) findViewById(R.id.toolbar_campus);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        selectedCampusString = sharedPreferences.getString("current_campus", null);
        checkIfCampusSelect();

        generateAvailableCampus();

        initGui(this);

    }

    private void checkIfCampusSelect() {
        if (selectedCampusString == null){
            initGui(this);
        } else{
            Intent i = new Intent(this, LoadActivity.class);
            this.startActivity(i);
        }
    }


    private void generateAvailableCampus() {
        UniCampus uib = new UniCampus(getResources().getString(R.string.uni_bergen), "uib","@drawable/uib_logo_2",
                "@drawable/splash_uib_card", "UiB" );
        UniCampus uio = new UniCampus(getResources().getString(R.string.uni_oslo), "uio","@drawable/uio_logo_2",
                "@drawable/splash_uio_card", "UiO" );
        uniCampusArray.add(uib);
        uniCampusArray.add(uio);

    }

    private void initGui(final Context context) {
        toolbar.setTitle("Romplan");
        uniCampusGrid = (GridView) findViewById(R.id.campusList);

        UniCampusAdapter adapter  = new UniCampusAdapter(context, R.layout.list_unicampus_layout, uniCampusArray);

        uniCampusGrid.setAdapter(adapter);

    }
    public void onBackPressed() {
        this.finishAffinity();


    }





}
