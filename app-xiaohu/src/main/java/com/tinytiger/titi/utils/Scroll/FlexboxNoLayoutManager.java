package com.tinytiger.titi.utils.Scroll;

import android.content.Context;


import com.google.android.flexbox.FlexboxLayoutManager;

/**
 * 实现RecycleView分页滚动的工具类
 * Created by zhuguohui on 2016/11/10.
 */

public class FlexboxNoLayoutManager extends FlexboxLayoutManager {


    public FlexboxNoLayoutManager(Context context) {
        super(context);
    }
    private boolean isScrollEnabled = true;
    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
