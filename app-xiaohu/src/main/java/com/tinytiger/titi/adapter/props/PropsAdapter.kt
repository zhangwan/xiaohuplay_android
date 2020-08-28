package com.tinytiger.titi.adapter.props


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.props.PropsBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R



/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 道具商城首页
 */
class PropsAdapter : BaseQuickAdapter<PropsBean, BaseViewHolder>(R.layout.props_item_home, null) {


    override fun convert(holder: BaseViewHolder, iten: PropsBean) {

        GlideUtil.loadImg(holder.getView(R.id.ivIcon), iten.image)
       // holder.setText(R.id.tvCate,data.cate_id)
        holder.setText(R.id.tvName,iten.name)
        holder.setText(R.id.tvDesc,"${iten.price.toInt()}H币")

    }


}