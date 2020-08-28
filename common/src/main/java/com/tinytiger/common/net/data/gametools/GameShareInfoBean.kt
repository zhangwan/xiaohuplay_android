package com.tinytiger.common.net.data.gametools

import com.tinytiger.common.http.response.BaseResp
import com.tinytiger.common.http.response.PageVo

/**
 * @author zwy
 * create at 2020/7/6 0006
 * description:
 */
class GameShareInfoResp : BaseResp<GameShareInfoBean>()
class GameShareInfoBean {
    //分享名称或标题
    var title:String?=null
    //分享描述
    var desc:String?=null
    //分享图片
    var img:String?=null
    //分享地址
    var share_url:String?=null
    //当前用户的分享代码(更新分享关系信息接口需要使用)
    var code:String?=null
}