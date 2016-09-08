package com.pensive.android.romplanuib.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pensive.android.romplanuib.CalendarActivity;
import com.pensive.android.romplanuib.R;
import com.pensive.android.romplanuib.models.CalActivity;
import com.pensive.android.romplanuib.models.UIBroom;
import com.pensive.android.romplanuib.util.Randomized;
import com.pensive.android.romplanuib.util.StringCleaner;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by EddiStat on 24.05.2016.
 */
public class RoomAdapter extends ArrayAdapter<UIBroom> {


    LayoutInflater inflater;
    Context context;
    int textViewResourceId;
    List<UIBroom> uiBrooms;
    String buildingCode;
    StringCleaner stringCleaner = new StringCleaner();
    Randomized randomized = new Randomized();

    public RoomAdapter(Context context, int textViewResourceId, List<UIBroom> buildings) {
        super(context, textViewResourceId, buildings);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.uiBrooms = buildings;


    }

    public int getCount() {
        return uiBrooms.size();
    }
    public UIBroom getItem(int position) {
        return uiBrooms.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        RoomHolder holder = null;
        buildingCode = stringCleaner.createBuildingCode(uiBrooms.get(position).getBuilding());

        if (row == null) {
            holder = new RoomHolder();
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(textViewResourceId, parent, false);

            holder.roomImage = (ImageView) row.findViewById(R.id.card_image);
            holder.roomText = (TextView) row.findViewById(R.id.card_string1);
            holder.roomButton = (Button) row.findViewById(R.id.calendar_button);

            row.setTag(holder);


        } else {

            holder = (RoomHolder)row.getTag();
        }


        //http://rom_img.app.uib.no/byggogrombilder/GR_/GR_110/GR_110I.jpg
        String url = "http://rom_img.app.uib.no/byggogrombilder/" + buildingCode + "_/"+ buildingCode + "_" + uiBrooms.get(position).getName() + "/"+ buildingCode + "_" + uiBrooms.get(position).getName() + "I.jpg";
        Picasso.with(context)
                .load(url)
                .centerCrop()
                .fit()
                .placeholder(R.drawable.uiblogo)
                .into(holder.roomImage);


        UIBroom uiBroom = uiBrooms.get(position);
        holder.roomText.setText("Rom: "+ uiBroom.getName());



        holder.roomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(context,
                        CalendarActivity.class);
                i.putExtra("room", getItem(position));
                context.startActivity(i);

            }
            });

        return row;
    }



    private class RoomHolder
    {
        TextView roomText;
        ImageView roomImage;
        Button roomButton;

    }
}
