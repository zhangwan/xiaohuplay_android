package com.tinytiger.common.net.data.home2

import com.tinytiger.common.http.response.BaseResp
import com.tinytiger.common.http.response.PageVo

class BannerAdResp : BaseResp<ArrayList<BannerAdBean>>()
class BannerAdBean : SkipChangeInterface {
    //广告id
    var id: String? = null

    //广告名称
    var title: String? = null

    //广告位
    var position_id: String? = null

    //广告图片
    var image: String? = null

    //排序
    var sort: String? = null

    //跳转url
    var jump_url: String? = null

    // # 事件类型:0无事件1外部url跳转 2内部url跳转 3内部功能页面跳转 4跳转游戏 5跳转百科 6跳转词条 7跳转帖子',
    var jump_type: Int? = null

    //事件ID
    var jump_view: String? = null

    //判断是否是云游戏
    //#是否是云游戏 是否云游戏:0否 1是',
    var cloud_game: String? = null

    //视频还是文章
    var content_type: String? = null

    var gameInfo: GameInfoBean? = null
    override fun getIds(): String? {
        return id
    }

    override fun getJumpTypes(): Int? {
        return jump_type
    }

    override fun getContentType(): String? {
        return content_type
    }

    override fun getJumpUrl(): String? {
        return jump_url
    }

    override fun getJumpView(): String? {
        return jump_view
    }
}

class GameInfoBean {
    //游戏ID
    var id: String? = null

    //游戏名字
    var name: String? = null

    //#是否是云游戏 是否云游戏:0否 1是',
    var cloud_game: String? = null

    //是否预约 -#是否预约:0否 1是',2:预约成功
    var appointment_type: String? = null

    //游戏包名
    var package_name_android:String?=null

    var leader_mage: LeaderMargeBean? = null

    //二级分类
    var cate_info = arrayListOf<String>()
}

//雷达图数据
class LeaderMargeBean {
    var game_id: String? = null
    var playway_score: String? = null
    var music_score: String? = null
    var art_score: String? = null
    var control_score: String? = null
    var conscience_score: String? = null
    var score: String? = null
    var user_num: String? = null
}
