package com.pensive.android.romplanuib.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Various methods for setting the fonts
 *
 * @author Edvard Bjørgen
 * @version 1.0
 */

public class FontController {

    public void setActionBarTitleFont(Context context, String toolbarTitle, Toolbar actionBar, String fontType, int size){
        SpannableString title = new SpannableString(toolbarTitle);

        // Add a span for the custom font font
        title.setSpan(new TypefaceSpan(fontType), 0, title.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        actionBar.setTitle(title);
    }

    public Typeface getTypeface(Context context, String font){
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}
