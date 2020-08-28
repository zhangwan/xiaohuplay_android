package com.tinytiger.common.net.data.home2;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.center.MineGameBean;
import com.tinytiger.common.net.data.gametools.GameInfoBean;

import java.util.ArrayList;

/**
 * @author luke
 * @date 2018/9/1 13:54
 * @doc
 */
public class PushBeanMulti implements MultiItemEntity {

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public PushBeanMulti(int itemType) {
        this.itemType =itemType;
    }

    public PushBeanMulti(AmwayWallListBean bean) {
        this.itemType =2;
        mAmwayWallListBean=bean;
    }
    public PushBeanMulti(int itemType ,ArrayList<MineGameBean> bean) {
        this.itemType =itemType;
        listBean=bean;
    }
    public PushBeanMulti(int itemType,ArrayList<HomeRecommendBean> bean2,int tag){
        this.itemType=itemType;
        homeBeans=bean2;
    }

    /**
     * q缺省文字
     */
    public String defaulttxt = "";
    public int id = 0;
    public AmwayWallListBean mAmwayWallListBean;
    public ArrayList<MineGameBean> listBean;

    public ArrayList<HomeRecommendBean> homeBeans;
}	
