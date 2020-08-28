package com.tinytiger.titi.adapter.props


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.props.PropsBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R



/**
 *
 * @author tamas
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 道具商城 兑换码
 */
class PropsCodeAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.props_item_code, null) {


    override fun convert(holder: BaseViewHolder, data: String) {

        holder.setText(R.id.tv_code,data)

    }


}