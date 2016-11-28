package com.pensive.android.romplanuib;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
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
import com.pensive.android.romplanuib.util.Randomized;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener {


    WeekView mWeekView;
    JsoupTask jsoupTask;
    private UIBroom room;
    List<WeekViewEvent> events = new ArrayList<>();
    TextView weekNumber;
    String buildingCode;
    com.pensive.android.romplanuib.util.StringCleaner sc = new com.pensive.android.romplanuib.util.StringCleaner();
    private ImageView roomImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        room = getRoomFromLastActivity();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(collapsingToolbar != null){
            collapsingToolbar.setTitle( room.getName() + " - " +  sc.createBuildingName(room.getBuilding()));
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(CalendarActivity.this, R.color.transpBlack));


        jsoupTask = new JsoupTask(CalendarActivity.this, room);
        jsoupTask.execute();

        roomImage = (ImageView) findViewById(R.id.backdrop_room);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        weekNumber = (TextView) findViewById(R.id.week_text);

        if(mWeekView != null){
            mWeekView.setMonthChangeListener(this);}
        weekNumber.setText("Uke: " + getWeekNumber());

        setRoomImage();
    }

    private void setRoomImage() {

        buildingCode = sc.createBuildingCode(room.getBuilding());

        String url = "http://rom_img.app.uib.no/byggogrombilder/" + buildingCode + "_/"+ buildingCode + "_" + room.getCode() + "/"+ buildingCode + "_" + room.getCode() + "I.jpg";
        Picasso.with(CalendarActivity.this)
                .load(URLEncoding.encode(url))
                .centerCrop()
                .fit()
                .placeholder(R.drawable.uiblogo)
                .into(roomImage);
    }

    public String getWeekNumber(){
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.WEEK_OF_YEAR);


        return Integer.toString(week);
    }

    private Calendar getFirstDayofWeek() {
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


        public JsoupTask(Context context, UIBroom room){
            super();
            this.context = context;
            this.room = room;
            System.out.println("JSOUPCalendarCOnstrutor");

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

                    WeekViewEvent event = new WeekViewEvent(i, listOfCal.get(i).getSubject() +" - " + listOfCal.get(i).getDescription(), listOfCal.get(i).getBeginTime(), listOfCal.get(i).getEndTime());
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
            mWeekView.goToDate(getFirstDayofWeek());
            mWeekView.goToHour(7);

            asyncDialog.dismiss();

        }





    }
}
