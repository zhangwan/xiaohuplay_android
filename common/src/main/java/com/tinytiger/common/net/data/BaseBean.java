package com.tinytiger.common.net.data;

import java.io.Serializable;

/**
 *
 * @author zhw_luke
 * @date 2019/11/14 0014 下午 8:36
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
public class BaseBean implements Serializable {
	/**
	*状态码;
	 */
	
	public int code;
	/**
	*消息;
	 */
	public String msg;

	/**
	 * 1成功，0失败
	 * 不要使用它,它代表接可以连接服务器,不代表数据正确
	 */
	//public boolean status;

}	
