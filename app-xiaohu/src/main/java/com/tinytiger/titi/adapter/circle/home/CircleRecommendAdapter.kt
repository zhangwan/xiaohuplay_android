package com.tinytiger.titi.adapter.circle.home


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.CircleBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 推荐圈子
 */
class CircleRecommendAdapter :
    BaseQuickAdapter<CircleBean, BaseViewHolder>(R.layout.circle_item_cover, null) {

    override fun convert(holder: BaseViewHolder, item: CircleBean) {
        holder.setText(R.id.tvName, "#${item.name}")
        holder.setText(R.id.tvInfo, "${StringUtils.sizeToString(item.hots)}")

        GlideUtil.loadImg(holder.getView(R.id.ivIcon),item.background)

        holder.getView<View>(R.id.rlInfo).setOnClickListener {
            CircleAgentUtils.setCircleRecommend(item.id)
//         CirclesActivity.actionStart(context,item.id)
            CirclesDetailsActivity.actionStart(context,item.id,"","")
        }
    }
}
