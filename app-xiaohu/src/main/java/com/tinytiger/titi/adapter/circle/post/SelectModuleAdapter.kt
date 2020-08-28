package com.tinytiger.titi.adapter.circle.post



import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.tinytiger.common.net.data.circle.postsend.SelectCirclerNameBean
import com.tinytiger.titi.R


/**
 *
 * @author zhw_luke
 * @date 2020/5/6 0006 下午 3:05
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 选择发布圈子
 */
class SelectModuleAdapter :
    BaseQuickAdapter<SelectCirclerNameBean.DataModule, BaseViewHolder>(R.layout.circle_item_module, null) {

    override fun convert(holder: BaseViewHolder, item: SelectCirclerNameBean.DataModule) {
        holder.setText(R.id.tvName, item.name)
    }
}