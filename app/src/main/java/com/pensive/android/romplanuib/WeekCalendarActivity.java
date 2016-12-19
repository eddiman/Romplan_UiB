package com.pensive.android.romplanuib;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.pensive.android.romplanuib.io.BuildingCodeParser;
import com.pensive.android.romplanuib.io.CalActivityParser;
import com.pensive.android.romplanuib.io.util.URLEncoding;
import com.pensive.android.romplanuib.models.CalActivity;
import com.pensive.android.romplanuib.models.UIBroom;
import com.pensive.android.romplanuib.util.FontController;
import com.pensive.android.romplanuib.util.Randomized;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.picasso.transformations.ColorFilterTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

public class WeekCalendarActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener {


    WeekView mWeekView;
    JsoupTask jsoupTask;
    private UIBroom room;
    List<WeekViewEvent> events = new ArrayList<>();
    TextView weekNumber;
    String buildingCode;
    com.pensive.android.romplanuib.util.StringCleaner sc = new com.pensive.android.romplanuib.util.StringCleaner();
    private ImageView roomImage;
    FontController fc = new FontController();
    private CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        room = getRoomFromLastActivity();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBar = (AppBarLayout) findViewById(R.id.cal_appbar);

        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);



        jsoupTask = new JsoupTask(WeekCalendarActivity.this, room);
        jsoupTask.execute();

        initGUI();


    }

    private void initGUI() {
        initCal();
        setRoomImage();
        if(collapsingToolbar != null){

            collapsingToolbar.setTitle( room.getName() + " - " +  sc.createBuildingName(room.getBuilding()));
            collapsingToolbar.setCollapsedTitleTypeface(fc.getTypeface(this, "fonts/roboto_thin.ttf"));
            collapsingToolbar.setExpandedTitleTypeface(fc.getTypeface(this, "fonts/roboto_thin.ttf"));
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(WeekCalendarActivity.this, R.color.transpBlack));

        AppBarLayout.OnOffsetChangedListener mListener = new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(collapsingToolbar.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbar)) {
                    collapsingToolbar.setTitle(room.getName() + " - " +  sc.createBuildingName(room.getBuilding()));
                } else {
                    collapsingToolbar.setTitle(room.getName());

                }
            }
        };

        appBar.addOnOffsetChangedListener(mListener);




    }

    private void initCal() {

        roomImage = (ImageView) findViewById(R.id.backdrop_room);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        weekNumber = (TextView) findViewById(R.id.week_text);

        if(mWeekView != null){
            mWeekView.setMonthChangeListener(this);}
          weekNumber.setText("Uke: " + getWeekNumber());

    }


    private void setRoomImage() {

        buildingCode = sc.createBuildingCode(room.getBuilding());

        List<Transformation> transformations = new ArrayList<>();
        transformations.add(new GrayscaleTransformation());
        transformations.add(new ColorFilterTransformation(ContextCompat.getColor(this, R.color.transp_primary_blue)));

        String url = "http://rom_img.app.uib.no/byggogrombilder/" + buildingCode + "_/"+ buildingCode + "_" + room.getCode() + "/"+ buildingCode + "_" + room.getCode() + "I.jpg";
        Picasso.with(WeekCalendarActivity.this)
                .load(URLEncoding.encode(url))
                .centerCrop()
                .fit()
                .placeholder(R.drawable.uiblogo)
                .transform(transformations)
                .into(roomImage);
    }

    public String getWeekNumber(){
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.WEEK_OF_YEAR);


        return Integer.toString(week);
    }

    private Calendar getFirstDayOfWeek() {
// Get calendar set to current date and time
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c;
    }


    private UIBroom getRoomFromLastActivity() {
        UIBroom extraBuilding;


        Bundle extra = getIntent().getExtras();
        if( extra != null){
            extraBuilding = (UIBroom)getIntent().getSerializableExtra("room");
        } else {
            extraBuilding = new UIBroom("Error:Room",  "Error building","Error");
        }
        return extraBuilding;
    }

    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth){
        // Populate the week view with some events.
        List<WeekViewEvent> matchedEvents = new ArrayList<>();
        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth))
                matchedEvents.add(event);

        }

        return matchedEvents;
    }

    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) ||
                (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class JsoupTask extends AsyncTask<Void, Void, List<WeekViewEvent>> {
        ProgressDialog asyncDialog;
        UIBroom room;
        Randomized rnd = new Randomized();
        com.pensive.android.romplanuib.util.StringCleaner sc = new com.pensive.android.romplanuib.util.StringCleaner();
        Context context;


        JsoupTask(Context context, UIBroom room){
            super();
            this.context = context;
            this.room = room;
            System.out.println("JSOUPCalendarConstrutor");

            asyncDialog = new ProgressDialog(context);

        }

        @Override
        protected void onPreExecute() {
            asyncDialog.setMessage("Getting data...");
            asyncDialog.setCancelable(false);
            asyncDialog.show();
            System.out.println("JSOUPCalendarPREEXCUTE");
            super.onPreExecute();
        }

        @Override
        protected List<WeekViewEvent> doInBackground(Void... param) {
            try{
                String roomURL = BuildingCodeParser.getRoomURL(room.getBuilding(), room.getCode()) + "&printweek=" + getWeekNumber();
                CalActivityParser parser = new CalActivityParser(roomURL, room.getBuilding(), sc.createBuildingCode(room.getCode()));
                List<CalActivity> listOfCal = parser.getCalActivityList();


                for(int i = 0; i < listOfCal.size(); i++ ){

                    WeekViewEvent event = new WeekViewEvent(i, listOfCal.get(i).getSubject() +" - " + listOfCal.get(i).getDescription() + " - " + listOfCal.get(i).getWeekday(), listOfCal.get(i).getBeginTime(), listOfCal.get(i).getEndTime());
                    event.setColor(rnd.getRandomColorFilter());
                    events.add(event);
                }

                System.out.println("There are: " + events.size());
            } catch(NullPointerException e){
                e.printStackTrace();
                System.out.println("UiB rom is error");

            }
            return events;

        }
        protected void onPostExecute(List<WeekViewEvent> activitiesOfRoom){


            mWeekView.notifyDatasetChanged();
            System.out.println("JSOUPCalendarPOST-EXCUTE");
            mWeekView.goToDate(getFirstDayOfWeek());
            mWeekView.goToHour(7);

            asyncDialog.dismiss();

        }





    }
}
