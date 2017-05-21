package com.pensive.android.romplanuib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.View;

import com.pensive.android.romplanuib.util.DataManager;
import com.pensive.android.romplanuib.models.Building;

import java.util.List;

/**
 * @author Edvard P. B.
 *
 * "Splashscreen"-class to load all buildings and rooms
 */
public class StartActivity extends AppCompatActivity {



    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        context = StartActivity.this;
        hideSystemUI();
        populateList();

    }

    /**
     * Hides the system UI
     */
    private void hideSystemUI() {

        getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.transparent));
        getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.transparent));

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so
        // that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /**
     * Executes the inner class JsoupTask
     */
    private void populateList() {
        JsoupTask jsoupTask = new JsoupTask(context, this, getResources().getString(R.string.load_data_string));
        jsoupTask.execute();
    }

}

class JsoupTask extends AsyncTask<Void, Void, List<Building>>{
    private Context context;
    private ProgressDialog asyncDialog;
    private SpannableString loadSpanString;


    public JsoupTask(Context context, Activity mActivity, String loadDataString){
        super();
        this.context = context;
        asyncDialog = new ProgressDialog(context, R.style.DialogTheme);

        loadSpanString = new SpannableString(loadDataString);

    }

    /**
     * Starts the progress dialog
     */
    @Override
    protected void onPreExecute() {


        // Add a span for the custom font font
        loadSpanString.setSpan(new TypefaceSpan("roboto_thin.ttf"), 0, loadSpanString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        asyncDialog.setTitle(loadSpanString);
        asyncDialog.setCancelable(false);
        asyncDialog.show();

        super.onPreExecute();
    }

    /**
     * Background process for scraping and downloading the data from romplan app. Also stores the data
     * in SharedPreferences
     * @param param standard for doInBavkground
     * @return the list of allbuildings
     */
    @Override
    protected List<Building> doInBackground(Void... param) {

        DataManager dataManager = new DataManager(context);


        return dataManager.getAllBuildings();
    }

    /**
     * dismisses the dialog and starts the TabActivity
     * @param buildings a list of the buildings, again standard param for onPostExecute
     */
    protected void onPostExecute(List<Building> buildings){

        asyncDialog.dismiss();

        Intent i = new Intent(context, TabActivity.class);
        context.startActivity(i);

    }




}
