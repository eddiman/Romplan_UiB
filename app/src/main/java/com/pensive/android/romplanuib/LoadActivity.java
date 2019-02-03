package com.pensive.android.romplanuib;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.reflect.TypeToken;
import com.pensive.android.romplanuib.models.Area;
import com.pensive.android.romplanuib.models.University;
import com.pensive.android.romplanuib.util.DataManager;
import com.pensive.android.romplanuib.util.io.ApiClient;
import com.pensive.android.romplanuib.util.io.ApiInterface;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Edvard Bjørgen & Fredrik Heimsæter
 *
 * @version 2.0
 *
 * "Splashscreen"-class to load all buildings and rooms
 */
public class LoadActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView splashTitle;
    ImageView splashLogo;
    ImageView splashImage;
    University selectedUniversity;
    String uniCampusCode;
    ApiInterface apiService;
    DataManager dataManager;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        context = LoadActivity.this;
        dataManager = new DataManager();
        apiService = ApiClient.getClient().create(ApiInterface.class);
        selectedUniversity = dataManager.getSavedObjectFromSharedPref(context, "university", new TypeToken<University>(){}.getType());
        uniCampusCode = selectedUniversity.getCampusCode();

        hideSystemUI();
        initGui();
        downloadUniversity();
    }

    private void initGui() {

        splashImage = (ImageView) findViewById(R.id.splash_image);
        splashTitle = (TextView) findViewById(R.id.splash_title);
        splashLogo = (ImageView) findViewById(R.id.splash_logo);

        //Uri bgUri = Uri.parse("android.resource://com.pensive.android.romplanuib/drawable/splash_" + selectedUniversity);
        String bgUri = "@drawable/splash_" + uniCampusCode;
        String logoUri = selectedUniversity.getLogoUrl();

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
            case "uit":
                splashTitle.append(getResources().getText(R.string.uni_oslo));
                break;
            case "ntnu":
                splashTitle.append(getResources().getText(R.string.uni_oslo));
                break;
            case "oslomet":
                splashTitle.append(getResources().getText(R.string.uni_oslo));
                break;
        }

    }
    private void downloadUniversity(){
        Call<List<Area>> call = apiService.getUniversity(selectedUniversity.getCampusCode());
        call.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                if(response.code() == 200){
                    selectedUniversity.setAreas(response.body());
                    dataManager.storeObjectInSharedPref(context, "university", selectedUniversity);

                    Intent i = new Intent(context, BuildingMainActivity.class);
                    context.startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                Toast.makeText(context, R.string.something_wrong, Toast.LENGTH_LONG);
                t.printStackTrace();

            }
        });

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
}
