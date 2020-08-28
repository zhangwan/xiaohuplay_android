package com.tinytiger.common.http

import com.tinytiger.common.NetConstants_B

/**
 * Created by zwy on 2020/6/5.
 */
class RetrofitRenewManager {
    val api = RetrofitBuilder()
            .initTimeOut(HttpConstants.TIME_OUT)
            .initBaseUrl(NetConstants_B.getHttp())
            .build(Api::class.java)

    private object RetrofitHolder {
        val holder = RetrofitRenewManager()
    }

    companion object {
        val retrofitInstance = RetrofitHolder.holder
    }
}