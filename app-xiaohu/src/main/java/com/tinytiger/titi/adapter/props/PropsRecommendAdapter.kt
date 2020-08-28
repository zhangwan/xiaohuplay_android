package com.tinytiger.titi.adapter.props


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.props.PropsBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R



/**
 *
 * @author tamas
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 推薦工具
 */
class PropsRecommendAdapter : BaseQuickAdapter<PropsBean, BaseViewHolder>(R.layout.props_item_recommend, null) {


    override fun convert(holder: BaseViewHolder, data: PropsBean) {

        GlideUtil.loadImg(holder.getView(R.id.ivIcon), data.image)
        holder.setText(R.id.tvName,data.name)
        holder.setText(R.id.tvDesc,data.cate_name)
        holder.setText(R.id.tvPrice,"${data.price.toInt()}H币")

    }


}