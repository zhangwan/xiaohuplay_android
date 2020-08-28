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
 * @doc 道具商城搜索
 */
class PropsSearchAdapter : BaseQuickAdapter<PropsBean, BaseViewHolder>(R.layout.props_item_search, null) {

    var searchTxt=true
    override fun convert(holder: BaseViewHolder, data: PropsBean) {

        GlideUtil.loadImg(holder.getView(R.id.ivIcon), data.image)
        holder.setText(R.id.tvName,data.name)
        if (searchTxt){
            holder.setText(R.id.tvDesc,"${data.price.toInt()}H币")
        }else{
            holder.setText(R.id.tvDesc,"${data.total_price.toInt()}H币")
        }
        holder.setText(R.id.tvCate,data.cate_name)
    }


}