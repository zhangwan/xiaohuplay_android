package com.tinytiger.titi.adapter.circle.post


import android.widget.ImageView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.tinytiger.common.net.data.circle.postsend.SelectCirclerName2Bean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/**
 *
 * @author zhw_luke
 * @date 2020/5/6 0006 下午 3:05
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 选择发布圈子
 */
class SelectCircleAdapter :
    BaseQuickAdapter<SelectCirclerName2Bean.Data.CirclerName, BaseViewHolder>(R.layout.circle_item_circle, null) {

    override fun convert(holder: BaseViewHolder, item: SelectCirclerName2Bean.Data.CirclerName) {
        GlideUtil.loadImg(holder.getView<ImageView>(R.id.ivIcon), item.logo)
        holder.setText(R.id.tvNum, "加入人数: ${StringUtils.sizeToString(item.add_circle_num)}")
        holder.setText(R.id.tv_title, item.name)
    }
}