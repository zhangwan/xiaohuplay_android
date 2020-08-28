package com.tinytiger.common.http.response

/**
 * Created By zwy
 * On 2020/6/5
 */
open class PageVo<T> {
    var data: ArrayList<T>? = null //列表

    var current_page: Int? = null// 当前页数

    var per_page: Int? = null//  每页条数

    var total: Long? = null// 总数
    var last_page: Long? = null//最后一页


}