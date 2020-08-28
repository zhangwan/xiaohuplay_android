package com.tinytiger.titi.widget.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import com.tinytiger.titi.R;

import razerdp.basepopup.BasePopupWindow;

/*
 * @author Tamas
 * create at 2020/3/25 0025
 * Email: ljw_163mail@163.com
 * description: 搜索 提示 popup window
 */
public class PopupTips extends BasePopupWindow {
    public PopupTips(Context context) {
        super(context);
    }

    public PopupTips(Context context, boolean delayInit) {
        super(context, delayInit);
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_window_tips);
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

}
