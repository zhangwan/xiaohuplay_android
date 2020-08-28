package com.tinytiger.titi.adapter.mine


import android.content.pm.ResolveInfo
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.titi.R



/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索用户记录
 */
class AppStoreAdapter :
    BaseQuickAdapter<ResolveInfo, BaseViewHolder>(
        R.layout.mine_item_appstore,
        null
    ) {

    override fun convert(holder: BaseViewHolder, item: ResolveInfo) {
        holder.setText(R.id.tvName, item.activityInfo.loadLabel(context.packageManager))
        holder.getView<ImageView>(R.id.ivIcon).setImageDrawable(item.activityInfo.loadIcon(context.packageManager))

    }


}