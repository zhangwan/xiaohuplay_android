package com.tinytiger.titi.ui.yungame.vp


import com.tinytiger.common.basis.BasisPresenter
import com.tinytiger.common.http.HttpBaseCallBack
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.yungaem.YunTimeAddResp
import com.tinytiger.common.net.data.yungaem.YunTimeResp
import com.tinytiger.common.utils.umeng.GameAgentUtils
import com.tinytiger.titi.ui.yungame.YunGameActivity


class YunGamePresenter : BasisPresenter<YunGameActivity>() {

    fun getGameTime(game_id: String) {
        val hashMap = HashMap<String, String>()
        hashMap["game_id"] = game_id
        HttpRequestUtil.onGet(HttpConstants.Game.GET_GAME_TIME, hashMap,
            object : HttpLoadingCallback<YunTimeResp>() {
                override fun onResponse(any: YunTimeResp) {
                    super.onResponse(any)
                    val date = any.data
                    if (date != null) {
                        mvpView?.showGameTime(date)
                    }
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                    mvpView?.showToast(errorMsg)
                }
            })
    }


    fun addGameTime(game_id: String, seconds: Int,logout: Int) {

        val hashMap = HashMap<String, String>()
        hashMap["game_id"] = game_id
        hashMap["seconds"] = "" + seconds
        hashMap["logout"] = "" + logout
        HttpRequestUtil.onGet(HttpConstants.Game.ADD_GAME_TIME, hashMap,
            object : HttpBaseCallBack<BaseBean>() {
                override fun onResponse(any: BaseBean) {

                    //   super.onResponse(any)
                    // val date = any.data
                    //  if (date != null) {
                    //  mvpView?.showGameNavigation(date)
                    //  }
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    //  super.onFailure(errorCode, errorMsg)
                    //  mvpView?.showToast(errorMsg)

                }
            })
    }

    fun setShareRelation(game_id: String, code: String) {
        val hashMap = HashMap<String, String>()
        hashMap["game_id"] = game_id
        hashMap["code"] = code

        HttpRequestUtil.onGet(HttpConstants.Game.SET_GAME_RELATION, hashMap,
            object : HttpLoadingCallback<YunTimeAddResp>() {
                override fun onResponse(any: YunTimeAddResp) {
                    super.onResponse(any)
                    val date = any.data
                    if (date != null && date.additional_time > 0) {
                        mvpView?.showAddGameTime(date.additional_time)
                    } else {
                        mvpView?.showToast("您今日秒玩时长已增加，明天再来吧。")
                    }
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                    mvpView?.showToast(errorMsg)
                }
            })
    }

    fun setPing(seq: Int, showView: Boolean) {
        val hashMap = HashMap<String, String>()
        hashMap["seq"] = "" + seq
        hashMap["showView"] = if (showView) "1" else "0"

        HttpRequestUtil.onGet(HttpConstants.Game.GAME_PING, hashMap,
            object : HttpBaseCallBack<BaseBean>() {
                override fun onResponse(any: BaseBean) {
                    super.onResponse(any)
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                }
            })
    }
}