package com.tinytiger.titi.ui.circle.detail.vp

import android.text.TextUtils
import com.tinytiger.common.basis.BasisPresenter
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.common.net.data.circle.CircleInfoBean
import com.tinytiger.common.net.data.circle.CircleNavigationResp
import com.tinytiger.common.net.data.gametools.GameInfoDetailBean
import com.tinytiger.common.net.data.gametools.GameInfoResp
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity


/**
 * @author zwy
 * create at 2020/6/29 0029
 * description:
 */

class CirclesDetailsPresenter : BasisPresenter<CirclesDetailsActivity>() {

    fun getCircleNavigation(game_id: String, circle_id: String) {
        val hashMap = HashMap<String, String>()
        if (!TextUtils.isEmpty(game_id)) {
            hashMap["game_id"] = game_id
        }
        if (!TextUtils.isEmpty(circle_id)) {
            hashMap["circle_id"] = circle_id
        }
        HttpRequestUtil.onGet(HttpConstants.Game.GET_CIRCLE_NAVIGATION, hashMap,
            object : HttpLoadingCallback<CircleNavigationResp>() {
                override fun onResponse(any: CircleNavigationResp) {
                    super.onResponse(any)
                    var date = any.data
                    if (date != null) {
                        mvpView?.showGameNavigation(date!!)
                    }else{
                        mvpView?.showToast("圈子不存在")
                        mvpView?.finish()
                    }
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                    mvpView?.showToast(errorMsg)
                    mvpView?.finish()
                }
            })
    }


}