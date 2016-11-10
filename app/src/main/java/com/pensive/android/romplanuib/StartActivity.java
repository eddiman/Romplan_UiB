package com.pensive.android.romplanuib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.pensive.android.romplanuib.util.DataManager;
import com.pensive.android.romplanuib.models.UIBbuilding;
import com.pensive.android.romplanuib.models.UIBroom;

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
        // Set the content to appear under the system bars so that the content
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
        JsoupTask jsoupTask = new JsoupTask(context, this);
        jsoupTask.execute();
    }

}

class JsoupTask extends AsyncTask<Void, Void, List<UIBbuilding>>{
    private Activity mActivity;
    private Context context;
    private ProgressDialog asyncDialog;
    private DataManager dataManager;


    public JsoupTask(Context context, Activity mActivity){
        super();
        this.context = context;
        this.mActivity = mActivity;
        asyncDialog = new ProgressDialog(context);


    }

    /**
     * Starts the progress dialog
     */
    @Override
    protected void onPreExecute() {
        asyncDialog.setMessage("Henter data...");
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
    protected List<UIBbuilding> doInBackground(Void... param) {

        dataManager = new DataManager(context);


        return dataManager.getAllBuildings();
    }

    /**
     * dismisses the dialog and starts the TabActivity
     * @param buildings a list of the buildings, again standard param for onPostExecute
     */
    protected void onPostExecute(List<UIBbuilding> buildings){

        asyncDialog.dismiss();

        Intent i = new Intent(context, TabActivity.class);
        context.startActivity(i);

    }




}
