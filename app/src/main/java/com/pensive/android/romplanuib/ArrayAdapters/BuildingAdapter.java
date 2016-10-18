package com.pensive.android.romplanuib.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pensive.android.romplanuib.R;
import com.pensive.android.romplanuib.RoomActivity;
import com.pensive.android.romplanuib.models.UIBbuilding;

import java.util.List;

/**
 * Created by EddiStat on 24.05.2016.
 */
public class BuildingAdapter extends ArrayAdapter<UIBbuilding> {


    LayoutInflater inflater;
    Context context;
    int textViewResourceId;
    List<UIBbuilding> buildings;



    public BuildingAdapter(Context context, int textViewResourceId, List<UIBbuilding> buildings) {
        super(context, textViewResourceId, buildings);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.buildings = buildings;
    }

    public int getCount() {
        return buildings.size();
    }
    public UIBbuilding getItem(int position) {
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
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(textViewResourceId, parent, false);


            holder.buildText = (TextView) row.findViewById(R.id.building_name);
            row.setTag(holder);
        } else {

            holder = (BuildingHolder)row.getTag();
        }

        UIBbuilding uibBuilding = buildings.get(position);
        holder.buildText.setText(uibBuilding.getName());


        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Toast.makeText(context, getItem(position).getListOfRooms().get(0).getCode(), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, RoomActivity.class);
                i.putExtra("building", getItem(position));
                context.startActivity(i);

            }
            });

        return row;
    }

    private class BuildingHolder
    {
        TextView buildText;

    }
}
