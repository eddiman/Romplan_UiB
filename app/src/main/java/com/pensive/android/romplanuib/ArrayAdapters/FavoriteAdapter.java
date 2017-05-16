package com.pensive.android.romplanuib.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pensive.android.romplanuib.R;
import com.pensive.android.romplanuib.BuildingActivity;
import com.pensive.android.romplanuib.WeekCalendarActivity;
import com.pensive.android.romplanuib.models.UIBroom;
import com.pensive.android.romplanuib.models.UiBunit;
import com.pensive.android.romplanuib.util.FontController;

import java.util.Calendar;
import java.util.List;

/**
 * @author Edvard Bjørgen & Fredrik Heimsæter
 * @version 1.0
 */
public class FavoriteAdapter extends ArrayAdapter<UiBunit> {


    LayoutInflater inflater;
    Context context;
    int textViewResourceId;
    List<UiBunit> units;
    FontController fc = new FontController();
    Typeface bebasFont;


    public FavoriteAdapter(Context context, int textViewResourceId, List<UiBunit> units) {
        super(context, textViewResourceId, units);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.units = units;
        this.bebasFont = fc.getTypeface(getContext(), "bebas_neue.ttf");

    }

    public int getCount() {
        return units.size();
    }
    public UiBunit getItem(int position) {
        return units.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        UnitHolder holder = null;

        if (row == null) {
            holder = new UnitHolder();
            row = inflater.inflate(textViewResourceId, parent, false);


            holder.buildCode = (TextView) row.findViewById(R.id.favorite_building_code);
            holder.unitName = (TextView) row.findViewById(R.id.favorite_name);
            row.setTag(holder);
        } else {
            holder = (UnitHolder)row.getTag();
        }

        UiBunit uibUnit = units.get(position);
        holder.buildCode.setTypeface(bebasFont);
        String name = uibUnit.getName();
        holder.unitName.setText(name);
        holder.buildCode.setText(uibUnit.getBuildingAcronym());



        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                //Toast.makeText(context, getItem(position).getListOfRooms().get(0).getCode(), Toast.LENGTH_SHORT).show();

                if(getItem(position) instanceof UIBroom) {
                    Intent i = new Intent(context, WeekCalendarActivity.class);
                    i.putExtra("room", getItem(position));
                    i.putExtra("currentWeek", getWeekNumber());
                    context.startActivity(i);
                }else {
                    Intent i = new Intent(context, BuildingActivity.class);
                    i.putExtra("building", getItem(position));
                    context.startActivity(i);
                }

            }
            });

        return row;
    }

    private class UnitHolder
    {
        TextView unitName;
        TextView buildCode;
    }
    /**
     * Gets the current week number, to send to next activity with the room.
     * @return the current week
     */
    public String getWeekNumber(){
        Calendar c = Calendar.getInstance();
        //Normalizing the weeks, Java calculates the range of week going from 1 to 53, week definitions changes depending on local/region set on phone
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        int week = c.get(Calendar.WEEK_OF_YEAR);


        return Integer.toString(week);
    }
}
