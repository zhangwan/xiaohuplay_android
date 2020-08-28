package com.tinytiger.titi.widget.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.tinytiger.titi.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author zhw_luke
 * @date 2020/6/28 0028 下午 6:58
 * @Copyright 小虎互联科技
 * @doc 游戏操作
 * @since 3.0.0
 */
public class PopupGameInfo extends BasePopupWindow {
    TextView tvBack;

    public PopupGameInfo(Context context) {
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
        return createPopupById(R.layout.pop_game);
    }

    public void setPopInfo(String info, View.OnClickListener listener) {
        tvBack = findViewById(R.id.tvReport);
        tvBack.setText(info);
        tvBack.setOnClickListener(listener);
    }

    public void setDrawableLeft(int resourceId) {
        tvBack.setCompoundDrawablesWithIntrinsicBounds(
                getContext().getDrawable(resourceId), null, null, null);
    }

}
