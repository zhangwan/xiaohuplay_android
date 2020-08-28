package com.tinytiger.common.view.textview;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.tinytiger.common.R;


/**
 * Created by yang1006 on 17/7/13.
 * 显示全文的span
 */

public class ShowAllSpan extends ClickableSpan {

    private OnAllSpanClickListener clickListener;
    private Context context;
    private int type;
    public ShowAllSpan(Context context,int type,OnAllSpanClickListener clickListener){
        this.context = context;
        this.type=type;
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View widget) {
        if (clickListener != null){
            clickListener.onClick(widget,type);
        }
    }

    public interface OnAllSpanClickListener{
        void onClick(View widget,int type);
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setARGB(255, 255, 255, 255);
        ds.setColor(ContextCompat.getColor(context, R.color.color_bg));
        ds.setUnderlineText(false);
        ds.setColor(ContextCompat.getColor(context, R.color.color_ffcc03));

    }
}
