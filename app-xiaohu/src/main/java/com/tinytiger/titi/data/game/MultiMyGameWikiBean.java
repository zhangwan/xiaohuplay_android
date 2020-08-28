package com.tinytiger.titi.data.game;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.gametools.GameInfoBean;
import com.tinytiger.common.net.data.gametools.wiki.WikiCategoryBean;
import com.tinytiger.common.net.data.gametools.wiki.WikiModularList;

public class MultiMyGameWikiBean implements MultiItemEntity {



    /**
     * 0 title
     * 1 info
     * 2.top
     * #模板id 1=横图banner模板 2=图标banner模板 3=上图下文分类模板 4=纯文字标题模板 5=左图右文分类模板 6=申请管理员模板
     */
    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public WikiModularList.WikiModular mWikiModular = null;
    public WikiCategoryBean mWikiCategoryBean = null;


    public MultiMyGameWikiBean(WikiCategoryBean bean,int itemType){
        this.itemType =itemType;
        mWikiCategoryBean = bean;
    }


    public MultiMyGameWikiBean(WikiModularList.WikiModular bean){
        mWikiModular = bean;
        if (bean.template_id==1&&bean.child_data.size()==1){
            //单图,模式
            itemType = 7;
        }else {
            itemType = bean.template_id;
        }

    }

    public MultiMyGameWikiBean(int itemType){
        this.itemType =itemType;
    }



}
