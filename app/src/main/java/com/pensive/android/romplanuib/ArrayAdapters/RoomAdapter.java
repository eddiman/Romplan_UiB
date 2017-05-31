package com.pensive.android.romplanuib.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pensive.android.romplanuib.WeekCalendarActivity;
import com.pensive.android.romplanuib.R;
import com.pensive.android.romplanuib.io.util.URLEncoding;
import com.pensive.android.romplanuib.models.Room;
import com.pensive.android.romplanuib.models.UniCampus;
import com.pensive.android.romplanuib.util.DataManager;
import com.pensive.android.romplanuib.util.Randomized;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class RoomAdapter extends ArrayAdapter<Room> {


    LayoutInflater inflater;
    Context context;
    int textViewResourceId;
    List<Room> rooms;
    String buildingCode;
    Randomized randomized = new Randomized();
    UniCampus selectedCampus;

    public RoomAdapter(Context context, int textViewResourceId, List<Room> buildings) {
        super(context, textViewResourceId, buildings);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.rooms = buildings;


    }

    public int getCount() {
        return rooms.size();
    }
    public Room getItem(int position) {
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
        buildingCode = rooms.get(position).getBuildingAcronym();
        DataManager dataManager = new DataManager(context);
        selectedCampus = dataManager.loadCurrentUniCampusSharedPref();

        if (row == null) {
            holder = new RoomHolder();
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(textViewResourceId, parent, false);

            holder.roomImage = (ImageView) row.findViewById(R.id.card_image);
            holder.roomText = (TextView) row.findViewById(R.id.card_string1);

            row.setTag(holder);


        } else {

            holder = (RoomHolder)row.getTag();
        }


        //http://rom_img.app.uib.no/byggogrombilder/GR_/GR_110/GR_110I.jpg
        int imageResource = context.getResources().getIdentifier(selectedCampus.getLogoUrl(), null, context.getPackageName());

        String url = "http://rom_img.app.uib.no/byggogrombilder/" + buildingCode + "_/"+ buildingCode + "_" + rooms.get(position).getAreaID() + "/"+ buildingCode + "_" + rooms.get(position).getAreaID() + "I.jpg";
        Picasso.with(context)
                .load(URLEncoding.encode(url))
                .centerCrop()
                .fit()
                .placeholder(imageResource)
                .transform(new GrayscaleTransformation())
                .into(holder.roomImage);


        Room room = rooms.get(position);
        holder.roomText.setText(room.getName());



        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(context,
                        WeekCalendarActivity.class);
                i.putExtra("room", getItem(position));
                i.putExtra("currentWeek", getWeekNumber());
                context.startActivity(i);

            }
            });

        return row;
    }



    private class RoomHolder
    {
        TextView roomText;
        ImageView roomImage;

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
