package com.tinytiger.titi.adapter.gametools



import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.RecommendVideoBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 视频介绍 画廊效果 adapter
*/
class GameVideoAdapter : BaseQuickAdapter<RecommendVideoBean, BaseViewHolder>(R.layout.game_item_video_introduct, null) {

    override fun convert(holder: BaseViewHolder, item: RecommendVideoBean) {
        GlideUtil.loadImg(holder.getView(R.id.iv_image),item.cover)
        holder.setText(R.id.tv_title,item.title)
    }
}