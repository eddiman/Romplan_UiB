package com.pensive.android.romplanuib;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.reflect.TypeToken;
import com.pensive.android.romplanuib.models.CalActivity;
import com.pensive.android.romplanuib.models.Room;
import com.pensive.android.romplanuib.models.University;
import com.pensive.android.romplanuib.models.messages.CalendarActivityListEvent;
import com.pensive.android.romplanuib.queries.EventQueries;
import com.pensive.android.romplanuib.util.DataManager;
import com.pensive.android.romplanuib.util.DateFormatter;
import com.pensive.android.romplanuib.util.FavoriteHandler;
import com.pensive.android.romplanuib.util.FontController;
import com.pensive.android.romplanuib.util.Randomized;
import com.pensive.android.romplanuib.util.ThemeSelector;
import com.pensive.android.romplanuib.util.io.ApiClient;
import com.pensive.android.romplanuib.util.io.ApiInterface;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.ColorFilterTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity containing calendar week view
 *
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class WeekCalendarActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener {


    WeekView mWeekView;
    private Room room;
    List<WeekViewEvent> events = new ArrayList<>();
    TextView weekNumber;
    private ImageView roomImage;
    FontController fc = new FontController();
    private CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBar;
    int currentWeekNumber;
    String currentSemester;
    String semesterStart;
    String semesterEnd;
    int nextYear = 0;
    DateFormatter df;
    String loadDataString;
    private TextView buildingNameText;
    private Button goToMazeMap;
    Boolean isRoomfav;
    List<Room> favRooms;

    EventQueries eventQueries;
    FavoriteHandler favoriteHandler;
    DataManager dataManager;
    Calendar weekDayChanged;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private FloatingActionButton fab;

    ThemeSelector theme = new ThemeSelector();
    String uniCampusCode;
    University selectedUniversity;
    private WebView mazeMapWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        room = getDataFromLastActivity();
        System.out.println(room.toString());
        weekDayChanged = getFirstDayOfWeek();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        appBar = (AppBarLayout) findViewById(R.id.cal_appbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        loadDataString = getResources().getString(R.string.load_data_string);
        DataManager dataManager = new DataManager();
        eventQueries = new EventQueries();
        selectedUniversity = dataManager.getSavedObjectFromSharedPref(this, "university", new TypeToken<University>() {
        }.getType());
        uniCampusCode = selectedUniversity.getCampusCode();


        int year = Calendar.getInstance().get(Calendar.YEAR);
        int weekNumber = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        if (weekDayChanged.get(Calendar.MONTH) < Calendar.JULY) {
            semesterStart = year + "-01-01";
            semesterEnd = year + "-06-30";
            currentSemester = "S";
        } else {
            semesterStart = year + "-07-01";
            semesterEnd = year + "-12-31";
            currentSemester = "F";
        }
        eventQueries.getEvents(this, room, weekNumber, year);
        favoriteHandler = new FavoriteHandler();
        isRoomfav = favoriteHandler.isRoomInFavorites(this, room);

        initGUI();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initGUI() {
        roomImage = (ImageView) findViewById(R.id.backdrop_room);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        weekNumber = (TextView) findViewById(R.id.week_text);
        buildingNameText = (TextView) findViewById(R.id.building_name_text);
        goToMazeMap = (Button) findViewById(R.id.goto_mazemap);
        mazeMapWebView = (WebView) findViewById(R.id.mazemap_webview);

        mWeekView.goToDate(weekDayChanged);
        mWeekView.goToHour(7);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        initCal();
        setAppBarLayoutHeightOfScreenDivide(2);
        floatingActionButtonFavoriteListener(this);
        getWindow().setStatusBarColor(ContextCompat.getColor(WeekCalendarActivity.this, R.color.transpBlack));

        if (isRoomfav) {
            fab.setImageResource(R.drawable.ic_star_full);
        } else {
            fab.setImageResource(R.drawable.ic_star_empty);
        }


        initIndoorMap();


        setRoomImage();
        setCollapsingTitles();
        setBuildingTextView();
        setWeekButtons();
        setGoToMazeMap();

        updateWeekTextView();


    }

    private void initIndoorMap() {

        //TODO: Reenable mazemap
        switch (uniCampusCode) {
            case "uib":
                //initFoldingCellMazeMap();
                //initMazeMap();
                break;

            default:
                break;

        }
    }

    private void initFoldingCellMazeMap() {
        final FoldingCell fc = (FoldingCell) findViewById(R.id.folding_cell);

        // attach click listener to folding cell
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });
    }

    private void initMazeMap() {

        mazeMapWebView.getSettings().setJavaScriptEnabled(true);

        // Add a WebViewClient
        mazeMapWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                // Inject CSS when page is done loading
                injectCSS();
                super.onPageFinished(view, url);
            }
        });
        mazeMapWebView.loadUrl(room.getMazeMapUrl() + "&zoom=17");
    }


    private void injectCSS() {
        try {
            InputStream inputStream = getAssets().open("mazemap.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            mazeMapWebView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * 1w
     *
     * @param weight Set AppBar height to 0.2->0.5 weight of screen height
     */
    protected void setAppBarLayoutHeightOfScreenWeight(@FloatRange(from = 0.2F, to = 0.5F) float weight) {
        if (appBar != null) {
            ViewGroup.LayoutParams params = appBar.getLayoutParams();
            params.height = Math.round(getResources().getDisplayMetrics().heightPixels * weight);
            appBar.setLayoutParams(params);
        }
    }

    /**
     * Initializes the calendar, sets it to listen for added events and date formatting
     */
    private void initCal() {

        if (mWeekView != null) {
            mWeekView.setMonthChangeListener(this);
            mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
                @Override
                public void onEventClick(WeekViewEvent event, RectF eventRect) {
                    createEventDialog(event);

                }
            });

            /*
            Formatting the date into dd.MM, standard was MM/dd
             */
            mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
                @Override
                public String interpretDate(Calendar date) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd.M", Locale.getDefault());
                        return sdf.format(date.getTime()).toUpperCase();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }

                @Override
                public String interpretTime(int hour) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, 0);

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        return sdf.format(calendar.getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }
            });
        }
    }

    /**
     * Creates an event dialog and displays the subject and the description of the event in a more readable fashion
     *
     * @param event the event to be displayed
     */
    private void createEventDialog(WeekViewEvent event) {
        String[] eventData = event.getName().split(" - ", 2);


        final AlertDialog dialog = new AlertDialog.Builder(this, theme.getDialogThemeFromCampusCode(this, selectedUniversity.getCampusCode()))
                .setView(R.layout.dialog_event)
                .create();
        dialog.setCancelable(true);
        dialog.show();


        TextView eventDescription = (TextView) dialog.findViewById(R.id.dialog_event_desc);
        TextView eventTitle = (TextView) dialog.findViewById(R.id.dialog_event_title);
        TextView eventTime = (TextView) dialog.findViewById(R.id.dialog_event_time);

        //Checks if textview eventTitle is not null, set text to display subject of the event
        if (eventTitle != null) {
            eventTitle.setText(eventData[0]);

            //If the eventData contains 2 or more, set the description to be the second element in eventData
            if (eventData.length >= 2) {
                eventDescription.setText(eventData[1]);
            }

            //Formats the event time and sets it in the textview
            df = new DateFormatter();

            String eventStart = df.formatCalendar(event.getStartTime(), "HH:mm");
            String eventEnd = df.formatCalendar(event.getEndTime(), "HH:mm");

            eventTime.setText(eventStart + " - " + eventEnd);

        }

        floatingActionButtonCalendarListener(this, event, eventData, dialog);


    }


    /**
     * Initializes the floating action button. For adding event to personal user's calendar
     */

    private void floatingActionButtonCalendarListener(final Context context, WeekViewEvent event, final String[] eventData, AlertDialog dialog) {
        final String eventTitleCal = eventData[0];
        final long eventStartInMillis = event.getStartTime().getTimeInMillis();
        final long eventEndInMillis = event.getEndTime().getTimeInMillis();
        final String location = room.getBuildingAcronym();//TODO should be buildingname, not acronym


        //Checks whether eventData contains description, then sets the FINAL string to the temporary description.
        String tempDescription;
        if (eventData.length >= 2) {
            tempDescription = eventData[1];
        } else {
            tempDescription = getString(R.string.event_no_descript);
        }

        final String eventDesc = tempDescription;


        FloatingActionButton fab = (FloatingActionButton) dialog.findViewById(R.id.fabCal);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar snack = Snackbar.make(view, getResources().getString(R.string.event_added), Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View sbView = snack.getView();

                    //Set custom typeface
                    TextView tv = (TextView) (sbView).findViewById(android.support.design.R.id.snackbar_text);
                    Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/roboto_thin.ttf");
                    tv.setTypeface(font);
                    tv.setTextSize(16);

                    int color = theme.getAttributeColor(context, R.attr.colorPrimary);


                    sbView.setBackgroundColor(color);
                    snack.show();


                    //Countdown to when to start the calendar intent, this for showing user the snackbar of whats happening next
                    new CountDownTimer(1200, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {


                            Intent intent = new Intent(Intent.ACTION_INSERT)
                                    .setData(CalendarContract.Events.CONTENT_URI)
                                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventStartInMillis)
                                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventEndInMillis)
                                    .putExtra(CalendarContract.Events.TITLE, eventTitleCal)
                                    .putExtra(CalendarContract.Events.DESCRIPTION, eventDesc)
                                    .putExtra(CalendarContract.Events.EVENT_LOCATION, location + ", Bergen")//TODO, this cannot be Bergen for everything
                                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                            startActivity(intent);
                        }
                    }.start();
                }
            });
    }

    /**
     * Initializes the floating action button.
     */
    private void floatingActionButtonFavoriteListener(final Context context) {

        fab = (FloatingActionButton) findViewById(R.id.fabWeekCal);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Snackbar snack;
                    if (!isRoomfav) {
                        fab.setImageResource(R.drawable.ic_star_full);
                        favoriteHandler.addRoomToFavorites(getApplicationContext(), room);
                        snack = Snackbar.make(view, room.getName() + getString(R.string.add_elem_to_fav), Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        isRoomfav = true;

                    } else {
                        fab.setImageResource(R.drawable.ic_star_empty);
                        favoriteHandler.removeRoomFromFavorites(getApplicationContext(), room);
                        snack = Snackbar.make(view, room.getName() + getString(R.string.remove_elem_from_fav), Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        isRoomfav = false;
                    }
                    View sbView = snack.getView();

                    TextView tv = (TextView) (sbView).findViewById(android.support.design.R.id.snackbar_text);
                    Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/roboto_thin.ttf");
                    tv.setTypeface(font);

                    int color = theme.getAttributeColor(context, R.attr.colorPrimary);

                    sbView.setBackgroundColor(color);
                    snack.show();
                }
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setBuildingTextView() {
        String currBuildingName = room.getBuildingAcronym();//TODO buildingname?
        buildingNameText.setText(getString(R.string.building) + ": " + currBuildingName);

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
                goToLastWeek();

            }
        });


    }

    private void setGoToMazeMap() {

        goToMazeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mazeUrl = room.getMazeMapUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mazeUrl));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * Empties the event list completely
     */
    private void emptyEventList() {
        events.clear();
    }

    /**
     * Updates the week text view
     */
    private void updateWeekTextView() {
        weekNumber.setText(getString(R.string.week) + " " + currentWeekNumber);
    }

    /**
     * Method for loading the following week's events
     */
    private void goToNextWeek() {
        //increments a week number and corrects week numbers if it exceeds 52, and sets it to be next year
        weekDayChanged.add(Calendar.DAY_OF_YEAR, 7);
        currentWeekNumber = weekDayChanged.get(Calendar.WEEK_OF_YEAR);
        emptyEventList();
        eventQueries.getEvents(this, room, currentWeekNumber, weekDayChanged.get(Calendar.YEAR));

        //Adds -7 days to weekDayChanged, goes to last week
        mWeekView.goToDate(weekDayChanged);
        updateWeekTextView();


    }

    /**
     * Method for loading the previous week's events
     */
    private void goToLastWeek() {

        weekDayChanged.add(Calendar.DAY_OF_YEAR, -7);
        currentWeekNumber = weekDayChanged.get(Calendar.WEEK_OF_YEAR);
        eventQueries.getEvents(this, room, currentWeekNumber, weekDayChanged.get(Calendar.YEAR));
        emptyEventList();


        //Adds -7 days to weekDayChanged, goes to last week
        mWeekView.goToDate(weekDayChanged);
        updateWeekTextView();

    }

    private void updateSemester() {
        int year = weekDayChanged.get(Calendar.YEAR);
        semesterStart = year + "-01-01";
        semesterEnd = year + "-06-30";
        if (weekDayChanged.get(Calendar.MONTH) > Calendar.JUNE) {
            semesterStart = year + "-07-01";
            semesterEnd = year + "-12-31";
        }
    }

    private void setCollapsingTitles() {

        if (collapsingToolbar != null) {
            collapsingToolbar.setCollapsedTitleTypeface(fc.getTypeface(this, "fonts/roboto_thin.ttf"));
            collapsingToolbar.setCollapsedTitleTypeface(fc.getTypeface(this, "fonts/roboto_thin.ttf"));
            collapsingToolbar.setExpandedTitleTypeface(fc.getTypeface(this, "fonts/roboto_thin.ttf"));
        }

        //Changes the title when actionbar is collapsed/expanded
        AppBarLayout.OnOffsetChangedListener mListener = new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (collapsingToolbar.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbar)) {
                    //Collapsed, after scrolling down
                    collapsingToolbar.setTitle(room.getName() + " - " + room.getBuildingAcronym());//TODO should be buildingname, not acronym
                } else {
                    //Expanded, normal state
                    collapsingToolbar.setTitle(room.getName());

                    //weekNumber.setText("Uke: " + currentWeekNumber);

                }
            }
        };


        appBar.addOnOffsetChangedListener(mListener);
    }


    private void setRoomImage() {

        int color = theme.getAttributeColor(this, R.attr.transpImgColor);

        String url = room.getImageURL();
        List<Transformation> transformations = new ArrayList<>();
        transformations.add(new GrayscaleTransformation());
        transformations.add(new ColorFilterTransformation(color));
        int imageResource = getResources().getIdentifier(selectedUniversity.getLogoUrl(), null, getPackageName());


        Picasso.get()
                .load(url)
                .transform(transformations)
                .centerCrop()
                .fit()
                .placeholder(imageResource)
                .into(roomImage);
    }


    /**
     * Returns the first weekDayChanged of the week
     *
     * @return Calender with first weekDayChanged of week
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
     * @return a Room with the room in question
     */
    private Room getDataFromLastActivity() {
        Room extraBuilding;
        String weekNumberString;


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            extraBuilding = (Room) getIntent().getSerializableExtra("room");
            weekNumberString = (String) getIntent().getSerializableExtra("currentWeek");

            currentWeekNumber = Integer.parseInt(weekNumberString);

        } else {
            extraBuilding = new Room("Error:Room", "Error building", "Error");
        }
        return extraBuilding;
    }

    /**
     * Adds a list of events to the calender
     *
     * @param newYear  year the calendar will show
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

    public void onBackPressed() {
        super.onBackPressed();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnCalendarActivityListEvent(CalendarActivityListEvent calendarActivityListEvent) {
        Randomized rnd = new Randomized();
        List<CalActivity> calendarActivityList = calendarActivityListEvent.getListOfCalendarActivities();
        for (int i = 0; i < calendarActivityList.size(); i++) {

            WeekViewEvent event = new WeekViewEvent(i, calendarActivityList.get(i).getCourseID() + " " + calendarActivityList.get(i).getTeachingMethodName() + " - " + calendarActivityList.get(i).getSummary(), calendarActivityList.get(i).getBeginTime(), calendarActivityList.get(i).getEndTime());

            event.setColor(rnd.getRandomColorFilter());
            events.add(event);
            mWeekView.notifyDatasetChanged();
        }
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
        EventBus.getDefault().register(this);
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
