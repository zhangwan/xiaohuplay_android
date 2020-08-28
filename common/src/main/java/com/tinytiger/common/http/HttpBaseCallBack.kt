package com.tinytiger.common.http



/**
 * HttpBaseCallBack
 *
 * @author:zwy
 * @date:2020-6-5
 */
open class HttpBaseCallBack<T>:HttpCallback<T> {

    override fun onResponse(resp: T) {

    }

    override fun onFailure(errorCode: String, errorMsg: String) {

    }
}