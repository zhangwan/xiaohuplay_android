package com.tinytiger.titi.ui.test.vp

import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.titi.ui.test.LoginResp

class LoginPresenter : LoginBasePresenter() {


    fun loginByCode(phone: String, verifyCode: String) {
        val hashMap =mapOf("phone" to phone,"verification_code" to verifyCode)
        HttpRequestUtil.onPost(HttpConstants.Auth.LOGIN_CODE,hashMap,object : HttpLoadingCallback<LoginResp>(){
            override fun onResponse(any: LoginResp) {
                super.onResponse(any)

                mvpView?.startChange()
            }

            override fun onFailure(errorCode: String, errorMsg: String) {
                super.onFailure(errorCode, errorMsg)
            }
        })
    }


}