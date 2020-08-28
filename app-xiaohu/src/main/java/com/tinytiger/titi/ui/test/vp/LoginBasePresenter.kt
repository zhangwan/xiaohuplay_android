package com.tinytiger.titi.ui.test.vp


import com.tinytiger.common.basis.BasisPresenter
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.common.http.response.StringResp

open class LoginBasePresenter: BasisPresenter<LoginView>(){
    var imgKey: String? = null
    fun sendMsg(phone:String){
        val hashMap =mapOf("phone" to phone)
        HttpRequestUtil.onPost(HttpConstants.Auth.MSG_CODE,hashMap,object : HttpLoadingCallback<StringResp>(){
            override fun onResponse(any: StringResp) {
                super.onResponse(any)
                mvpView?.showToast("发送成功")
            }

            override fun onFailure(errorCode: String, errorMsg: String) {
                super.onFailure(errorCode, errorMsg)
            }
        })
    }

    fun getImageCode() {
//        HttpRequestUtil.onGet(HttpConstants.Auth.VERIFY_CODE, object : HttpLoadingCallback<VerifyCodeResp>() {
//            override fun onResponse(any:VerifyCodeResp) {
//                super.onResponse(any)
//                imgKey = any?.data?.imgKey
//                val base64decodedBytes = Base64.decode(any?.data?.imgCode, Base64.DEFAULT)
//                base64decodedBytes?.let {
//                    var bitmap =
//                        BitmapFactory.decodeByteArray(base64decodedBytes, 0, base64decodedBytes.size)
//                    mvpView?.setVerificationCode(bitmap)
//                }
//            }
//        })
    }

    //useType（用途类型: 1-注册；2-登录；3-找回密码）
    fun getMsgCode(phone: String, imageCode: String, useType: String) {
//        if (TextUtils.isEmpty(phone)) {
//            mvpView?.showToast(R.string.register_input_phone_hint)
//            return
//        }
//        if (TextUtils.isEmpty(imageCode)) {
//            mvpView?.showToast(R.string.register_input_img_hint)
//            return
//        }
//
//        var map = mapOf("phoneCode" to "91", "phone" to phone,  "useType" to useType)
//        HttpRequestUtil.onBody(HttpConstants.Auth.MSG_CODE, map, object : HttpLoadingCallback<StringResp>() {
//            override fun onResponse(any: StringResp) {
//                super.onResponse(any)
//                mvpView?.showToast(R.string.register_code_success)
//                mvpView?.startCountDown()
//            }
//        })
    }
}