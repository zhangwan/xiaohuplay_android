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
 * @doc 举报
 */
public class User2Popup extends BasePopupWindow {

    public User2Popup(Context context) {
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
        return createPopupById(R.layout.item_pop_user);
    }



    public void setReport(View.OnClickListener listener){
        TextView tvBack=findViewById(R.id.tvReport);
        tvBack.setOnClickListener(listener);
    }

}
