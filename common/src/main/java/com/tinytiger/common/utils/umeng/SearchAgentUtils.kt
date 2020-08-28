package com.tinytiger.common.utils.umeng

import com.tinytiger.common.R
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.utils.preference.SpUtils
import com.umeng.analytics.MobclickAgent



/**
 *
 * @author zhw_luke
 * @date 2020/6/9 0009 下午 5:52
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 友盟游戏事件
 */
object SearchAgentUtils {

    /**
     * 点击搜索推荐词
     */
    fun setSearchRecommend(recmd_word:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["recmd_word"] =recmd_word
        LoginAgentUtils.setlog( "XH_023", map)
    }

    /**
     * 当点击实时匹配或搜索按钮，触发一次搜索时
     */
    fun setSearchWord(search_word:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["search_word"] =search_word
        LoginAgentUtils.setlog( "XH_024", map)
    }

    /**
     * 当触发一次搜索且无搜索结果时
     */
    fun setSearchWordNo(no_result:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["no_result"] =no_result
        LoginAgentUtils.setlog("XH_025", map)
    }

    /**
     * 搜索导向
     * 搜索触发成功出结果后，点击并进入结果页
     * 说明：
     * 游戏用0代表，
     * 帖子用1代表，
     * 圈子用2代表，
     * 用户用3代表，
     * 资讯用4代表
     */
    fun setSearchRoute(search_word:String,search_route:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["search_word"] =search_word
        map["search_route_tag"] =""+search_route
        LoginAgentUtils.setlog( "XH_026", map)
    }


    /**
     * 资讯-文章
     */
    fun setNewTxt(new_id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["new_id"] =new_id
        map["new_view"] =1
        LoginAgentUtils.setlog("XH_027", map)
    }

    /**
     * 资讯-视频
     */
    fun setNewVideo(new_id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["new_id"] =new_id
        map["new_view"] =1

        LoginAgentUtils.setlog("XH_028", map)
    }
}