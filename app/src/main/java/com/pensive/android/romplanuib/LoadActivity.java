package com.pensive.android.romplanuib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pensive.android.romplanuib.models.UniCampus;
import com.pensive.android.romplanuib.util.DataManager;
import com.pensive.android.romplanuib.models.Building;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author Edvard P. B.
 *
 * "Splashscreen"-class to load all buildings and rooms
 */
public class LoadActivity extends AppCompatActivity {
    TextView splashTitle;
    ImageView splashLogo;
    ImageView splashImage;
    UniCampus selectedCampus;
    String uniCampusCode;

    String areaCode;


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        context = LoadActivity.this;
        DataManager dataManager = new DataManager(context);
        selectedCampus = dataManager.loadCurrentUniCampusSharedPref();
        uniCampusCode = selectedCampus.getCampusCode();
        areaCode = dataManager.getSelectedAreaCode(this);

        hideSystemUI();
        populateList();
        initGui();
    }

    private void initGui() {

        splashImage = (ImageView) findViewById(R.id.splash_image);
        splashTitle = (TextView) findViewById(R.id.splash_title);
        splashLogo = (ImageView) findViewById(R.id.splash_logo);

        //Uri bgUri = Uri.parse("android.resource://com.pensive.android.romplanuib/drawable/splash_" + selectedCampus);
        String bgUri = "@drawable/splash_" + uniCampusCode;
        String logoUri = selectedCampus.getLogoUrl();

        int bgRes = getResources().getIdentifier(bgUri, null, getPackageName());

        Drawable splashImageRes = ResourcesCompat.getDrawable(getResources(), bgRes, null);
        splashImage.setImageDrawable(splashImageRes);

        int logoRes = getResources().getIdentifier(logoUri, null, getPackageName());

        Drawable splashLogoRes = ResourcesCompat.getDrawable(getResources(), logoRes, null);
        splashLogo.setImageDrawable(splashLogoRes);

        splashTitle.setText(getResources().getText(R.string.splash_title) + " ");
        switch (uniCampusCode){
            case "uib":
                splashTitle.append(getResources().getText(R.string.uni_bergen));
                break;
            case "uio":
                splashTitle.append(getResources().getText(R.string.uni_oslo));
                break;



        }


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
        JsoupTask jsoupTask = new JsoupTask(context, getResources().getString(R.string.load_data_string), uniCampusCode, areaCode);
        jsoupTask.execute();
    }

}

class JsoupTask extends AsyncTask<Void, Void, List<Building>>{
    private String uniCampusCode;
    private Context context;
    private ProgressDialog asyncDialog;
    private SpannableString loadSpanString;
    private String areaCode;


    public JsoupTask(Context context, String loadDataString, String uniCampusCode, String areaCode){
        super();
        this.context = context;
        this.uniCampusCode = uniCampusCode;
        this.areaCode = areaCode;
        asyncDialog = new ProgressDialog(context, R.style.DialogRedTheme);


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
        //asyncDialog.show();

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
        dataManager.checkIfDataHasBeenLoadedBefore(uniCampusCode);

        List <Building> buildings= dataManager.getAllBuildings();

        return buildings;
    }

    /**
     * dismisses the dialog and starts the BuildingMainActivity
     * @param buildings a list of the buildings, again standard param for onPostExecute
     */
    protected void onPostExecute(List<Building> buildings){

        asyncDialog.dismiss();

        Intent i = new Intent(context, BuildingMainActivity.class);
        context.startActivity(i);

    }




}
