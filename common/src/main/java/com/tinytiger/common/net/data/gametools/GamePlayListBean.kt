package com.tinytiger.common.net.data.gametools

import com.tinytiger.common.http.response.BaseResp

/**
 * @author zwy
 * create at 2020/7/6 0006
 * description:
 */
class GamePlayListResp : BaseResp<GamePlayListBean>()
class GamePlayListBean {
    var remaining_time: String? = null //用户当天剩余试玩时长
    var play_list: PlayListBean? = null //试玩详细记录
    var share_info:ShareInfoBean?=null//分享信息
}

class ShareInfoBean{
    //logo
    var logo_url:String?=null
    //标题
    var title:String?=null
    //简介
    var slogan:String?=null
    //下载页H5
    var app_download_url:String?=null
    //协议H5
    var app_agreement_url:String?=null
    //当前用户的分享代码(更新分享关系信息接口需要使用)
    var code:String?=null
}
class PlayListBean {
    var total: Int? = null
    var per_page: Int? = null
    var current_page: Int? = null
    var last_page: Int? = null
    var data: ArrayList<PlayBean>? = null
}

class PlayBean {
    var id: String? = null
    var game_name: String? = null
    //游戏logo
    var game_logo: String? = null
    var game_id: String? = null
    var create_time: String? = null
    //试玩总长/s
    var play_time: String? = null

}