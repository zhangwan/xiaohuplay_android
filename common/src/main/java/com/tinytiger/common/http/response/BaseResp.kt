package com.tinytiger.common.http.response


/**
 * Created by zwy on 2020/6/5
 */
open class BaseResp<T> {
    var code: String? = null// 应用返回编码
    var msg: String? = null //    应用返回消息
    var status: Boolean? = null
    var data: T? = null//应用返回结果

}