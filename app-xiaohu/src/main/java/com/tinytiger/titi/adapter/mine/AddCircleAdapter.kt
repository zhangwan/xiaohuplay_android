package com.tinytiger.titi.adapter.mine

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.CircleHistoryBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import kotlinx.android.synthetic.main.item_add_circle.view.*

class AddCircleAdapter:
    BaseQuickAdapter<CircleHistoryBean, BaseViewHolder>(R.layout.item_add_circle, null) {

    override fun convert(holder: BaseViewHolder, item: CircleHistoryBean) {
       with(holder.itemView){
           GlideUtil.loadImg(ivLogo,item.logo)
           tvGameName.text=item.name
           tvAddCircleTips.text=context.getString(R.string.mine_circle_num_tips,item.add_circle_num)
       }
    }
}