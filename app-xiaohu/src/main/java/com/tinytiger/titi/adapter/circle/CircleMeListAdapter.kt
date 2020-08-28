package com.tinytiger.titi.adapter.circle


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.CircleBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc push 我加入的圈子
 */
class CircleMeListAdapter :
    BaseQuickAdapter<CircleBean, BaseViewHolder>(R.layout.circle_item_me, null) {

    override fun convert(holder: BaseViewHolder, item: CircleBean) {
        holder.setText(R.id.tvName, item.name)
        holder.setText(R.id.tvNumber, "加入人数：${StringUtils.sizeToString(item.add_circle_num)}")

        GlideUtil.loadImg(holder.getView(R.id.ivIcon), item.logo)

        holder.getView<View>(R.id.rl_content).setOnClickListener {
            CirclesDetailsActivity.actionStart(context, item.id,"","")
        }

    }


    fun deleteData(id: String) {
        var ben: CircleBean? = null
        for (i in data) {
            if (i.id == id) {
                ben = i
            }
        }
        if (ben != null) {
            remove(ben)
        }
    }
}
