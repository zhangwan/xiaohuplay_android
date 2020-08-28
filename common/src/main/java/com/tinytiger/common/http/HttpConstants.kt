package com.tinytiger.common.http

import retrofit2.http.GET


/**
 * Created by zwy on 2020/6/5.
 */
object HttpConstants {
    const val TIME_OUT: Long = 30

    object Code {

        const val SUCCESS = "200" //请求成功
        const val CUSTOMIZE_CODE_3000 = "3000" //未登录-登录失效
        const val CUSTOMIZE_CODE_1000 = "1000" //停服通知
        const val CUSTOMIZE_CODE_3008 = "3008" //用户禁止登录
        const val CODE_700 = "700" //接口返回的数据结构和约定的不一致
        const val TIME_OUT = "-1" //网络连接超时
    }

    /**
     * 用户管理
     */
    object Auth {
        const val MSG_CODE = "Login/sendVerificationCode" //获取短信验证码
        const val LOGIN_CODE = "Login/verificationCodeLogin" //手机验证码登录
        const val GET_POST_LIST = "UserFavorites/getPostList" //获取我收藏的帖子列表
        const val GET_GAME_FOLLOW = "User/getUserFollowGameList" //游戏关注
        const val DEL_COLLECT = "game/GameFollowDel" // 游戏批量取消收藏

    }


    object Game {
        const val GET_CIRCLE_NAVIGATION = "Common/circleNavigation" //获取以圈子为主的 导航栏
        const val GET_GAME_INFO = "game/getGameInfo" //游戏详情页
        const val GET_GAME_TIME = "CloudGames/getGameTime" //获取云游戏时间
        const val ADD_GAME_TIME = "CloudGames/addGameTime" //云游戏-写入游戏时长
        const val SET_GAME_RELATION = "CloudGames/setShareRelation" //云游戏-更新分享关系信息
        const val GET_SHARE_INFO = "CloudGames/getShareInfo" //云游戏-获取分享信息
        const val GET_PLAY_LIST = "CloudGames/getPlayList" //试玩游戏详情
        const val GAME_PING = "CloudGames/ping" //云游戏-心跳


        const val GET_AD_LIST = "recommend/getAdList" //发现广告
        const val CLICK_AD_RECORD = "AdClickLog/clickAdRecord" //点击广告记录
        const val AMWAY_WALL_RECOMMEND = "recommend/amwayWallRecommend" //发现页-安利墙
        const val INDEX_GAME = "Game/indexGame" //游戏列表

        const val ADD_APPOINTMENT="game/addAppointment"//新游戏预约

    }

    object Home {
        const val GET_AD_LIST = "recommend/getAdList" //获取广告列表
        const val GAME_BANNER_LIST = "recommend/gameBannerList" //首页游戏banner
        const val AMWAY_WALL_RECOMMEND = "recommend/amwayWallRecommend" //首页安利墙
        const val GET_HOME_RECOMMEND = "recommend/homeRecommend" //首页推荐信息流
        const val GET_DISCOVER_INDEX = "Discover/index" //发现广告
        const val CLICK_AD_RECORD = "AdClickLog/clickAdRecord" //点击广告记录
        const val GET_DISCOVER_CLASSIFY = "Discover/classify" //游戏分类
    }

    object Circle {
        const val GET_JOIN_LIST = "UserCircle/getJoinList" //我加入的圈子列表
        const val INSET_HISTORY = "UserCircle/setHistory" //新增圈子浏览记录
        const val GET_HISTORY_LIST = "UserCircle/getHistoryList" //最近30天浏览历史
        const val GET_POST_LIST = "UserHistory/getPostList" //帖子历史记录
    }

}