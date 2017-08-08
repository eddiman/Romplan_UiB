package com.pensive.android.romplanuib.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.pensive.android.romplanuib.RoomsActivity;
import com.pensive.android.romplanuib.R;
import com.pensive.android.romplanuib.models.Building;
import com.pensive.android.romplanuib.util.FontController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class BuildingAdapter extends ArrayAdapter<Building> implements SectionIndexer {

    HashMap<String, Integer> mapIndex;
    LayoutInflater inflater;
    Context context;
    int textViewResourceId;
    List<Building> buildings;
    FontController fc = new FontController();
    Typeface bebasFont;
    String[] sections;


    public BuildingAdapter(Context context, int textViewResourceId, List<Building> buildings) {
        super(context, textViewResourceId, buildings);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.buildings = buildings;
        this.bebasFont = fc.getTypeface(getContext(), "bebas_neue.ttf");


        mapIndex = new LinkedHashMap<String, Integer>();

        for (int x = 0; x < buildings.size(); x++) {
            String building = buildings.get(x).getBuildingAcronym();
            String ch = building.substring(0, 1);
            ch = ch.toUpperCase();

            // HashMap will prevent duplicates
            mapIndex.put(ch, x);
        }

        Set<String> sectionLetters = mapIndex.keySet();

        // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

        Collections.sort(sectionList);

        sections = new String[sectionList.size()];

        sectionList.toArray(sections);

    }

    public int getCount() {
        return buildings.size();
    }
    public Building getItem(int position) {
        return buildings.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        BuildingHolder holder = null;

        if (row == null) {
            holder = new BuildingHolder();
            row = inflater.inflate(textViewResourceId, parent, false);


            holder.buildCode = (TextView) row.findViewById(R.id.building_code);
            holder.buildText = (TextView) row.findViewById(R.id.building_name);
            row.setTag(holder);
        } else {

            holder = (BuildingHolder)row.getTag();
        }

        Building uibBuilding = buildings.get(position);
        holder.buildCode.setTypeface(bebasFont);
        holder.buildText.setText(uibBuilding.getName());
        holder.buildCode.setText(uibBuilding.getBuildingAcronym());


        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                //Toast.makeText(context, getItem(position).getListOfRooms().get(0).getCode(), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, RoomsActivity.class);
                i.putExtra("building", getItem(position));
                context.startActivity(i);

            }
            });

        return row;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mapIndex.get(sections[sectionIndex]);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    private class BuildingHolder
    {
        TextView buildText;
        TextView buildCode;
    }
}
