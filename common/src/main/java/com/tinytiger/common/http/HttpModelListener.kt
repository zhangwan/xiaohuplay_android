package com.tinytiger.common.http

/**
 * Created by zwy on 2019/6/5.
 */
interface HttpModelListener<T> {
    fun onSuccess(baseResp: T)
    fun onError(errorCode: String, errorMsg: String)
}