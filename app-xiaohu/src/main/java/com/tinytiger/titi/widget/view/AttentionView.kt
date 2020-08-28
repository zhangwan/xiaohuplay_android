package com.tinytiger.titi.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R

/**
 * @author zhw_luke
 * @date 2020/4/1 0001 下午 6:12
 * @Copyright 小虎互联科技
 * @doc 关注按钮
 * @since 2.1.0
 */
class AttentionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    private var attention: TextView? = null
    private var isMutual = -1
    var userId = ""
    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_attention, this)
        attention = view.findViewById(R.id.attention)
        view.setOnClickListener {
            if (mListener != null && isMutual != 2 && isMutual != -2) {
                mListener!!.onAttentionView(isMutual)
            }
        }
    }

    fun setTextSize(size: Float) {
        attention?.textSize = size
    }

    fun setMutual(is_mutual: Int, user_id: String) {
        if (SpUtils.getString(R.string.user_id, "0") == user_id) {
            attention!!.visibility = View.GONE
            isMutual=2
        } else {
            setMutual(is_mutual)
        }
    }


    fun setMutual(is_mutual: Int) {
        isMutual = is_mutual
        //1:互相关注 0:已关注 -1:未关注 -2:自己
        attention!!.visibility = View.VISIBLE
        when (is_mutual) {
            1 -> {
                attention!!.isSelected = true
                attention!!.text = "互相关注"
                attention!!.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                attention!!.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_aaaaaa)
            }
            0 -> {
                attention!!.isSelected = true
                attention!!.text = "已关注"
                attention!!.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                attention!!.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_aaaaaa)
            }
            -1 -> {
                attention!!.isSelected = false
                attention!!.text = "+ 关注"
                attention!!.setTextColor(ContextCompat.getColor(context, R.color.color_ffcc03))
                attention!!.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_ffcc03)
            }
            -2 -> {
                attention!!.visibility = View.GONE
            }
            2 -> {
                attention!!.visibility = View.GONE
            }
        }
    }


    var mListener: OnAttentionViewListener? = null

    interface OnAttentionViewListener {
        fun onAttentionView(is_mutual: Int)
    }

    init {
        init(context)
    }

    fun setGameMutual(is_mutual: Int){
        isMutual=is_mutual
        attention!!.visibility = View.VISIBLE
        if (is_mutual == 0) {
            attention!!.isSelected = false
            attention!!.text = "+ 关注"
            attention!!.setTextColor(ContextCompat.getColor(context, R.color.color_ffcc03))
            attention!!.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_ffcc03)
        }else {
            attention!!.isSelected = true
            attention!!.text = "已关注"
            attention!!.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
            attention!!.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_aaaaaa)
        }
    }

    fun setJoin(is_join: Int) {
        isMutual = is_join
        attention!!.visibility = View.VISIBLE
        if (is_join == 1) {
            attention!!.isSelected = true
            attention!!.text = "已加入"
            attention!!.setTextColor(ContextCompat.getColor(context, R.color.gray28))
            attention!!.background = ContextCompat.getDrawable(context, R.drawable.solid_gradient_15_eaeaea)
        } else {
            attention!!.isSelected = false
            attention!!.text = "加入圈子"
            attention!!.setTextColor(ContextCompat.getColor(context, R.color.gray33))
            attention!!.background = ContextCompat.getDrawable(context, R.drawable.solid_gradient_15_ffcc03)
        }

    }

}