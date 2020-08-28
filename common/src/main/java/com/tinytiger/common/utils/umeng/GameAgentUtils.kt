package com.tinytiger.common.utils.umeng


import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.R



/**
 *
 * @author zhw_luke
 * @date 2020/6/9 0009 下午 5:52
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 友盟游戏事件
 */
object GameAgentUtils {

    /**
     * 点击游戏详情
     * 说明：从首页打开的，用0代表；
     *      从banner页面打开的，用1代表；
     *      从排行列表打开的，用2代表；
     *      从百科打开的，用3代表；
     *      从分类列表打开的，用4代表；
     *      从搜索打开的，用5代表；
     *      从圈子列表打开的，用6代表；
     *      从游戏管理打开的，用7代表
     */
    fun setGameDetailClick(app_id:String,package_name:String,open_from:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["app_id"] =app_id
        map["package_name"] =package_name
        map["open_from_tag"] =""+open_from
        LoginAgentUtils.setlog( "XH_015", map)
    }

    /**
     * 点击游戏详情 关注游戏
     */
    fun setGameDetailFollow(app_name:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["app_id"] =app_name
        map["app_follow"] =1
        LoginAgentUtils.setlog("XH_016", map)
    }


    /**
     * 游戏详情-评分
     */
    fun setGameDetailScore(app_name:String,game_score_fraction:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["app_name"] =app_name
        map["game_score"] =1
        map["game_score_fraction"] =game_score_fraction
        LoginAgentUtils.setlog("XH_017", map)
    }


    /**
     * 安利文
     * 当游戏评价被点赞，评论，转发，查看，记录一次并同时记录具体的帖子ID
     * 参数变量：app_name说明：记录被进入应用详情的应用的名称
     * 参数变量：gameevaluate_name说明：记录被点击帖子的标题
     * 1 参数变量：gameevaluate_like说明：记录被点赞一次
     * 2 参数变量：gameevaluate_discuss说明：记录被评论一次
     * 3 参数变量：gameevaluate_forward说明：记录被转发一次
     * 4 参数变量：gameevaluate_view说明：记录被查看一次
     * 参数变量：open_from：从首页点击，记录0，从发现页点击，记录1
     *
     *
     */
    fun setGameDetailInfo(app_id:String,gameevaluate_id:String,type:Int,open_from:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["app_id"] =app_id
        map["gameevaluate_id"] =gameevaluate_id
        map["open_from"] =""+open_from
        when(type){
            1->{
                map["gameevaluate_like"] =1
            }
            2->{
                map["gameevaluate_discuss"] =1
            }
            3->{
                map["gameevaluate_forward"] =1
            }
            4->{
                map["gameevaluate_view"] =1
            }
            5->{
                map["gameevaluate_collect"] =1
            }
        }
        LoginAgentUtils.setlog( "XH_018", map)
    }

    /**
     * 游戏下载
     * 当点击下载/更新 按钮并成功建立下载任务时，记录一次并同时记录点击的位置、触发下载的具体页面、应用的包名和应用名称、应用来源
     * app_source :0用户自主点击的下载，1更新产生的下载
     */
    fun setGameDetailDownload(app_id:String,download_location:Int,package_name:String,app_source:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["app_id"] =app_id
        map["download_location_tag"] =""+download_location
        map["package_name"] =package_name
        map["app_source_tag"] =""+app_source

        LoginAgentUtils.setlog("XH_019", map)
    }


    /**
     * 游戏下载
     * 当发生一次应用下载成功时，记录一次并同时记录应用名称和包名
     */
    fun setGameDetailDownloadOver(app_id:String,package_name:String) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["app_id"] =app_id
        map["package_name"] =package_name

        LoginAgentUtils.setlog("XH_020", map)
    }

    /**
     * 游戏下载失败
     * 说明：SD卡异常，用0代表；
     * 存储空间不足，用1代表；
     * 其他原因，用2代表
     */
    fun setGameDetailDownloadError(app_id:String,download_fail:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["app_id"] =app_id
        map["download_fail_tag"] =""+download_fail

        LoginAgentUtils.setlog("XH_021", map)
    }


    /**
     * 游戏详情转发
     * 参数变量：app_forward说明：选微信 用0代表，QQ用1代表，微信朋友圈用2代表，QQ空间用3代表，复制链接用4代表
     */
    fun setGameDetailShare(app_id:String,app_forward:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["app_id"] =app_id
        map["app_forward_tag"] =""+app_forward

        LoginAgentUtils.setlog("XH_022", map)
    }


    /**
     * 云游戏
     * type：1打开 2使用时长 3.分享 4.添加时长 5.切换分辨率
     */
    fun setYunGameDetail(cloud_id:String,cloud_package:String,type:Int,key:Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["user_id"] = SpUtils.getString(R.string.user_id,"0")
        map["cloud_id"] = cloud_id
        map["cloud_package"] =cloud_package

        when(type){
            1->{
                map["cloud_open"] ="1"
            }
            2->{
                map["cloud_time"] =""+key
            }
            3->{
                map["cloud_share"] ="1"
            }
            4->{
                map["cloud_addtime"] ="1"
            }
            5->{
                map["cloud_screen"] =""+key
            }
        }

        LoginAgentUtils.setlog("XH_037", map)
    }
}