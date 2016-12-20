package com.pensive.android.romplanuib;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pensive.android.romplanuib.io.BuildingCodeParser;
import com.pensive.android.romplanuib.io.CalActivityParser;
import com.pensive.android.romplanuib.io.util.URLEncoding;
import com.pensive.android.romplanuib.models.CalActivity;
import com.pensive.android.romplanuib.models.UIBroom;
import com.pensive.android.romplanuib.util.FontController;
import com.pensive.android.romplanuib.util.Randomized;
import com.pensive.android.romplanuib.util.StringCleaner;
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
    StringCleaner sc = new StringCleaner();
    private ImageView roomImage;
    FontController fc = new FontController();
    private CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBar;
    int currentWeekNumber;
    int nextYear = 0;

    Calendar weekDayChanged;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        room = getDataFromLastActivity();
        weekDayChanged = getFirstDayOfWeek();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        appBar = (AppBarLayout) findViewById(R.id.cal_appbar);

        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        jsoupTask = new JsoupTask(WeekCalendarActivity.this, room);
        jsoupTask.execute();

        initGUI();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initGUI() {
        roomImage = (ImageView) findViewById(R.id.backdrop_room);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        weekNumber = (TextView) findViewById(R.id.week_text);

        mWeekView.goToDate(weekDayChanged);
        mWeekView.goToHour(7);
        weekNumber.setText("Uke: " + currentWeekNumber);

        initCal();
        getWindow().setStatusBarColor(ContextCompat.getColor(WeekCalendarActivity.this, R.color.transpBlack));
        setRoomImage();
        setCollapsingTitles();
        setWeekButtons();


    }

    private void initCal() {

        if (mWeekView != null) {
            mWeekView.setMonthChangeListener(this);
        }


    }

    /**
     * Sets the listener of the next- and last week buttons
     */
    private void setWeekButtons() {

        ImageButton nextWeekButton = (ImageButton) findViewById(R.id.nextweek_button);
        ImageButton lastWeekButton = (ImageButton) findViewById(R.id.lastweek_button);

        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToNextWeek();



            }
        });

        lastWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWeekNumber--;
                if(currentWeekNumber < 1){
                    currentWeekNumber = 52;
                    nextYear = 0;
                }
                jsoupTask = new JsoupTask(WeekCalendarActivity.this, room);
                jsoupTask.execute();

                //Adds -7 days to weekDayChanged, goes to last week
                weekDayChanged.add(Calendar.DAY_OF_YEAR, -7);
                mWeekView.goToDate(weekDayChanged);

            }
        });


    }

    private void goToNextWeek() {
        //increments a week number and corrects week numbers if it exceeds 52, and sets it to be next year
        currentWeekNumber++;

        //TODO: Clean this up later, hehe
        if(currentWeekNumber > 52){
            currentWeekNumber = 1;
            //sets it to be next year, cannot set definite year, limitaion of the rom.app.uib.no
            //TODO: Possible workaround exists: everything in current year is  between 1 and 52, if exceeds 52 it goes to the following year e.g. week 60 in 2016 is actually week 7 in 2017
            //And week -2 is week 50 in 2015 and so forth
            nextYear = 1;
        }
        jsoupTask = new JsoupTask(WeekCalendarActivity.this, room);
        jsoupTask.execute();

        //Adds -7 days to weekDayChanged, goes to last week
        weekDayChanged.add(Calendar.DAY_OF_YEAR, 7);
        mWeekView.goToDate(weekDayChanged);
    }

    private void setCollapsingTitles() {

        if (collapsingToolbar != null) {
            collapsingToolbar.setCollapsedTitleTypeface(fc.getTypeface(this, "fonts/roboto_thin.ttf"));
            collapsingToolbar.setExpandedTitleTypeface(fc.getTypeface(this, "fonts/roboto_thin.ttf"));
        }

        //Changes the title when actionbar is collapsed/expanded
        AppBarLayout.OnOffsetChangedListener mListener = new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (collapsingToolbar.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbar)) {
                    //Collapsed, after scrolling down
                    collapsingToolbar.setTitle(room.getName() + " - " + sc.createBuildingName(room.getBuilding()));
                } else {
                    //Expanded, normal state
                    collapsingToolbar.setTitle(room.getName());

                }
            }
        };

        appBar.addOnOffsetChangedListener(mListener);
    }


    private void setRoomImage() {

        buildingCode = sc.createBuildingCode(room.getBuilding());

        List<Transformation> transformations = new ArrayList<>();
        transformations.add(new GrayscaleTransformation());
        transformations.add(new ColorFilterTransformation(ContextCompat.getColor(this, R.color.transp_primary_blue)));

        String url = "http://rom_img.app.uib.no/byggogrombilder/" + buildingCode + "_/" + buildingCode + "_" + room.getCode() + "/" + buildingCode + "_" + room.getCode() + "I.jpg";
        Picasso.with(WeekCalendarActivity.this)
                .load(URLEncoding.encode(url))
                .centerCrop()
                .fit()
                .placeholder(R.drawable.uiblogo)
                .transform(transformations)
                .into(roomImage);
    }



    /**
     * Returns the first weekDayChanged of the week
     *
     * @return Calender with first weekDayChanged of wee
     */
    private Calendar getFirstDayOfWeek() {
// Get calendar set to current date and time
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c;
    }

    /**
     * Puts together the EXTRA data that was sent with the last activity
     *
     * @return a UIBroom with the room in question
     */
    private UIBroom getDataFromLastActivity() {
        UIBroom extraBuilding;
        String weekNumberString;


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            extraBuilding = (UIBroom) getIntent().getSerializableExtra("room");
            weekNumberString = (String) getIntent().getSerializableExtra("currentWeek");

            currentWeekNumber = Integer.parseInt(weekNumberString);

        } else {
            extraBuilding = new UIBroom("Error:Room", "Error building", "Error");
        }
        return extraBuilding;
    }

    /**
     * Adds a list of events to the calender
     *
     * @param newYear year the calendar will show
     * @param newMonth the month the calendar wil show
     * @return the events that matches with the parameters
     */
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("WeekCalendar Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * AsyncTask for downloading the eventdata
     */
    class JsoupTask extends AsyncTask<Void, Void, List<WeekViewEvent>> {
        ProgressDialog asyncDialog;
        UIBroom room;
        Randomized rnd = new Randomized();
        StringCleaner sc = new StringCleaner();
        Context context;
        boolean timeoutError;


        JsoupTask(Context context, UIBroom room) {
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

            //TODO: Problem here when going back and forth in weeks, adds the events everytime it reloads a week, must adapt to not add events if week has already been loaded
            try {
                String roomURL = BuildingCodeParser.getRoomURL(room.getBuilding(), room.getCode()) + "&printweek=" + currentWeekNumber + "&nextyear=" + nextYear;
                CalActivityParser parser = new CalActivityParser(roomURL, room.getBuilding(), sc.createBuildingCode(room.getCode()));
                List<CalActivity> listOfCal = parser.getCalActivityList();


                for (int i = 0; i < listOfCal.size(); i++) {

                    WeekViewEvent event = new WeekViewEvent(i, listOfCal.get(i).getSubject() + " - " + listOfCal.get(i).getDescription() + " - " + listOfCal.get(i).getWeekday(), listOfCal.get(i).getBeginTime(), listOfCal.get(i).getEndTime());
                    event.setColor(rnd.getRandomColorFilter());
                    events.add(event);
                }

                System.out.println("There are: " + events.size());
            } catch (NullPointerException e) {
                e.printStackTrace();
                timeoutError = true;

            }
            return events;

        }

        protected void onPostExecute(List<WeekViewEvent> activitiesOfRoom) {
            if(timeoutError){
                Toast.makeText(context, "Tilkoblingen tok for lang tid, prøv igjen", Toast.LENGTH_SHORT).show();
            }

            mWeekView.notifyDatasetChanged();

            asyncDialog.dismiss();

        }


    }



}
