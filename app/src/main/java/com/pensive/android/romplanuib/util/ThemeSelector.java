package com.pensive.android.romplanuib.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;

import com.pensive.android.romplanuib.R;

/**
 * Created by moled on 23.05.2017.
 */

public class ThemeSelector {


    public int getDialogThemeFromCampusCode(Context context, String campusCode){
        switch (campusCode){
            case "uib":
                return context.getTheme().getResources().getIdentifier("DialogBlueTheme", "style", context.getPackageName());

            case "uio":
                return context.getTheme().getResources().getIdentifier("DialogGreenTheme", "style", context.getPackageName());
        }
        return 0;
    }

    public int getThemeFromCampusCode(Context context, String campusCode){
        switch (campusCode){
            case "uib":
                return context.getTheme().getResources().getIdentifier("BlueTheme", "style", context.getPackageName());
            case "uio":
                return context.getTheme().getResources().getIdentifier("GreenTheme", "style", context.getPackageName());
        }
        return 0;
    }

    public int getAttributeColor(Context context, int attributeId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeId, typedValue, true);
        int colorRes = typedValue.resourceId;
        int color = -1;
        try {
            color = context.getResources().getColor(colorRes);
        } catch (Resources.NotFoundException e) {
            Log.w("", "Not found color resource by id: " + colorRes);
        }
        return color;
    }
}
