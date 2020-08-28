package com.tinytiger.common.adapter


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.R
import com.tinytiger.common.net.data.ShareInfo

/**
 *
 * @author zhw_luke
 * @date 2019/12/17 0017 上午 10:29
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
class ShareAdapter : BaseQuickAdapter<ShareInfo, BaseViewHolder>(R.layout.dialog_share_item, null) {

    override fun convert(holder: BaseViewHolder, item: ShareInfo) {
        holder.setText(R.id.tvShare, item.title)
        holder.setImageDrawable(R.id.ivShare,item.path)
    }


}