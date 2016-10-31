package com.pensive.android.romplanuib;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.pensive.android.romplanuib.ArrayAdapters.RoomAdapter;
import com.pensive.android.romplanuib.io.util.URLEncoding;
import com.pensive.android.romplanuib.models.UIBbuilding;
import com.pensive.android.romplanuib.models.UIBroom;
import com.pensive.android.romplanuib.util.StringCleaner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RoomActivity extends AppCompatActivity {

    UIBbuilding building;
    List<UIBroom> errorList = new ArrayList<>();
    String buildingName;
    String buildingCode;
    ListView roomList;

    StringCleaner sc = new StringCleaner();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        building = getBuildingFromLastActivity();
        buildingCode = sc.createBuildingCode(building.getName());
        buildingName = sc.createBuildingName(building.getName());


        //GUI elements
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(collapsingToolbar != null){collapsingToolbar.setTitle(buildingName);}
        loadBackdrop();
        floatingActionButtonListener();

        getWindow().setStatusBarColor(ContextCompat.getColor(RoomActivity.this, R.color.transpBlack));

        roomList = (ListView) findViewById(R.id.room_listView);

        RoomAdapter adapter  = new RoomAdapter(RoomActivity.this, R.layout.list_room_layout, building.getListOfRooms());
        roomList.setAdapter(adapter);
    }






    private UIBbuilding getBuildingFromLastActivity() {
        UIBbuilding extraBuilding;


        Bundle extra = getIntent().getExtras();
        if( extra != null){
            extraBuilding = (UIBbuilding)getIntent().getSerializableExtra("building");
        } else {
            extraBuilding = new UIBbuilding("Error building",  errorList);
        }
        return extraBuilding;
    }

    private void loadBackdrop() {

        System.out.println(buildingCode);
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        String url = "http://rom_img.app.uib.no/byggogrombilder/"+ buildingCode +"_/"+ buildingCode +"_byggI.jpg";
        Picasso.with(getApplicationContext())
                .load(URLEncoding.encode(url))
                .centerCrop()
                .fit()
                .into(imageView);

    }

    private void floatingActionButtonListener() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Add to favorite, coming soon!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
    }


}
