package com.tinytiger.titi.adapter.home2.find


import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.ad.AdChildClassify
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_游戏分类列表
 */
class GameSortAdapter(data: ArrayList<AdChildClassify>) :
    BaseQuickAdapter<AdChildClassify, BaseViewHolder>(R.layout.home_find_item_sort, data) {

    override fun convert(holder: BaseViewHolder, item: AdChildClassify) {
        holder.setText(R.id.tvName, item.name)
        GlideUtil.loadImg(holder.getView(R.id.ivLogo), item.logo)
        var ll_content = holder.getView<LinearLayout>(R.id.ll_content)
        //设置内边距使得滑动到最左边
        val paddingLeft =
            if (holder.adapterPosition == 0) Dp2PxUtils.dip2px(context, 20)
            else Dp2PxUtils.dip2px(context, 15)
        val paddingRight =
            if (holder.adapterPosition == itemCount - 1) Dp2PxUtils.dip2px(context, 20) else 0
        ll_content.setPadding(paddingLeft, 0, paddingRight, 0)

        ll_content.setOnClickListener {
            GameDetailActivity.actionStart(context, item.id.toString(), 0)
        }
    }
}
