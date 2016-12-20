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
import com.pensive.android.romplanuib.models.UIBroom;
import com.pensive.android.romplanuib.util.Randomized;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class RoomAdapter extends ArrayAdapter<UIBroom> {


    LayoutInflater inflater;
    Context context;
    int textViewResourceId;
    List<UIBroom> uiBrooms;
    String buildingCode;
    com.pensive.android.romplanuib.util.StringCleaner stringCleaner = new com.pensive.android.romplanuib.util.StringCleaner();
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

            row.setTag(holder);


        } else {

            holder = (RoomHolder)row.getTag();
        }


        //http://rom_img.app.uib.no/byggogrombilder/GR_/GR_110/GR_110I.jpg
        String url = "http://rom_img.app.uib.no/byggogrombilder/" + buildingCode + "_/"+ buildingCode + "_" + uiBrooms.get(position).getCode() + "/"+ buildingCode + "_" + uiBrooms.get(position).getCode() + "I.jpg";
        Picasso.with(context)
                .load(URLEncoding.encode(url))
                .centerCrop()
                .fit()
                .placeholder(R.drawable.uiblogo)
                .transform(new GrayscaleTransformation())
                .into(holder.roomImage);


        UIBroom uiBroom = uiBrooms.get(position);
        holder.roomText.setText(uiBroom.getName());



        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(context,
                        WeekCalendarActivity.class);
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

    }
}
