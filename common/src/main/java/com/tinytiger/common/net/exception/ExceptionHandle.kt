package com.tinytiger.common.net.exception

import com.google.gson.JsonParseException
import org.json.JSONException

import java.net.ConnectException
import java.net.NoRouteToHostException

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 3:49
 * @Copyright 小虎互联科技
 * @since 1.0.0
 * @doc 异常处理类
 */
class ExceptionHandle {


    companion object {
         var errorCode = ErrorStatus.UNKNOWN_ERROR
         var errorMsg = "请求失败，请稍后重试"

        fun handleException(e: Throwable): String {
            e.printStackTrace()

            if (e is SocketTimeoutException) {//网络超时
                errorMsg = "网络不可用"
                errorCode = ErrorStatus.NETWORK_ERROR
            } else if (e is ConnectException) { //均视为网络错误
                errorMsg = "网络不可用"
                errorCode = ErrorStatus.NETWORK_ERROR
            } else if (e is JsonParseException
                    || e is JSONException
                    || e is ParseException) {   //均视为解析错误
                errorMsg = "数据解析异常"
                errorCode = ErrorStatus.SERVER_ERROR
            } else if (e is ApiException) {//服务器返回的错误信息
                errorMsg = e.message.toString()
                errorCode = ErrorStatus.SERVER_ERROR
            } else if (e is UnknownHostException) {
                errorMsg = "网络不可用"
                errorCode = ErrorStatus.NETWORK_ERROR
            } else if (e is IllegalArgumentException) {
                errorMsg = "参数错误"
                errorCode = ErrorStatus.SERVER_ERROR
            } else if (e is NoRouteToHostException) {
                errorMsg = "网络不可用"
                errorCode = ErrorStatus.NETWORK_ERROR
            } else {//未知错误
                errorMsg = "未知数据~"
                errorCode = ErrorStatus.UNKNOWN_ERROR
            }
            return errorMsg
        }

    }


}
