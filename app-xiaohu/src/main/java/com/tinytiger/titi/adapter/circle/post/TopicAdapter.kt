package com.tinytiger.titi.adapter.circle.post

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.postsend.SelectTopicBean
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.circle.post.SelectTopicActivity
import java.util.ArrayList


/**
 *
 * @author zhw_luke
 * @date 2020/5/6 0006 下午 3:05
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 选择的话题列表
 */
class TopicAdapter :
    BaseQuickAdapter<SelectTopicBean, BaseViewHolder>(R.layout.circle_item_topic, null) {

    var mActivity: Activity? = null
    var tvCirclerNum: TextView? = null
    override fun convert(holder: BaseViewHolder, item: SelectTopicBean) {

        val tvName = holder.getView<TextView>(R.id.tvName)
        if (item.id == 0) {
            tvName.text = "+ 添加圈子"
            tvName.setTextColor(ContextCompat.getColor(context, R.color.color_ff556e))
            tvName.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_5_ff556e)
            holder.setGone(R.id.ivDelete, true)
            holder.getView<View>(R.id.ll_content).setOnClickListener {
                SelectTopicActivity.actionStart(mActivity!!)
            }
        } else {
            tvName.text = item.name
            tvName.setTextColor(ContextCompat.getColor(context, R.color.color_d7ab00))
            tvName.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_5_d7ab00)

            holder.setGone(R.id.ivDelete, false)
            holder.getView<View>(R.id.ivDelete).setOnClickListener {
                remove(item)
                if (data.size > 0 && data[data.size - 1].id != 0) {
                    addData(SelectTopicBean(0))
                }
                setCirclerNum()
            }
        }
    }

    fun addTopicBean(bean: SelectTopicBean) {
        addData(0, bean)
        if (data.size > 3) {
            remove(data.size - 1)
        }
        setCirclerNum()

    }
    fun addTopicAdd(bean: ArrayList<String>) {
        val list=ArrayList<SelectTopicBean>()
        for (i in bean){
            list.add(SelectTopicBean(i))
        }

        addData(0, list)
        if (data.size > 3) {
            remove(data.size - 1)
        }
        setCirclerNum()

    }

    fun setCirclerNum(){
        var size = 0
        for (i in data) {
            if (i.id != 0) {
                size++
            }
        }
        tvCirclerNum?.text = "(${size}/3)"
    }
}