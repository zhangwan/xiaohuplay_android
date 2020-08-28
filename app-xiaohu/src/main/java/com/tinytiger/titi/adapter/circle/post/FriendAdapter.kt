package com.tinytiger.titi.adapter.circle.post

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.postsend.FriendBean
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.circle.post.SelectFriendActivity


/**
 *
 * @author zhw_luke
 * @date 2020/5/6 0006 下午 3:05
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 选择的回答人员列表
 */
class FriendAdapter:
    BaseQuickAdapter<FriendBean, BaseViewHolder>(R.layout.circle_item_friend, null) {

    var mActivity: Activity?=null

    var circleId=""
    var tvCirclerNum:TextView?=null
    override fun convert(holder: BaseViewHolder, item: FriendBean) {

        val tvName=holder.getView<TextView>(R.id.tvName)
        if (item.user_id==0){
            tvName.text="@ 邀请回答"
            tvName.setTextColor(ContextCompat.getColor(context,R.color.color_ff556e))
            tvName.background= ContextCompat.getDrawable(context,R.drawable.stroke_rectangle_5_ff556e)
            holder.setGone(R.id.ivDelete,true)
            holder.getView<View>(R.id.ll_content).setOnClickListener {
                var size = 0
                for (i in data) {
                    if (i.user_id != 0) {
                        size++
                    }
                }
                SelectFriendActivity.actionStart(mActivity!!, data as ArrayList<FriendBean>)
            }
        }else{
            tvName.text= item.nickname
            tvName.setTextColor(ContextCompat.getColor(context,R.color.color_d7ab00))
            tvName.background=null

            holder.setGone(R.id.ivDelete,false)
            holder.getView<View>(R.id.ivDelete).setOnClickListener {
                remove(item)

                if (data.size > 0 && data[data.size - 1].user_id != 0) {
                    addData(FriendBean(0))
                }
                setFriendNum()
            }
        }
    }


    fun setlist(list :ArrayList<FriendBean>){
        setNewInstance(list)

        if (data.size >= 5) {
           // remove(data.size - 1)
        }else{
            addData(FriendBean(0))
        }
        setFriendNum()
    }

    fun setFriendNum(){
        var size = 0
        for (i in data) {
            if (i.user_id != 0) {
                size++
            }
        }
        tvCirclerNum?.text = "${size}/5"
    }
}