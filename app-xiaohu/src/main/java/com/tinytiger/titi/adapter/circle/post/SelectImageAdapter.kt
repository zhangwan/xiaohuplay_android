package com.tinytiger.titi.adapter.circle.post

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.j256.ormlite.stmt.query.In
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean
import com.tinytiger.common.utils.image.GlideUtil

import com.tinytiger.titi.R
import com.tinytiger.titi.data.circle.ImageBean


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc push 选择图片
 */
class SelectImageAdapter :
    BaseQuickAdapter<ImageBean, BaseViewHolder>(R.layout.circle_item_imageselest, null) {

    //限制最大图片数量,默认是9
    private var limit: Int = 9

    override fun convert(holder: BaseViewHolder, item: ImageBean) {
        val ivIcon = holder.getView<ImageView>(R.id.ivIcon)
        val ivIcon2 = holder.getView<ImageView>(R.id.ivIcon1)
        val ivDelete = holder.getView<ImageView>(R.id.ivDelete)

        //限制最大图片数量
        if (itemCount >= 2 && holder.adapterPosition == limit) {
            ivIcon2.visibility = View.GONE
            ivIcon.visibility = View.GONE
            ivDelete.visibility = View.GONE
        } else {
            if (item.url.isEmpty()) {
                ivIcon2.visibility = View.VISIBLE
                ivIcon.visibility = View.GONE
                ivDelete.visibility = View.GONE
            } else {
                ivIcon2.visibility = View.GONE
                ivIcon.visibility = View.VISIBLE
                ivDelete.visibility = View.VISIBLE

                GlideUtil.loadImg(ivIcon, item.url)

            }
        }

        holder.getView<View>(R.id.ivDelete).setOnClickListener {
            remove(item)
            mListener?.onItemDelete(item)
//            if (data.size > 0 && data[data.size - 1].type == 0) {
//                addData(ImageBean(1))
//            }
        }
    }


    fun setLimit(limit: Int) {
        this.limit = limit
    }

    fun setList(list: ArrayList<ImageBean>) {
        addData(0, list)
        if (data.size > 9) {
            remove(data.size - 1)
        }
    }

    fun setList(list: ImageBean) {
        addData(0, list)
        if (data.size > 9) {
            remove(data.size - 1)
        }
        notifyDataSetChanged()
    }

    var mListener: OnItemDeleteListener? = null

    interface OnItemDeleteListener {
        fun onItemDelete(item: ImageBean)
    }

    fun setOnItemDeleteListener( listener: OnItemDeleteListener){
        this.mListener=listener
    }

}
