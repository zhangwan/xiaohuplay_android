package com.tinytiger.common.view.text;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/*
 * @author Tamas
 * create at 2019/11/15 0015
 * Email: ljw_163mail@163.com
 * description: 数字字体
 */
public class NumTypeTextView extends AppCompatTextView {
    public NumTypeTextView(Context context) {
        super(context);
        init(context);
    }

    public NumTypeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NumTypeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface mTypeface = Typeface.createFromAsset(context.getAssets(), "DINMittelschrift.otf");
        setTypeface(mTypeface);
    }


}