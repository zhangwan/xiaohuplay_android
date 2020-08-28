package com.tinytiger.titi.ui.mine.me.collect.vp

import com.tinytiger.common.basis.BasisFragment
import com.tinytiger.common.basis.BasisPresenter
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.center.MineGameListBean
import com.tinytiger.common.net.data.gametools.GameWikiListBean
import com.tinytiger.titi.ui.mine.me.collect.CollectGameFragment
import com.tinytiger.titi.ui.test.TestPostResp

/**
 * @author zwy
 * create at 2020/6/29 0029
 * description:
 */
class CollectGamePresenter :BasisPresenter<CollectGameFragment>(){
    fun getUserGameAmwayCollectList(page: Int) {
        val hashMap = mapOf("page" to page.toString())
        HttpRequestUtil.onGet(
            HttpConstants.Auth.GET_GAME_FOLLOW, hashMap, object : HttpLoadingCallback<MineGameListBean>() {
                override fun onResponse(any: MineGameListBean) {
                    super.onResponse(any)
                    var date=any.data
                    mvpView?.showGameFollow(date)
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                }
            })
    }
    fun batchCancelCollectNode(ids:String){
        val hashMap = mapOf("ids" to ids)
        HttpRequestUtil.onGet(
            HttpConstants.Auth.DEL_COLLECT, hashMap, object : HttpLoadingCallback<BaseBean>() {
                override fun onResponse(any: BaseBean) {
                    super.onResponse(any)
                    mvpView?.batchCancelCollect()
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                    mvpView?.showToast(errorMsg)
                }
            })
    }
}