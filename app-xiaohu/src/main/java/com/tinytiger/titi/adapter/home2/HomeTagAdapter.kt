package com.tinytiger.titi.adapter.home2


import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.home2.GameCategoryBean
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_标签列表
 */
class HomeTagAdapter :
    BaseQuickAdapter<GameCategoryBean.GameCategory, BaseViewHolder>(R.layout.home_item_tag, null) {

    override fun convert(holder: BaseViewHolder, item: GameCategoryBean.GameCategory) {
        val tvSize = holder.getView<TextView>(R.id.tvTab)
        holder.setText(R.id.tvName, item.game_name)
        when (holder.adapterPosition % 3) {
            1 -> {
                tvSize.setTextColor(ContextCompat.getColor(context, R.color.color_0fb50a))
            }
            2 -> {
                tvSize.setTextColor(ContextCompat.getColor(context, R.color.color_d7ab00))
            }
            else -> {
                tvSize.setTextColor(ContextCompat.getColor(context, R.color.color_ff556e))
            }
        }

        holder.getView<View>(R.id.rlInfo).setOnClickListener {

        }
//solid_rectangle_white_30
    }
}
