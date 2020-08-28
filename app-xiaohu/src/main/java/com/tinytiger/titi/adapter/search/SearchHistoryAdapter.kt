package com.tinytiger.titi.adapter.search


import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.titi.R


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索历史记录
 */
class SearchHistoryAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.search_item_text, null) {


    var type = true
    override fun convert(holder: BaseViewHolder, item: String) {
        val tv_title = holder.getView<TextView>(R.id.tv_title)
        tv_title.text = item

        if (type) {
            tv_title.setTextColor(ContextCompat.getColor(context,R.color.color_d7ab00))
            tv_title.background=ContextCompat.getDrawable(context,R.drawable.solid_rectangle_15_f7f6f0)
        } else {
            tv_title.setTextColor(ContextCompat.getColor(context,R.color.color_0fb50a))
            tv_title.background=ContextCompat.getDrawable(context,R.drawable.solid_rectangle_15_f1f8f2)
        }
    }


}