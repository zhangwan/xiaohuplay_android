package com.tinytiger.common.adapter


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.R
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil

/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description:
*/
class RecommendHAdapter : BaseQuickAdapter<RecommendBean, BaseViewHolder>(R.layout.video_item_recommend_horizontal, null) {

    override fun convert(holder: BaseViewHolder, item: RecommendBean) {
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_time,TimeZoneUtil.formatTimemS(item.video_length *1000))
        GlideUtil.loadImg(holder.getView(R.id.iv_image),item.cover)
    }

}