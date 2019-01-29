package com.pensive.android.romplanuib.util;

import com.pensive.android.romplanuib.R;

import java.util.Random;

/**
 * @author Edvard Bjørgen
 * @version 1.0
 */
public class Randomized {
    private static final Random RANDOM = new Random();

    /**
     * Gets a random colorfilter
     * @return a random colorfilter
     */
    public int getRandomColorFilter() {
        switch (RANDOM.nextInt(3)) {
            default:
            case 0:
                return R.color.primary_blue;
            case 1:
                return R.color.primaryDark_blue;
            case 2:
                return R.color.background_blue_dark;
        }
    }

}
