package com.tinytiger.common.net.data.home2;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.ad.AdClassify;

import java.util.ArrayList;

/**
 * @author luke
 * @date 2018/9/1 13:54
 * @doc
 */
public class FindBeanMulti implements MultiItemEntity {

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public FindBeanMulti(int itemType) {
        this.itemType = itemType;
    }

    public FindBeanMulti(AmwayWallListBean bean) {
        this.itemType = 2;
        mAmwayWallListBean = bean;
    }

    public FindBeanMulti(int itemType, AdClassify bean) {
        this.itemType = itemType;
        listBean = bean;
    }

    public FindBeanMulti(int itemType, ArrayList<HomeBean> bean2, int tag) {
        this.itemType = itemType;
        homeBeans = bean2;
    }

    /**
     * q缺省文字
     */
    public String defaulttxt = "";
    public int id = 0;
    public AmwayWallListBean mAmwayWallListBean;
    public AdClassify listBean;

    public ArrayList<HomeBean> homeBeans;
}	
