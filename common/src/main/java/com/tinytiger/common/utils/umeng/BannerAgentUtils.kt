package com.tinytiger.common.utils.umeng

import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.R
import com.tinytiger.common.net.data.AdBean
import com.tinytiger.common.net.data.home2.GameBannerBean
import com.tinytiger.common.net.data.home2.HomeRecommendBean
import java.util.ArrayList


/**
 *
 * @author zhw_luke
 * @date 2020/6/9 0009 下午 5:52
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 友盟banner事件
 */
object BannerAgentUtils {

    /**
     * 首页banner点击
     */
    fun setBannerHomeClick(id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["banner_id"] = id
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        LoginAgentUtils.setlog("XH_008", map)
    }

    /**
     * 百科详情页点击
     */
    fun setBannerWikiDetailClick(id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["banner_id"] = id
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")

        LoginAgentUtils.setlog("XH_010", map)
    }

    /**
     * 百科banner点击
     */
    fun setBannerWikiClick(id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["banner_id"] = id
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")

        LoginAgentUtils.setlog("XH_009", map)
    }

    /**
     * 首页banner展示
     */
    fun setBannerHomeShow(mAdBean: ArrayList<AdBean>) {
        var banner_id=""
        for (i in mAdBean){
            banner_id="$banner_id,${i.id}"
        }

        if (banner_id.length>1){
            banner_id=banner_id.substring(1,banner_id.length)
        }
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["banner_id"] = banner_id

        LoginAgentUtils.setlog("XH_011", map)
    }

    /**
     * 百科详情页banner展示
     */
    fun setBannerDetailShow(banner_id: String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["banner_id"] = banner_id

        LoginAgentUtils.setlog("XH_012", map)
    }

    /**
     * 百科banner展示
     */
    fun setBannerWikiShow(mAdBean: ArrayList<AdBean>) {
        var banner_id=""
        for (i in mAdBean){
            banner_id="$banner_id,${i.id}"
        }

        if (banner_id.length>1){
            banner_id=banner_id.substring(1,banner_id.length)
        }
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["banner_id"] = banner_id

        LoginAgentUtils.setlog("XH_013", map)
    }


    /**
     * 发现页banner点击
     */
    fun setBannerFindClick(id:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["banner_id"] = id
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")

        LoginAgentUtils.setlog("XH_038", map)
    }
    /**
     * 首页发现页展示
     */
    fun setBannerFindShow(mAdBean: ArrayList<AdBean>) {
        var banner_id=""
        for (i in mAdBean){
            banner_id="$banner_id,${i.id}"
        }
        if (banner_id.length>1){
            banner_id=banner_id.substring(1,banner_id.length)
        }
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["banner_id"] = banner_id
        LoginAgentUtils.setlog("XH_042", map)
    }

    /**
     * 首页游戏推荐位展示
     */
    fun setGameBannerShow(mAdBean: ArrayList<GameBannerBean>) {
        var banner_id=""
        for (i in mAdBean){
            banner_id="$banner_id,${i.game_id}"
        }
        if (banner_id.length>1){
            banner_id=banner_id.substring(1,banner_id.length)
        }
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["Gametuijian_ID"] = banner_id
        map["Gametuijian_show"] = 1
        LoginAgentUtils.setlog("XH_039", map)
    }


    /**
     * 游戏分类点击
     */
    fun setBannerClassClick(id:String,cate_id:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["banner_id"] = id
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["cate_id_tag"]=""+cate_id
        LoginAgentUtils.setlog("XH_040", map)
    }
    /**
     * 游戏分类展示
     */
    fun setBannerClassShow(banner_id: String,cate_id:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["banner_id"] = banner_id
        map["cate_id_tag"]=""+cate_id
        LoginAgentUtils.setlog("XH_041", map)
    }
}