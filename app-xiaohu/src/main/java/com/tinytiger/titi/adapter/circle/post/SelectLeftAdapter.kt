package com.tinytiger.titi.adapter.circle.post


import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.postsend.SelectCircler1Bean
import com.tinytiger.titi.R


/**
 *
 * @author zhw_luke
 * @date 2020/5/6 0006 下午 3:05
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 左侧选择
 */
class SelectLeftAdapter:
    BaseQuickAdapter<SelectCircler1Bean.Data, BaseViewHolder>(R.layout.circle_item_left, null) {

    var prePosition = 0

    override fun convert(holder: BaseViewHolder, item: SelectCircler1Bean.Data) {
        holder.setText(R.id.tv_title, item.name)


        if (prePosition == holder.adapterPosition) {
            holder.setTextColor(R.id.tv_title,ContextCompat.getColor(context, R.color.color_ffcc03))
            holder.setBackgroundColor(R.id.ll_content, ContextCompat.getColor(context, R.color.white))

            holder.setGone(R.id.ivLine,false)
        } else {
            holder.setTextColor(R.id.tv_title,ContextCompat.getColor(context, R.color.gray_8A8A8A))
            holder.setBackgroundColor(R.id.ll_content, ContextCompat.getColor(context, R.color.color_line2))
            holder.setGone(R.id.ivLine,true)
        }
    }

     fun setSelection( pos:Int) {
        this.prePosition = pos;
        notifyDataSetChanged();
    }



}