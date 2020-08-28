package com.tinytiger.titi.adapter.mine


import android.widget.Button

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.StringInfo
import com.tinytiger.titi.R


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 举报
 */
class ReportAdapter :
    BaseQuickAdapter<StringInfo, BaseViewHolder>(R.layout.report_item_reason, null) {

    override fun convert(holder: BaseViewHolder, bean: StringInfo) {
        holder.setText(R.id.tvTitle, bean.title)
        if (bean.type){
            holder.setGone(R.id.ivApply,false)
        }else{
            holder.setGone(R.id.ivApply,true)
        }


        holder.getView<Button>(R.id.tvTitle).setOnClickListener {
            val t= bean.type
            for (i in data){
                i.type=false
            }
            bean.type=!t
            if (bean.type){
                holder.setGone(R.id.ivApply,false)
            }else{
                holder.setGone(R.id.ivApply,true)
            }
            listener?.onType(bean.type,bean.id)
        }
    }

    interface onReportListener {
        fun onType(type: Boolean,id:Int)
    }

    var listener: onReportListener? = null

}