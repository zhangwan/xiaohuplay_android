package com.tinytiger.common.view.textview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/*
 * @author zwy
 * create at 2020/5/15 0015
 * description:
 */
public class CenteredImageSpan extends ImageSpan {

    private  int mMarginLeft;
    private  int mMarginRight;
    private WeakReference<Drawable> mDrawableRef;

    public CenteredImageSpan(Drawable drawable) {
        super(drawable);
    }

    public CenteredImageSpan(Drawable drawable,int marginLeft, int marginRight) {
        super(drawable);
        mMarginLeft = marginLeft;
        mMarginRight = marginRight;
    }



    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     @NonNull Paint paint) {

//        Drawable b =  getDrawable();
//        canvas.save();
//        //与descent线对齐
//        canvas.translate(x, paint.getFontMetricsInt().descent);
//        b.draw(canvas);
//        canvas.restore();
        Drawable b = getDrawable();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        x = mMarginLeft + x;
        int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;
        canvas.save();
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return mMarginLeft + super.getSize(paint, text, start, end, fm) + mMarginRight;

    }


}
