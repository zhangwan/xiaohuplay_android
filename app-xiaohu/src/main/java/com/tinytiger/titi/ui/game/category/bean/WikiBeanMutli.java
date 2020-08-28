package com.tinytiger.titi.ui.game.category.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.wiki.MainWikiBean;

/*
 * @author Tamas
 * create at 2020/4/14 0014
 * Email: ljw_163mail@163.com
 * description:
 */
public class WikiBeanMutli implements MultiItemEntity {

    /**
     * 0 标题
     * 1 内容
     */
    public int itemType=0;


    @Override
    public int getItemType() {
        return itemType;
    }


    private String title;
    private MainWikiBean mWiki;

    public WikiBeanMutli(String title) {
        itemType = 0;
        this.title = title;
    }

    public WikiBeanMutli(MainWikiBean bean) {
        itemType = 1;

        this.mWiki = bean;
    }


    public String getTitle() {
        return title;
    }

    public MainWikiBean getWikiBean() {
        return mWiki;
    }
}
