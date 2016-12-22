package com.pensive.android.romplanuib;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.pensive.android.romplanuib.ArrayAdapters.RoomAdapter;
import com.pensive.android.romplanuib.io.util.URLEncoding;
import com.pensive.android.romplanuib.models.UIBbuilding;
import com.pensive.android.romplanuib.models.UIBroom;
import com.pensive.android.romplanuib.util.FontController;
import com.pensive.android.romplanuib.util.StringCleaner;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.ColorFilterTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class RoomActivity extends AppCompatActivity {

    UIBbuilding building;
    List<UIBroom> errorList = new ArrayList<>();
    FontController fc = new FontController();
    String buildingName;
    String buildingCode;
    ListView roomList;
    AppBarLayout appBar;

    StringCleaner sc = new StringCleaner();
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        building = getBuildingFromLastActivity();
        buildingCode = sc.createBuildingCode(building.getName());
        buildingName = sc.createBuildingName(building.getName());

        initGUI();

    }


    private void initGUI() {
        //GUI elements
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //
        appBar = (AppBarLayout) findViewById(R.id.room_appbar);

         collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(collapsingToolbar != null){
            collapsingToolbar.setTitle(buildingName);
            collapsingToolbar.setCollapsedTitleTypeface(fc.getTypeface(this, "fonts/roboto_thin.ttf"));
            collapsingToolbar.setExpandedTitleTypeface(fc.getTypeface(this, "fonts/roboto_thin.ttf"));
        }

        loadBackdrop();
        floatingActionButtonFavoriteListener();

        getWindow().setStatusBarColor(ContextCompat.getColor(RoomActivity.this, R.color.transpBlack));

        roomList = (ListView) findViewById(R.id.room_listView);

        RoomAdapter adapter  = new RoomAdapter(RoomActivity.this, R.layout.list_room_layout, building.getListOfRooms());
        roomList.setAdapter(adapter);
        roomList.setFastScrollEnabled(true);

        setAppBarLayoutHeightOfScreenDivide(2);

    }

    /**
     * Gets the building-object from the intent that was sent from the last Activity
     * @return
     */
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

    /**
     * Creates a url from the buildingcode, uses this to load a picture with the Picasso image library.
     * Transformations objects are for applying colors to the image.
     */
    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        String url = "http://rom_img.app.uib.no/byggogrombilder/"+ buildingCode +"_/"+ buildingCode +"_byggI.jpg";

        //List for aa legge til flere transformations til image
        List<Transformation> transformations = new ArrayList<>();
        transformations.add(new GrayscaleTransformation());
        transformations.add(new ColorFilterTransformation(ContextCompat.getColor(this, R.color.transp_primary_blue)));

        Picasso.with(getApplicationContext())
                .load(URLEncoding.encode(url))
                .centerCrop()
                .fit()
                .placeholder(R.drawable.uiblogo)
                .transform(transformations)
                .into(imageView);

    }

    /**
     * Initializes the floating action button.
     * TODO: Use it for adding and removing favorites.
     */
    private void floatingActionButtonFavoriteListener() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabRoom);
        if(fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar snack = Snackbar.make(view, getResources().getString(R.string.add_elem_to_fav), Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View sbView = snack.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary_blue));
                    snack.show();
                }
            });
    }

    /**
     * @param divide Set AppBar height to screen height divided by 2->5
     */
    protected void setAppBarLayoutHeightOfScreenDivide(@IntRange(from = 2, to = 5) int divide) {
        setAppBarLayoutHeightOfScreenPercent(100 / divide);
    }

    /**
     * @param percent Set AppBar height to 20->50% of screen height
     */
    protected void setAppBarLayoutHeightOfScreenPercent(@IntRange(from = 20, to = 50) int percent) {
        setAppBarLayoutHeightOfScreenWeight(percent / 100F);
    }


    /**
     * @param weight Set AppBar height to 0.2->0.5 weight of screen height
     */
    protected void setAppBarLayoutHeightOfScreenWeight(@FloatRange(from = 0.2F, to = 0.5F) float weight) {
        if (appBar != null) {
            ViewGroup.LayoutParams params = appBar.getLayoutParams();
            params.height = Math.round(getResources().getDisplayMetrics().heightPixels * weight);
            appBar.setLayoutParams(params);
        }
    }

}
