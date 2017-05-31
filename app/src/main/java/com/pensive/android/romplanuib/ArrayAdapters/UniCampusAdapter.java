package com.pensive.android.romplanuib.ArrayAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pensive.android.romplanuib.LoadActivity;
import com.pensive.android.romplanuib.R;
import com.pensive.android.romplanuib.models.UniCampus;
import com.pensive.android.romplanuib.util.DataManager;
import com.pensive.android.romplanuib.util.FontController;
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
public class UniCampusAdapter extends ArrayAdapter<UniCampus> {


    private LayoutInflater inflater;
    private Context context;
    private int textViewResourceId;
    private List<UniCampus> campuses;
    FontController fc = new FontController();
    private Typeface bebasFont;
    private RadioGroup areaRadioGroup;
    private String localAreaCode;

    public UniCampusAdapter(Context context, int textViewResourceId, List<UniCampus> campuses) {
        super(context, textViewResourceId, campuses);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.campuses = campuses;
        this.bebasFont = fc.getTypeface(getContext(), "bebas_neue.ttf");


    }

    public int getCount() {
        return campuses.size();
    }
    public UniCampus getItem(int position) {
        return campuses.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent){
        View row = convertView;
        UniCampusAdapter.CampusHolder holder = null;

        if (row == null) {
            holder = new UniCampusAdapter.CampusHolder();
            row = inflater.inflate(textViewResourceId, parent, false);

            holder.campusImage = (ImageView) row.findViewById(R.id.campus_card_image);
            holder.campusLogo = (ImageView) row.findViewById(R.id.campus_logo);
            holder.campusText = (TextView) row.findViewById(R.id.campus_card_string1);

            row.setTag(holder);


        } else {

            holder = (UniCampusAdapter.CampusHolder)row.getTag();
        }

        final UniCampus uniCampus = campuses.get(position);
        Uri logoUri = Uri.parse(uniCampus.getLogoUrl());
        Uri bgUri = Uri.parse(uniCampus.getBgUrl());

        List<Transformation> transformations = new ArrayList<>();
        transformations.add(new GrayscaleTransformation());
        transformations.add(new ColorFilterTransformation(R.attr.transpImgColor));
        int bgResource = context.getResources().getIdentifier(uniCampus.getBgUrl(), null, context.getPackageName());
        int logoResource = context.getResources().getIdentifier(uniCampus.getLogoUrl(), null, context.getPackageName());

        Picasso.with(context)
                .load(bgResource)
                .centerCrop()
                .fit()
                .transform(transformations)
                .into(holder.campusImage);

        Picasso.with(context)
                .load(logoResource)
                .fit()
                .into(holder.campusLogo);

        holder.campusText.setText(uniCampus.getName());


        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                //For use of TP-API
                if(uniCampus.getCampusCode().equals("uib")){
                    DataManager dataManager = new DataManager(context);
                    dataManager.writeCurrentUniCampusSharedPref(campuses.get(position));

                    Intent i = new Intent(context, LoadActivity.class);
                    context.startActivity(i);
                    //For use of scraper-API
                } else {
                    JsoupTask jsoupTask = new JsoupTask(context, context.getResources().getString(R.string.load_data_string), campuses.get(position));
                    jsoupTask.execute();


                }



            }
        });

        return row;
    }

    /**
     * Creates a dialog that display the areas of the selected campus. Allows user to select an area to load the data from
     *
     * @param selectedUniCampus the unicampus to download buildings and rooms from
     * @param areaList list of areas generated earlier when user clicked the actual uni in the activity
     */
    private void createAreaDialog(final UniCampus selectedUniCampus, List<String> areaList) {
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.DialogBlueTheme)
                .setView(R.layout.dialog_area_chooser)
                .create();
        dialog.setCancelable(true);
        dialog.show();

        TextView areaDialogTitle = (TextView) dialog.findViewById(R.id.dialog_area_chooser_title);
        areaRadioGroup = (RadioGroup) dialog.findViewById(R.id.dialog_area_chooser_radio_group);

        //Generating radio buttons based on number of ares
        areaDialogTitle.setText(context.getResources().getString(R.string.unicampus_choose_area) + " " + selectedUniCampus.getAcronym());
        final RadioButton[] rb = new RadioButton[areaList.size()];
        for(int i = 0; i < areaList.size(); i++){
            rb[i] = new RadioButton(context);
            areaRadioGroup.addView(rb[i]);
            rb[i].setId(i);
            rb[i].setPadding(16, 16, 16, 16);
            rb[i].setText(areaList.get(i));

        }
        //Sets
        areaRadioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged (RadioGroup group,int checkedId){
                        Context context = getContext();
                        RadioButton thisButton = (RadioButton) dialog.findViewById(checkedId);
                        localAreaCode = thisButton.getText().toString();
                        floatingActionButtonCalendarListener(context, dialog, selectedUniCampus, localAreaCode);

                    }
                }
        );
    }

    /**
     *
     * @param context activity context
     * @param dialog the dialog the FAB belongs to
     * @param selectedUniCampus the selected campus from when user clicked the on the campus list
     * @param areaCode the area code that was selected in the dialog
     */
    private void floatingActionButtonCalendarListener(final Context context, AlertDialog dialog, final UniCampus selectedUniCampus, final String areaCode) {


        FloatingActionButton fab = (FloatingActionButton) dialog.findViewById(R.id.dialog_area_chooser_fab);
        if(fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataManager dataManager = new DataManager(context);
                    dataManager.writeCurrentUniCampusSharedPref(selectedUniCampus);
                    Snackbar snack = Snackbar.make(view, areaCode + " " + context.getResources().getString(R.string.unicampus_chosen_area), Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View sbView = snack.getView();

                    //Set custom typeface
                    TextView tv = (TextView) (sbView).findViewById(android.support.design.R.id.snackbar_text);
                    Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_thin.ttf");
                    tv.setTypeface(font);
                    tv.setTextSize(16);
                    snack.show();

                    //int color = theme.getAttributeColor(context, R.attr.colorPrimary);
                        sbView.setBackgroundColor(context.getResources().getColor(R.color.primaryDark_blue));

                    Log.d("FAB", areaCode + "");
                    //Writes the area code into shared pref so that the datamanager later can get it easier, see "checkIfDataHasBeenLoadedBefore()" in DataManager for wher it's primarily used
                    dataManager.setSelectedAreaCode(areaCode);
                    Intent i=new Intent(context,LoadActivity.class);
                    context.startActivity(i);

                }
            });
    }

    private class CampusHolder
    {
        TextView campusText;
        ImageView campusImage;
        ImageView campusLogo;

    }



    private class JsoupTask extends AsyncTask<Void, Void, List<String>> {
        private UniCampus selectedCampus;
        private Context context;
        private ProgressDialog asyncDialog;
        private SpannableString loadSpanString;

        /**
         *  selectedCampus is a param for sending the campus so that we can use its campus code to download the areas of the uni
         *
         * @param context activity context
         * @param loadDataString loading string
         * @param selectedCampus to get the campus code from
         */
        public JsoupTask(Context context, String loadDataString, UniCampus selectedCampus){
            super();
            this.context = context;
            this.selectedCampus = selectedCampus;
            asyncDialog = new ProgressDialog(context, R.style.DialogBlueTheme);


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
        protected List<String> doInBackground(Void... param) {

            DataManager dataManager = new DataManager(context);

            return dataManager.downloadAreas(selectedCampus.getCampusCode());
        }

        /**
         * dismisses the dialog and starts the BuildingMainActivity
         * @param areas a list of the buildings, again standard param for onPostExecute
         */
        protected void onPostExecute(List<String> areas){

            asyncDialog.dismiss();

            createAreaDialog(selectedCampus, areas);

        }

    }

}
