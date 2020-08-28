package com.tinytiger.common.net.data.home;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.AdBean;


import java.util.ArrayList;

/**
 * @author luke
 * @date 2018/9/1 13:54
 * @doc
 */
public class RecommedBeanMulti implements MultiItemEntity {


    /**
     * 1 图文
     * 2 视频
     * 3 用户
     * 0 说明
     */
    public int itemType=0;

    //占屏幕比例
    public int spanSize = 1;

    @Override
    public int getItemType() {
        return itemType;
    }


    public RecommedBeanMulti(RecommendBean defaulttxt) {
        this.mRecommendBean = defaulttxt;
    }

    public RecommedBeanMulti(ArrayList<AdBean> mAdBean) {
        this.mAdBean = mAdBean;
        this.itemType = 1;
        this.spanSize = 2;
    }


    public RecommendBean mRecommendBean;
    public ArrayList<AdBean> mAdBean;
}	
