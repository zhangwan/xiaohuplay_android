package com.tinytiger.titi.ui.mine.other.vp


import com.tinytiger.common.basis.BasisPresenter
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.common.net.data.gametools.GamePlayListResp
import com.tinytiger.common.net.data.gametools.GameShareInfoResp
import com.tinytiger.common.net.data.yungaem.YunTimeAddResp
import com.tinytiger.titi.ui.mine.other.GameCloudActivity


/**
 * @author zwy
 * create at 2020/7/3 0003
 * description:
 */
class GameCloudPresenter : BasisPresenter<GameCloudActivity>() {

    fun getPlayList(page: Int) {
        val hashMap = mapOf("page" to page.toString())
        HttpRequestUtil.onGet(HttpConstants.Game.GET_PLAY_LIST, hashMap,
            object : HttpLoadingCallback<GamePlayListResp>() {
                override fun onResponse(any: GamePlayListResp) {
                    super.onResponse(any)
                    var date = any.data
                    if (date != null) {
                        mvpView?.getPlayBean(date!!)
                    }

                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                    mvpView?.showToast(errorMsg)
                }
            })
    }

    fun setShareRelation(code:String) {
        val hashMap = HashMap<String, String>()
        hashMap["code"] = code

        HttpRequestUtil.onGet(HttpConstants.Game.SET_GAME_RELATION, hashMap,
            object : HttpLoadingCallback<YunTimeAddResp>() {
                override fun onResponse(any: YunTimeAddResp) {
                    super.onResponse(any)
                    val date = any.data
                    if (date != null&&date.additional_time>0) {
                        mvpView?.showAddGameTime(date.additional_time)
                    }else{
                        mvpView?.showToast("您今日秒玩时长已增加，明天再来吧。")
                    }
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                    mvpView?.showToast(errorMsg)
                }
            })
    }
}