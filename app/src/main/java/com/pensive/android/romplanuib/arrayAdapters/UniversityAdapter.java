package com.pensive.android.romplanuib.arrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pensive.android.romplanuib.LoadActivity;
import com.pensive.android.romplanuib.R;
import com.pensive.android.romplanuib.models.University;
import com.pensive.android.romplanuib.queries.UniversityQueries;
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
public class UniversityAdapter extends ArrayAdapter<University> {


    private FirebaseAnalytics mFirebaseAnalytics;
    private LayoutInflater inflater;
    private Context context;
    private int textViewResourceId;
    private List<University> campuses;
    private FontController fc = new FontController();
    private Typeface bebasFont;
    private UniversityQueries universityQueries;
    private DataManager dataManager;

    public UniversityAdapter(Context context, int textViewResourceId, List<University> campuses) {
        super(context, textViewResourceId, campuses);
        this.context = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.campuses = campuses;
        this.bebasFont = fc.getTypeface(getContext(), "bebas_neue.ttf");
        dataManager = new DataManager();
    }

    public int getCount() {
        return campuses.size();
    }
    public University getItem(int position) {
        return campuses.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent){
        View row = convertView;
        CampusHolder holder = null;

        if (row == null) {
            holder = new CampusHolder();
            row = inflater.inflate(textViewResourceId, parent, false);

            holder.universityImage = (ImageView) row.findViewById(R.id.campus_card_image);
            holder.universityLogo = (ImageView) row.findViewById(R.id.campus_logo);
            holder.universityText = (TextView) row.findViewById(R.id.university_card_string);

            row.setTag(holder);


        } else {

            holder = (CampusHolder)row.getTag();
        }

        final University university = campuses.get(position);
        Uri logoUri = Uri.parse(university.getLogoUrl());
        Uri bgUri = Uri.parse(university.getBgUrl());

        List<Transformation> transformations = new ArrayList<>();
        transformations.add(new GrayscaleTransformation());
        transformations.add(new ColorFilterTransformation(R.attr.transpImgColor));
        int bgResource = context.getResources().getIdentifier(university.getBgUrl(), null, context.getPackageName());
        int logoResource = context.getResources().getIdentifier(university.getLogoUrl(), null, context.getPackageName());

        Picasso.get()
                .load(bgResource)
                .centerCrop()
                .fit()
                .transform(transformations)
                .into(holder.universityImage);

        Picasso.get()
                .load(logoResource)
                .fit()
                .into(holder.universityLogo);

        holder.universityText.setText(university.getName());


        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Bundle params = new Bundle();
                params.putString("university_code", university.getCampusCode());
                params.putString("university_name", university.getName());
                mFirebaseAnalytics.logEvent("select_university", params);
                dataManager.storeObjectInSharedPref(context, "university", university);
                Intent i = new Intent(context, LoadActivity.class);
                context.startActivity(i);
            }
        });

        return row;
    }

    private class CampusHolder
    {
        TextView universityText;
        ImageView universityImage;
        ImageView universityLogo;

    }

}
