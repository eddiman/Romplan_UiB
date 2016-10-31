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
                return R.color.primary_blue;
            case 2:
                return R.color.primaryDark_blue;
            case 3:
                return R.color.background_blue_dark;
        }
    }

}
