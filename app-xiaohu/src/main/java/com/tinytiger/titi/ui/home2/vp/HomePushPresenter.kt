package com.tinytiger.titi.ui.home2.vp

import com.tinytiger.common.basis.BasisPresenter
import com.tinytiger.common.http.HttpBaseCallBack
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.common.http.response.BaseResp
import com.tinytiger.common.net.data.home2.*
import com.tinytiger.common.utils.umeng.BannerAgentUtils
import com.tinytiger.titi.ui.home2.HomePushFragment

class HomePushPresenter : BasisPresenter<HomePushFragment>(){

    fun getRecommendAdList(){
        HttpRequestUtil.onGet(
            HttpConstants.Home.GET_AD_LIST, object : HttpBaseCallBack<BannerAdResp>() {
            override fun onResponse(any: BannerAdResp) {
                super.onResponse(any)
                var date=any.data
                if(date!=null){
                    mvpView?.showAdvertisementList(date)
                }

            }

            override fun onFailure(errorCode: String, errorMsg: String) {
                super.onFailure(errorCode, errorMsg)
                mvpView?.showFailure()
            }
        })
    }

    fun getRecommendBannerList(){
        HttpRequestUtil.onGet(
            HttpConstants.Home.GAME_BANNER_LIST, object : HttpBaseCallBack<GameBannerResp>() {
                override fun onResponse(any: GameBannerResp) {
                    super.onResponse(any)
                    var date=any.data
                    if(date!=null){
                        mvpView?.showGameBanner(date)
                        BannerAgentUtils.setGameBannerShow(date)
                    }else{
                        mvpView?.showHideBanner()
                    }

                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                    mvpView?.showFailure()
                }
            })
    }

    fun getAmwayWallRecommend(){
        HttpRequestUtil.onGet(
            HttpConstants.Home.AMWAY_WALL_RECOMMEND, object : HttpBaseCallBack<AmwayWallListBean>() {
                override fun onResponse(any: AmwayWallListBean) {
                    super.onResponse(any)
                    if(any!=null){
                        mvpView?.loadAmwayWall(any)
                    }

                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                }
            })
    }

    /**
     * 首页推荐信息流
     */
    fun getHomeRecommend(page:Int){
        HttpRequestUtil.onGet(
            HttpConstants.Home.GET_HOME_RECOMMEND, mapOf("page" to page.toString()),object : HttpBaseCallBack<HomeRecommendResp>() {
                override fun onResponse(any: HomeRecommendResp) {
                    super.onResponse(any)
                    if(any.data!=null){
                        mvpView?.LoadPushList(any.data)
                    }

                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                }
            })
    }
    /**
     *新游戏预约
     */
    fun addAppointment(gameId:String){
        HttpRequestUtil.onGet(
            HttpConstants.Game.ADD_APPOINTMENT, mapOf("game_id" to gameId),object : HttpLoadingCallback<BaseResp<Any>>() {
                override fun onResponse(any: BaseResp<Any>) {
                    super.onResponse(any)
//                    if(any.data!=null){
                        mvpView?.loadAppointment(gameId)
//                    }

                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                }
            })
    }

}