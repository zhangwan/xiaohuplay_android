package com.tinytiger.common.view.textview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.tinytiger.common.R;


/**
 * Created by zwy on 17/7/13.
 * 显示全文的span
 */

public class ShowLabelSpan extends ClickableSpan {

    private ShowLabelSpan.OnLabelSpanClickListener clickListener;
    private boolean isPressed = false;
    private Context context;
    private int index;
    private String type;
    public ShowLabelSpan(Context context, int index, String type, ShowLabelSpan.OnLabelSpanClickListener clickListener){
        this.context = context;
        this.index=index;
        this.type=type;
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View widget) {
        if (clickListener != null){
            if(ShowAllTextView.TEXT.equals(type)){
                clickListener.onLabelOnClick(widget,index,type);
            }

        }
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public interface OnLabelSpanClickListener{
        void onLabelOnClick(View widget, int index, String type);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setARGB(255, 255, 255, 255);
        ds.setColor(ContextCompat.getColor(context, R.color.color_bg));
        ds.setColor(ContextCompat.getColor(context, R.color.color_ff556e));
        ds.setUnderlineText(false);

    }

}
