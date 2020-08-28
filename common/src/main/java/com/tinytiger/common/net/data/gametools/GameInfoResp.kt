package com.tinytiger.common.net.data.gametools

import com.tinytiger.common.http.response.BaseResp
import com.tinytiger.common.net.data.circle.CircleBean


/**
 * @author zwy
 * create at 2020/6/30 0030
 * description:
 */
class GameInfoResp : BaseResp<GameInfoDetailsBean>()

class GameInfoDetailsBean{
    var game_info: GameInfoBean? = null
    var circle_info: CircleBean? = null
}