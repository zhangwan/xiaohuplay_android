package com.tinytiger.common.net.data.search;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.circle.CircleBean;
import com.tinytiger.common.net.data.circle.PostBean;


/**
 *
 * @author zhw_luke
 * @date 2019/11/12 0012 上午 10:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索数据
 */
public class SearchBeanMulti implements MultiItemEntity {

	/**
	 * //0 全部 1游戏 2帖子 3圈子 4用户 5资讯
	 */
	private int itemType;

	@Override
	public int getItemType() {
		return itemType;
	}

	public SearchBeanMulti() {
		this.itemType = 10;
	}

	public SearchBeanMulti(String bean) {
		this.itemType = 0;
		this.defaulttxt = bean;
	}

	public SearchBeanMulti(SearchGameInfo bean) {
		this.mSearchGameInfo = bean;
		this.itemType = 1;
	}
	public SearchBeanMulti(SearchGameInfo bean,int itemType) {
		this.mSearchGameInfo = bean;
		this.itemType = 2;
	}


	public SearchBeanMulti(SearchUserInfo bean) {
		this.itemType = 5;
		this.mSearchUserInfo = bean;
	}

	public SearchBeanMulti(SearchNewsInfo bean) {
		// 1/2
		this.mSearchNewsInfo = bean;
		this.itemType = 6;
	}

	public SearchBeanMulti(PostBean bean) {
		this.mPostBean = bean;
		this.itemType = 4;
	}

	public SearchBeanMulti(CircleBean bean) {
		this.mCircleBean = bean;
		this.itemType = 3;
	}

	/**
	 * q缺省文字
	 */
	public String defaulttxt = "";

	public SearchGameInfo mSearchGameInfo;
	public SearchUserInfo mSearchUserInfo;
	public SearchNewsInfo mSearchNewsInfo;
	public PostBean mPostBean;
	public CircleBean mCircleBean;

}	
