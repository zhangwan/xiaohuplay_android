package com.tinytiger.common.net.data.home2

interface SkipChangeInterface {
    //id
    fun getIds(): String? = null

    // #事件类型:0无事件1外部url跳转 2内部url跳转 3跳转文章/视频 4跳转游戏 5跳转百科 6 跳转词条  7跳转帖子
    fun getJumpTypes(): Int? = null

    //事件ID
    fun getJumpView(): String? = null

    //跳转的url
    fun getJumpUrl(): String? = null

    //视频还是文章
    fun getContentType(): String? = null

}