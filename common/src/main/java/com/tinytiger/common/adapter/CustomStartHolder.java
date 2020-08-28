package com.tinytiger.common.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.tinytiger.common.R;
import com.ms.banner.holder.BannerViewHolder;


/**
 * @author zhw_luke
 * @date 2019/11/7 0007 上午 11:58
 * @Copyright 小虎互联科技
 * @doc banner基础通用操作
 * @since 5.0.0
 */
public class CustomStartHolder implements BannerViewHolder<Integer> {

    @Override
    public View createView(Context context, int position, Integer data) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_image, null);
        ImageView ivBanner = view.findViewById(R.id.ivBanner);
        ivBanner.setImageDrawable(ContextCompat.getDrawable(context,data));
        return view;
    }
}


