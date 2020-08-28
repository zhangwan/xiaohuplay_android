package com.tinytiger.common.http


import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * Created by zwy on 2020/6/5.
 */
interface HttpCallback<T> {
    fun onStart() {

    }

    fun onResponse(any: T)
    fun onFailure(errorCode: String, errorMsg: String)

    fun getEntry(): Type? {
        val type = javaClass.genericSuperclass
        var result: Type? = null
        if (type is ParameterizedType) {
            result = type.actualTypeArguments[0]
        }
        return result
    }
}