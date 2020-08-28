package com.tinytiger.titi.ui.test.vp

import com.tinytiger.common.basis.BasisPresenter
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.titi.ui.test.TestActivity
import com.tinytiger.titi.ui.test.TestPostResp

/*
 * @author zwy
 * create at 2020/6/5 0005
 * description:
 */
class TestPresenter : BasisPresenter<TestActivity>() {
    fun getPostList(page: Int) {
        val hashMap = mapOf("page" to page.toString())
        HttpRequestUtil.onGet(HttpConstants.Auth.GET_POST_LIST, hashMap, object : HttpLoadingCallback<TestPostResp>() {
            override fun onResponse(any: TestPostResp) {
                super.onResponse(any)
                var date=any.data
                 mvpView?.showAnswersNodeList(date)
            }

            override fun onFailure(errorCode: String, errorMsg: String) {
                super.onFailure(errorCode, errorMsg)
            }
        })
    }
}