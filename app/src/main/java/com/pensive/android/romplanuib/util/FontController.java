package com.pensive.android.romplanuib.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.support.v7.widget.Toolbar;

/**
 * Created by Edvard on 01.11.2016.
 * Various methods for setting the fonts
 */

public class FontController {

    public void setTitleFont(Context context, String toolbarTitle, Toolbar actionBar, String fontType, int size){
        SpannableString title = new SpannableString(toolbarTitle);

        // Add a span for the custom font font
        title.setSpan(new TypefaceSpan(fontType), 0, title.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        actionBar.setTitle(title);
    }

    public Typeface getTypeface(Context context, String font){
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}
