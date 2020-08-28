package com.tinytiger.common.net.data.msg;

import com.chad.library.adapter.base.entity.MultiItemEntity;


public class CommentBeanMulti implements MultiItemEntity {


	public int itemType;

	@Override
	public int getItemType() {
		return itemType;
	}

	public CommentBeanMulti(int itemType, String defaulttxt){
		this.itemType=itemType;
		this.defaulttxt=defaulttxt;
	}

	public CommentBeanMulti(ReplyBean mReplyBean){
		this.itemType=2;
		this.mReplyBean=mReplyBean;
	}




	/**
	 * q缺省文字
	 */
	public String defaulttxt="";

	/**
	 * 类型
	 */
	public int type;

	public ReplyBean mReplyBean;


}	
