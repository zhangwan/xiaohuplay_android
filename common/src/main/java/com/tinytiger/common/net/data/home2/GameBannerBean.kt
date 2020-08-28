package com.tinytiger.common.net.data.home2

import com.tinytiger.common.http.response.BaseResp

class GameBannerResp : BaseResp<ArrayList<GameBannerBean>>()
class GameBannerBean {
    var id: String? = null //id
    var game_id: String? = null //游戏ID
    var position_type: String? = null // 广告位置
    var game_name: String? = null //游戏名称
    var game_logo: String? = null //logo
}