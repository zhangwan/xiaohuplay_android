package com.tinytiger.titi.widget.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.tinytiger.titi.R;

import razerdp.basepopup.BasePopupWindow;

/**
 *
 * @author tamas
 * @date 2019/11/20 0020 下午 1:38
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 分享 道具兑换
 */
public class PropsPopup extends BasePopupWindow {

    public PropsPopup(Context context) {
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
        return createPopupById(R.layout.item_pop_props);
    }




    public void setExchangeListener(boolean is_show,View.OnClickListener listener){
        TextView tv_exchange=findViewById(R.id.tv_exchange);
        View line=findViewById(R.id.line);

        if(is_show){
            line.setVisibility(View.VISIBLE);
            tv_exchange.setVisibility(View.VISIBLE);
        }else{
            line.setVisibility(View.GONE);
            tv_exchange.setVisibility(View.GONE);
        }

        tv_exchange.setOnClickListener(listener);

    }

    public void setShareListener(View.OnClickListener listener){
        TextView tv_share=findViewById(R.id.tv_share);
        tv_share.setOnClickListener(listener);
    }

}
