package com.tinytiger.common.utils.umeng

import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.R
import com.umeng.analytics.MobclickAgent


/**
 *
 * @author zhw_luke
 * @date 2020/6/9 0009 下午 5:52
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 友盟首页事件
 */
object HomeAgentUtils {

    /**
     * 首页显示
     */
    fun setHomeShow() {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        LoginAgentUtils.setlog( "XH_003", map)
    }

    /**
     * 首页进入游戏分类
     */
    fun setCategoryShow() {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        LoginAgentUtils.setlog( "XH_004", map)
    }

    /**
     * 首页进入游戏排行榜
     */
    fun setLibraryShow() {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        LoginAgentUtils.setlog( "XH_005", map)
    }
    /**
     * 首页进入安利墙
     */
    fun setAmwayShow() {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        LoginAgentUtils.setlog("XH_006", map)
    }
    /**
     * 首页进入资讯
     */
    fun setNewsShow() {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        LoginAgentUtils.setlog("XH_007", map)
    }
}