package com.pensive.android.romplanuib.util;

import android.content.Context;

/**
 * Created by moled on 23.05.2017.
 */

public class ThemeSelector {


    public int getDialogTheme(Context context, String campusCode){
        switch (campusCode){
            case "uib":
                return context.getTheme().getResources().getIdentifier("DialogRedTheme", "style", context.getPackageName());

        }


        return 0;
    }
}
