package com.tinytiger.titi.adapter.mine


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.mine.ContentBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description:
*/
class HistoryListAdapter : BaseQuickAdapter<ContentBean, BaseViewHolder>(R.layout.mine_item_news, null) {

    override fun convert(holder: BaseViewHolder, bean: ContentBean) {
        GlideUtil.loadImg(holder.getView(R.id.iv_image),bean.cover)
        holder.setText(R.id.tv_title, bean.title)
        holder.setText(R.id.tv_info_0, "#${bean.game_name}")
        holder.setText(R.id.tv_info_1,  StringUtils.sizeToString(bean.comment_num)  )

        holder.setGone(R.id.iv_play,bean.type!=2)

    }



}