package com.tinytiger.common.net.data.circle

import com.tinytiger.common.http.response.BaseResp
import com.tinytiger.common.http.response.PageVo

class CircleHistoryResp : BaseResp<PageVo<CircleHistoryBean>>()
data class CircleHistoryBean(
    //圈子ID
    var id:String?=null,
    //圈子名称
    var name:String?=null,
    // 加入圈子人数
    var add_circle_num:String?=null,
    //圈子logo
    var logo:String?=null
)