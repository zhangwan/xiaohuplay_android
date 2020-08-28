package com.tinytiger.titi.adapter.circle


import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.CircleBeanMutli
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil

import com.tinytiger.titi.R
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity


/**
 *
 * @author zhw_luke
 * @date 2020/4/28 0028 上午 10:11
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 圈子列表
 */
class MultiCirclesAdapter(data: ArrayList<CircleBeanMutli>) :
    BaseMultiItemQuickAdapter<CircleBeanMutli, BaseViewHolder>(data) {


    init {
        addItemType(1, R.layout.item_text)
        addItemType(2, R.layout.circle_item_cover2)
    }


    override fun convert(holder: BaseViewHolder, item: CircleBeanMutli) {
        when (holder.itemViewType) {
            1 -> {
                holder.setText(R.id.tv_name, item.title)

            }
            2 -> {
                val bean = item.mBean
                holder.setText(R.id.tvName, "#${bean.name}#")
                GlideUtil.loadImg(holder.getView<ImageView>(R.id.ivIcon), bean.background)
                holder.setText(R.id.tvInfo, "${StringUtils.sizeToString(bean.hots)}")
                holder.getView<View>(R.id.rlInfo).setOnClickListener {
                    CirclesDetailsActivity.actionStart(context, bean.id,"","")
                }


            }

            else -> {

            }
        }
    }

}