package com.tinytiger.titi.adapter.props


import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.props.PropsBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.props.PropsDetailActivity


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 我的道具 兑换码
 */
class PropsExchangeAdapter : BaseQuickAdapter<PropsBean, BaseViewHolder>(R.layout.props_item_me, null) {


    override fun convert(holder: BaseViewHolder, data: PropsBean) {

        GlideUtil.loadImg(holder.getView(R.id.ivIcon), data.image)

        if (data.num>1){
            holder.setText(R.id.tvName,"${data.name}x${data.num}")
        }else{
            holder.setText(R.id.tvName,data.name)
        }

        holder.setText(R.id.tvDesc,"兑换码")
        val tvDesc=holder.getView<TextView>(R.id.tvDesc)

        tvDesc.setOnClickListener {
            listener?.onExchang(data)
        }

        holder.getView<RelativeLayout>(R.id.rl_content) .setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            PropsDetailActivity.actionStart(context,data.tool_id)
        }
    }

    interface OnExchangListener {
        fun onExchang(data: PropsBean)
    }

    var listener: OnExchangListener? = null
}