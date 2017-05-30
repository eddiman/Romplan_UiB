package com.pensive.android.romplanuib.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pensive.android.romplanuib.LoadActivity;
import com.pensive.android.romplanuib.R;
import com.pensive.android.romplanuib.models.Campus;
import com.pensive.android.romplanuib.models.Room;
import com.pensive.android.romplanuib.util.FontController;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class CampusAdapter extends ArrayAdapter<Campus> {


    LayoutInflater inflater;
    Context context;
    int textViewResourceId;
    List<Campus> campuses;
    FontController fc = new FontController();
    Typeface bebasFont;


    public CampusAdapter(Context context, int textViewResourceId, List<Campus> campuses) {
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
    public Campus getItem(int position) {
        return campuses.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        CampusAdapter.CampusHolder holder = null;

        if (row == null) {
            holder = new CampusAdapter.CampusHolder();
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(textViewResourceId, parent, false);

            holder.campusImage = (ImageView) row.findViewById(R.id.campus_card_image);
            holder.campusLogo = (ImageView) row.findViewById(R.id.campus_logo);
            holder.campusText = (TextView) row.findViewById(R.id.campus_card_string1);

            row.setTag(holder);


        } else {

            holder = (CampusAdapter.CampusHolder)row.getTag();
        }

        final Campus campus = campuses.get(position);
        Uri bgUri = Uri.parse("android.resource://com.pensive.android.romplanuib/drawable/splash_" + campus.getCampusCode());
        Uri logoUri = Uri.parse("android.resource://com.pensive.android.romplanuib/drawable/" + campus.getCampusCode() +"_logo_2");

        Picasso.with(context)
                .load(bgUri)
                .centerCrop()
                .fit()
                .into(holder.campusImage);

        Picasso.with(context)
                .load(logoUri)
                .fit()
                .into(holder.campusLogo);

        holder.campusText.setText(campus.getName());

        if()



        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                writeCampusCodeToSharedPref(context, campus.getCampusCode());
                Intent i = new Intent(context,
                        LoadActivity.class);
                context.startActivity(i);

            }
        });

        return row;
    }

    private void writeCampusCodeToSharedPref(Context context, String campusCode) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("campus_code", campusCode);
        editor.apply();

    }

    private class CampusHolder
    {
        TextView campusText;
        ImageView campusImage;
        ImageView campusLogo;

    }

}
