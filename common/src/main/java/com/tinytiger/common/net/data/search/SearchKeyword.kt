package com.tinytiger.common.net.data.search

import com.tinytiger.common.net.data.BaseBean
import java.io.Serializable

/**
 * @author lmq001
 * @date 2020/08/10 13:54
 * @copyright 小虎互联科技
 * @doc 关键词库
 */
data class SearchKeywordBean(
    var data: MutableList<SearchKeyword>
) : BaseBean()

data class SearchKeyword(
    var title: String,
    var type: Int,
    var view_id: Int
) : Serializable