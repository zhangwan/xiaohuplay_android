package com.tinytiger.titi.adapter.circle.home


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.CircleBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc push 我的圈子
 */
class CircleMeAdapter :
    BaseQuickAdapter<CircleBean, BaseViewHolder>(R.layout.circle_item_post, null) {

    override fun convert(holder: BaseViewHolder, item: CircleBean) {

        holder.setText(R.id.tvName, "#${item.name}")
        GlideUtil.loadImg(holder.getView(R.id.ivIcon),item.background)

        holder.getView<View>(R.id.rlInfo).setOnClickListener {
            CirclesDetailsActivity.actionStart(context,item.id,"","")
        }
    }
}
