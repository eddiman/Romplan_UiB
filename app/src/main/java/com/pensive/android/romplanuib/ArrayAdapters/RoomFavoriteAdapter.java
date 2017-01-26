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
import com.pensive.android.romplanuib.WeekCalendarActivity;
import com.pensive.android.romplanuib.models.UIBroom;
import com.pensive.android.romplanuib.util.FontController;
import com.pensive.android.romplanuib.util.StringCleaner;

import java.util.Calendar;
import java.util.List;

/**
 * @author Edvard Bjørgen & Fredrik Heimsæter
 * @version 1.0
 */
public class RoomFavoriteAdapter extends ArrayAdapter<UIBroom> {


    LayoutInflater inflater;
    Context context;
    int textViewResourceId;
    List<UIBroom> rooms;
    StringCleaner sc = new StringCleaner();
    FontController fc = new FontController();
    Typeface bebasFont;


    public RoomFavoriteAdapter(Context context, int textViewResourceId, List<UIBroom> rooms) {
        super(context, textViewResourceId, rooms);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.rooms = rooms;
        this.bebasFont = fc.getTypeface(getContext(), "bebas_neue.ttf");

    }

    public int getCount() {
        return rooms.size();
    }
    public UIBroom getItem(int position) {
        return rooms.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        RoomHolder holder = null;

        if (row == null) {
            holder = new RoomHolder();
            row = inflater.inflate(textViewResourceId, parent, false);


            holder.buildCode = (TextView) row.findViewById(R.id.favorite_room_building_code);
            holder.roomName = (TextView) row.findViewById(R.id.favorite_room_name);
            row.setTag(holder);
        } else {

            holder = (RoomHolder)row.getTag();
        }

        UIBroom uibRoom = rooms.get(position);
        holder.buildCode.setTypeface(bebasFont);
        holder.roomName.setText(uibRoom.getName());
        holder.buildCode.setText(sc.createBuildingCode(uibRoom.getBuilding()));


        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                //Toast.makeText(context, getItem(position).getListOfRooms().get(0).getCode(), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, WeekCalendarActivity.class);
                i.putExtra("room", getItem(position));
                i.putExtra("currentWeek", getWeekNumber());
                context.startActivity(i);

            }
            });

        return row;
    }

    private class RoomHolder
    {
        TextView roomName;
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
