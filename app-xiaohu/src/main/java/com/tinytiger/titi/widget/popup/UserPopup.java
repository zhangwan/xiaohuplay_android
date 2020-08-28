package com.tinytiger.titi.widget.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;


import com.tinytiger.titi.R;

import razerdp.basepopup.BasePopupWindow;

/**
 *
 * @author zhw_luke
 * @date 2019/11/20 0020 下午 1:38
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
public class UserPopup extends BasePopupWindow {

    public UserPopup(Context context) {
        super(context);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.item_pop_userblack);
    }


    public void setBack(String msg,View.OnClickListener listener){
        TextView tvBack=findViewById(R.id.tvBack);
        if(msg!=null &&!msg.isEmpty()){
            tvBack.setText(msg);
        }
        tvBack.setOnClickListener(listener);
    }

    public void setShare(View.OnClickListener listener){
        TextView tvBack=findViewById(R.id.tvShare);
        tvBack.setOnClickListener(listener);
    }
    public void setShare(String msg,View.OnClickListener listener){
        TextView tvShare=findViewById(R.id.tvShare);
        if(msg!=null &&!msg.isEmpty()){
            tvShare.setText(msg);
        }
        tvShare.setOnClickListener(listener);
    }
}
