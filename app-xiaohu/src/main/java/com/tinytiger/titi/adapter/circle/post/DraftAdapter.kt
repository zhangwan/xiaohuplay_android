package com.tinytiger.titi.adapter.circle.post


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.postsend.DraftBean
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 草稿箱
 */
class DraftAdapter :
    BaseQuickAdapter<DraftBean, BaseViewHolder>(R.layout.circle_item_draft, null) {

    override fun convert(holder: BaseViewHolder, item: DraftBean) {
        holder.setText(R.id.tvName, item.content)
        holder.setText(R.id.tvNumber, item.create_time)
        holder.setGone(R.id.ivIcon, false)

        if (item.img_url != null && item.img_url.size > 0) {
            holder.setGone(R.id.iv_video_start, true)
            GlideUtil.loadImg(holder.getView(R.id.ivIcon), item.img_url[0])

        } else if (item.cover_url != null && item.cover_url.isNotEmpty()) {
            holder.setGone(R.id.iv_video_start, false)
            GlideUtil.loadImg(holder.getView(R.id.ivIcon), item.cover_url)

        } else if (item.video_url != null && item.video_url.isNotEmpty()) {
            holder.setGone(R.id.iv_video_start, false)
            GlideUtil.loadImg(holder.getView(R.id.ivIcon), item.video_url)

        } else {
            holder.setGone(R.id.ivIcon, true)
            holder.setGone(R.id.iv_video_start, true)
        }
    }


    fun deleteData(id: Int) {
        var ben: DraftBean? = null
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
