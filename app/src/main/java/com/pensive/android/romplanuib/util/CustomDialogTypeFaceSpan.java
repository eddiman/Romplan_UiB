package com.pensive.android.romplanuib.util;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 *
 * Class for customizing fonts
 * @author Edvard Bjørgen
 * @version 1.0
 *
 * //TODO: Review if this class is really needed
 */

@SuppressLint("ParcelCreator")
public class CustomDialogTypeFaceSpan extends TypefaceSpan {
    private Typeface typeface;

    public CustomDialogTypeFaceSpan(Typeface typeface) {
        super("");
        this.typeface = typeface;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyTypeFace(ds, typeface);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyTypeFace(paint, typeface);
    }

    private static void applyTypeFace(Paint paint, Typeface tf) {
        paint.setTypeface(tf);
    }
}
