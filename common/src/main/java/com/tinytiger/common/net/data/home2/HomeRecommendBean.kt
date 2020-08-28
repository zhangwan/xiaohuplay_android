package com.tinytiger.common.net.data.home2

import com.tinytiger.common.http.response.BaseResp
import com.tinytiger.common.http.response.PageVo

class HomeRecommendResp : BaseResp<PageVo<HomeRecommendBean>>()
class HomeRecommendBean {
    var id:String?=null//推荐id
    var recommend_type:String?=null //# 推荐类型：1，游戏 2，广告 3，专题
    var create_time:String?=null //上传时间
    var start_time:String?=null //开始时间
    var end_time:String?=null //结束时间
    var all_time:String?=null //#是否长期 1是 0否
    var cate_name:String?=null //专题名称
    var cate_id:Int?=null //专题ID
    var game_info:HomeGameInfoBean?=null//游戏类型游戏信息
    var content:HomeAdvertBean?=null //推荐广告类型内容
    var game_list:ArrayList<HomeSubjectBean>?=null //专题下的游戏列表，只有7个最多
}


