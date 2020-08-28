package com.tinytiger.titi.adapter.search


import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.search.SearchKeyword
import com.tinytiger.titi.R


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索关键词库
 */
class SearchKeywordAdapter :
    BaseQuickAdapter<SearchKeyword, BaseViewHolder>(R.layout.search_item_keyword, null) {

    var type = true
    var keyword = ""

    override fun convert(holder: BaseViewHolder, item: SearchKeyword) {
        val tv_title = holder.getView<TextView>(R.id.tv_title)
        val tv_type = holder.getView<TextView>(R.id.tv_type)

        //标题
        val sb = SpannableString(item.title)
        sb.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.gray99)),
            item.title.indexOf(keyword),
            item.title.indexOf(keyword) + keyword.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv_title.text = sb

        //搜索类型
        when (item.type) {
            1 -> tv_type.text = "游戏"
            2 -> tv_type.text = "资讯"
            3 -> tv_type.text = "圈子"
            4 -> tv_type.text = "帖子"
            5 -> tv_type.text = "用户"
            else -> {
                tv_type.text = ""
            }
        }
    }

}