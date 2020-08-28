package com.tinytiger.common.net.data.home2

/**
 *   推荐流-广告
 */
class HomeAdvertBean : SkipChangeInterface {
    //广告名称
    var advertisement_name: String? = null

    //广告背景图
    var advertisement_img: String? = null

    //广告跳转类型 0无跳转 1外部链接 2功能页面 3游戏详情 4游戏详情-百科 5词条 6文章或者视频
    var jump_type: Int? = null

    //跳转所需要的值
    var jump_view: String? = null

    //文章还是视频 1，图文，2视频
    var content_type: String? = null

    //跳转的URL(只有外部跳转才有URL,其他类型的跳转没有)
    var jump_url: String? = null

    //id
    var content_id: String? = null

    override fun getContentType(): String? {
        return content_type
    }

    override fun getIds(): String? {
        return content_id
    }

    override fun getJumpTypes(): Int? {
        return jump_type
    }

    override fun getJumpView(): String? {
        return jump_view
    }

    override fun getJumpUrl(): String? {
        return jump_url
    }


}