package com.tinytiger.titi.ui.mine.me.circle.vp

import com.tinytiger.common.basis.BasisPresenter
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.common.net.data.circle.CircleHistoryResp

class AddCirclePresenter: BasisPresenter<CircleView>(){
    /**
     * 我的圈子-我加入的圈子列表
     */
    fun getJoinList(page:Int) {
        val hashMap =mapOf("page" to page.toString())
        HttpRequestUtil.onGet(
            HttpConstants.Circle.GET_JOIN_LIST,hashMap,object : HttpLoadingCallback<CircleHistoryResp>(){
            override fun onResponse(any: CircleHistoryResp) {
                super.onResponse(any)

                if(any?.data!=null){
                    mvpView?.setHistoryList(any.data)
                }

            }

            override fun onFailure(errorCode: String, errorMsg: String) {
                super.onFailure(errorCode, errorMsg)
                mvpView?.showToast(errorMsg)
            }
        })
    }

    /**
     * 我的圈子-最近30天浏览历史
     */
    fun getHistoryList(page:Int){
        val hashMap =mapOf("page" to page.toString())
        HttpRequestUtil.onGet(
            HttpConstants.Circle.GET_HISTORY_LIST,hashMap,object : HttpLoadingCallback<CircleHistoryResp>(){
                override fun onResponse(any: CircleHistoryResp) {
                    super.onResponse(any)

                    if(any?.data!=null){
                        mvpView?.setHistoryList(any.data)
                    }

                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                    mvpView?.showToast(errorMsg)
                }
            })
    }
}