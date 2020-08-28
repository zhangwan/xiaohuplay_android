package com.tinytiger.titi.ui.mine.me.history.vp

import com.tinytiger.common.basis.BasisPresenter
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.titi.ui.mine.me.collect.CollectGameFragment
import com.tinytiger.titi.ui.mine.me.history.HistoryNodeFragment
import com.tinytiger.titi.ui.test.TestPostResp

class HistoryNodePresenter : BasisPresenter<HistoryNodeFragment>(){
    fun getPostList(page: Int) {
        val hashMap = mapOf("page" to page.toString())
        HttpRequestUtil.onGet(
            HttpConstants.Circle.GET_POST_LIST, hashMap, object : HttpLoadingCallback<PostInfoBean>() {
            override fun onResponse(any: PostInfoBean) {
                super.onResponse(any)
                mvpView?.showAnswersNodeList(any)
            }

            override fun onFailure(errorCode: String, errorMsg: String) {
                super.onFailure(errorCode, errorMsg)
            }
        })
    }

}