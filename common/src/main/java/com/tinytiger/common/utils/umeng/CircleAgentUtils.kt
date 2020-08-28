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
 * @doc 友盟圈子事件
 */
object CircleAgentUtils {


    /**
     * 圈子-推荐圈子
     * 当点击推荐圈子并成功打开页面时
     */
    fun setCircleRecommend(circle_id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["circle_id"] =circle_id

        LoginAgentUtils.setlog("XH_014", map)
    }
    /**
     * 进入百科主页
     */
    fun setWikeHome() {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")

        LoginAgentUtils.setlog("XH_029", map)
    }
    /**
     * 当XX游戏百科详情页被点击，记录一次并同时记录百科名称，
     */
    fun setWikeDetail(pedia_id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["pedia_id"] =pedia_id

        LoginAgentUtils.setlog("XH_030", map)
    }

    /**
     * 当XX游戏百科词条被点击，转发，收藏，记录一次并同时记录词条名称
     * 说明：1记录被转发次数 2记录被收藏次数
     */
    fun setWikeDetailWord(pedia_word_id:String,type:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["pedia_word_id"] =pedia_word_id
        when(type){
            1->{
                map["pedia_word_forward"] =1
            }
            2->{
                map["pedia_word_Collection"] =1
            }
        }

        LoginAgentUtils.setlog("XH_031", map)
    }

    /**
     * 当进入圈子主页并成功打开页面时
     */
    fun setCircleHome() {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")

        LoginAgentUtils.setlog("XH_032", map)
    }

    /**
     * 进入圈子详情
     */
    fun setCircleDetail(circle_id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["circle_id"] =circle_id

        LoginAgentUtils.setlog("XH_033", map)
    }

    /**
     * 圈子-发帖
     */
    fun setCirclePost(circle_id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["circle_id"] =circle_id
        LoginAgentUtils.setlog("XH_034", map)
    }

    /**
     * 圈子-加入
     */
    fun setCircleOn(circle_id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["circle_id"] =circle_id
        LoginAgentUtils.setlog("XH_035", map)
    }

    /**
     * 当XX游戏圈子中帖子被 查看，点赞，评论，转发，记录一次，并同时记录帖子标题
     * 1参数变量：circle_postlike说明：记录被点赞一次
     * 2参数变量：circle_postdiscuss说明：记录被评论一次
     * 3参数变量：circle_postforward说明：记录被转发一次
     * 4参数变量：circle_postview说明：记录被查看一次
     * 5参数变量：circle_post_collect：记录被收藏一次
     *
     */
    fun setCirclePostInfo(circle_id:String,circle_post_ID:String,type:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["circle_id"] =circle_id
        map["circle_post_ID"] =circle_post_ID
        when(type){
            1->{
                map["circle_postlike"] =1
            }
            2->{
                map["circle_postdiscuss"] =1
            }
            3->{
                map["circle_postforward"] =1
            }
            4->{
                map["circle_postview"] =1
            }
            5->{
                map["circle_post_collect"] =1
            }
        }

        LoginAgentUtils.setlog("XH_036", map)
    }
}