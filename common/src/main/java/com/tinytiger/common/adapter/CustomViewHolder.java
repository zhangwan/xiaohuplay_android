package com.tinytiger.common.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tinytiger.common.R;
import com.tinytiger.common.net.data.AdBean;
import com.tinytiger.common.utils.image.GlideUtil;
import com.ms.banner.holder.BannerViewHolder;

/**
 * @author zhw_luke
 * @date 2020/6/8 0008 下午 3:30
 * @Copyright 小虎互联科技
 * @doc banner基础通用操作
 * @since 3.2.0
 */
public class CustomViewHolder implements BannerViewHolder<AdBean> {

    @Override
    public View createView(Context context, int position, AdBean data) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_base, null);
        ImageView ivBanner = view.findViewById(R.id.ivBanner);
        GlideUtil.loadImg(ivBanner, data.image);
        return view;
    }
}


