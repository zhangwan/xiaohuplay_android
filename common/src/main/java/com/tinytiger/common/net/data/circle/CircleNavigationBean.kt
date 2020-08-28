package com.tinytiger.common.net.data.circle

import com.tinytiger.common.http.response.BaseResp


/**
 * @author zwy
 * create at 2020/6/29 0029
 * description:
 */

class CircleNavigationResp : BaseResp<CircleNavigationBean>()

class CircleNavigationBean {
    var cirlce: CircleBean? = null
    var share: ShareBean? = null
    var nav: NavBean? = null

    class CircleBean {
        //圈子id
        var id: String?=null
        //圈子名称
        var name: String?=null
        //游戏id
        var game_id: String?=null
        //背景图
        var background: String?=null
        //logo
        var logo: String?=null
        //#1.百科模块状态 1.百科详情页界面 2.招募管理员界面 3.默认点亮百科
        var wiki_modular: String?=null
        //0 #申请管理员状态 -2：拒绝(可以重新发起申请) -1：拒绝(不可继续申请) 0：未申请 1：审核通过 2：审核中
        var apply_status:Int?=null
    }

    class NavBean {
        //1, # 圈子导航状态 0关闭  1开启
        var circle: String? = null
        //百科导航状态 0关闭  1开启
        var wiki: String? = null
        //资讯导航状态 0关闭  1开启
        var information: String? = null
    }

    class ShareBean {
        //咨询分享地址
        var information_url: String? = null
        //百科分享地址
        var wiki_url: String? = null
        //圈子分享地址
        var circle_url: String? = null
    }


}