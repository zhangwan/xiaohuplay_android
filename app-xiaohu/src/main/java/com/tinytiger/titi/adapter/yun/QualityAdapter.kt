package com.tinytiger.titi.adapter.yun


import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.haima.hmcp.beans.ResolutionInfo

import com.tinytiger.titi.R


/**
 *
 * @author zhw_luke
 * @date 2020/7/3 0003 下午 3:53
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 云游戏分辨率
 */
class QualityAdapter :
    BaseQuickAdapter<ResolutionInfo, BaseViewHolder>(R.layout.yun_item_quality, null) {

    var title = ""
    override fun convert(holder: BaseViewHolder, item: ResolutionInfo) {
        holder.setText(R.id.tvName, item.name)
        if (title == item.name) {
            holder.setTextColor(R.id.tvName, ContextCompat.getColor(context,R.color.color_ffcc03))
        } else {
            holder.setTextColor(R.id.tvName, ContextCompat.getColor(context,R.color.white))
        }

    }


}