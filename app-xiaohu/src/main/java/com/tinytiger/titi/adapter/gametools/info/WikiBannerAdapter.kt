package com.tinytiger.titi.adapter.gametools.info


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.wiki.WikiChildBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/**
 *
 * @author zhw_luke
 * @date 2020/3/26 0026 下午 4:31
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc wiki多图列表
 */
class WikiBannerAdapter : BaseQuickAdapter<WikiChildBean, BaseViewHolder>(R.layout.game_wiki_item_banner_img, null) {

    override fun convert(holder: BaseViewHolder, item: WikiChildBean) {
        GlideUtil.loadImg(holder.getView(R.id.ivIcon),item.img_url)
        holder.getView<View>(R.id.ivIcon).setOnClickListener {
            mListener?.setBannerId(item)
        }
    }


    var mListener: OnBannerListener? = null

    interface OnBannerListener {
        fun setBannerId(item:WikiChildBean)
    }


}