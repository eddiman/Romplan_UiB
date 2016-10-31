package com.pensive.android.romplanuib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ListView;

import com.pensive.android.romplanuib.util.DownloadAndStoreData;
import com.pensive.android.romplanuib.models.UIBbuilding;
import com.pensive.android.romplanuib.models.UIBroom;

import java.util.List;

/**
 * Author: Edvard P. B.
 *
 * "Splash" class to load all buildings and rooms
 */
public class StartActivity extends AppCompatActivity {



    ListView testView;
    Context context;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(toolbar != null){toolbar.setTitle("Romplan UiB");}

        context = StartActivity.this;
        populateList();


    }


    private void initGUI() {

        //testView = (ListView) findViewById(R.id.test_list);
        //button = (Button) findViewById(R.id.button_tab);
    }

    private void populateList() {
        JsoupTask jsoupTask = new JsoupTask(context, this);
        jsoupTask.execute();
    }

}

 class JsoupTask extends AsyncTask<Void, Void, List<UIBbuilding>>{
     List<UIBbuilding> allBuildings ;
     List<UIBroom> allRooms;

     private Activity mActivity;
     private Context context;
     ProgressDialog asyncDialog;
     DownloadAndStoreData dl = new DownloadAndStoreData();


     public JsoupTask(Context context, Activity mActivity){
         super();
         this.context = context;
         this.mActivity = mActivity;
         asyncDialog = new ProgressDialog(context);


     }

     @Override
     protected void onPreExecute() {
         asyncDialog.setMessage("Henter data...");
         asyncDialog.setCancelable(false);
         asyncDialog.show();
         super.onPreExecute();
     }

     @Override
     protected List<UIBbuilding> doInBackground(Void... param) {

         //Store data in SharedPref
         if(!dl.isDataIsStored(context)) {
             allBuildings = dl.getAllBuildings();
             allRooms = dl.getAllRoomsInUni();
             dl.setStoreDataAllRooms(context, allRooms);
             dl.setStoreDataAllBuildings(context, allBuildings);
         } else {
             allBuildings = dl.getStoredDataAllBuildings(context);
         }


         return allBuildings;
     }
     protected void onPostExecute(List<UIBbuilding> buildings){

        //ListView testView = (ListView)mActivity.findViewById(R.id.test_list);

         //BuildingAdapter adapter  = new BuildingAdapter(context, R.layout.list_building_element, dl.getStoredDataAllBuildings(context));

         //testView.setAdapter(adapter);

         asyncDialog.dismiss();

         Intent i = new Intent(context, TabActivity.class);
         context.startActivity(i);


         List<UIBroom> tasksList = dl.getStoredDataAllRooms(context);
         if (tasksList.size() <= 0){
             System.out.println("Cannot retrieve data from SharedPreferences");
         } else {
             System.out.println("Data has been retrieved. There are " + tasksList.size() + " rooms in the university");

         }

     }




 }
