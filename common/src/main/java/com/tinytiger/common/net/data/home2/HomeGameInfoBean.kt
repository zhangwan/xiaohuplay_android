package com.tinytiger.common.net.data.home2

/**
 * 推荐数据流的游戏详情页
 */
class HomeGameInfoBean {
    //游戏名称
    var name: String? = null

    //缩略图
    var thumbnail: String? = null

    //logo
    var logo: String? = null

    //游戏推荐理由
    var recommend_title:String?=null

    //游戏推荐类型名称
    var game_from:String?=null

    //是否云游戏 1是 0否
    var cloud_game:String?=null

    //游戏ID
    var game_id:String?=null

    //后台设置的游戏推荐类型
    var game_type:String?=null
}