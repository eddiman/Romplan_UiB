package com.pensive.android.romplanuib.util;

import android.graphics.Color;

import com.pensive.android.romplanuib.R;

import java.util.Random;

/**
 * Created by EddiStat on 05.06.2016.
 */
public class Randomized {
    private static final Random RANDOM = new Random();

    public int getRandomColorFilter() {
        switch (RANDOM.nextInt(9)) {
            default:
            case 0:
                return Color.argb(255, RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
            /*case 2:
                return R.color.color2;
            case 3:
                return R.color.color3;
            case 4:
                return R.color.color4;
            case 5:
                return R.color.color5;
            case 6:
                return R.color.color6;
            case 7:
                return R.color.color7;
            case 8:
                return R.color.color8;
            case 9:
                return R.color.color9;*/
        }
    }

}
