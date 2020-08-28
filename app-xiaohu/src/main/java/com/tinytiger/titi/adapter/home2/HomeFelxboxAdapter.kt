package com.tinytiger.titi.adapter.home2


import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.AssessTagBean
import com.tinytiger.titi.R



/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_标签列表
 */
class HomeFelxboxAdapter :
    BaseQuickAdapter<AssessTagBean, BaseViewHolder>(R.layout.home_item_felxbox, null) {

    override fun convert(helper: BaseViewHolder, item: AssessTagBean) {
        val rlInfo = helper.getView<LinearLayout>(R.id.rlInfo)
        val tvTitle = helper.getView<TextView>(R.id.tvTitle)
        tvTitle.text = "${item.viewpoint}"

        when (helper.adapterPosition % 3) {
            1 -> {
                rlInfo.background = ContextCompat.getDrawable(context, R.drawable.solid_rectangle_15_f1f8f2)
                tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_0fb50a))
            }
            2 -> {
                rlInfo.background = ContextCompat.getDrawable(context, R.drawable.solid_rectangle_15_f7f6f0)
                tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_d7ab00))
            }
            else -> {

                tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_ff556e))
                rlInfo.background = ContextCompat.getDrawable(context, R.drawable.solid_rectangle_15_f8f1f1)
            }
        }
    }
}
