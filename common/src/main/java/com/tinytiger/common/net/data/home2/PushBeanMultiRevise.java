package com.tinytiger.common.net.data.home2;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.center.MineGameBean;

import java.util.ArrayList;

/**
 * @author luke
 * @date 2018/9/1 13:54
 * @doc
 */
public class PushBeanMultiRevise implements MultiItemEntity {

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public PushBeanMultiRevise(int itemType) {
        this.itemType =itemType;
    }

    public PushBeanMultiRevise(AmwayWallListBean bean) {
        this.itemType =3;
        mAmwayWallListBean=bean;
    }
    public PushBeanMultiRevise(int itemType, HomeRecommendBean bean2){
        this.itemType=itemType;
        homeBeans=bean2;
    }

    /**
     * q缺省文字
     */
    public String defaulttxt = "";
    public int id = 0;
    public AmwayWallListBean mAmwayWallListBean;

    public HomeRecommendBean homeBeans;
}	
