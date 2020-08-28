package com.tinytiger.titi.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.other.Image9Adapter
import kotlinx.android.synthetic.main.view_iamge9.view.*


/**
 * @author zhw_luke
 * @date 2020/4/1 0001 下午 6:12
 * @Copyright 小虎互联科技
 * @doc 九宫格图片展示
 * @since 2.1.0
 */
class Image9View : FrameLayout {

    constructor(context: Context) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.view_iamge9, this)
        recyclerImg.adapter = mAdapter
        recyclerImg.setNestedScrollingEnabled(false)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_iamge9, this)
        recyclerImg.adapter = mAdapter
        recyclerImg.setNestedScrollingEnabled(false)
    }

    private val mAdapter by lazy { Image9Adapter() }

    /**
     * 根据图片数量的不同,设置不同的布局
     * 动态布局
     */
    fun setImages(list: ArrayList<String>) {
        when (list.size) {
            1 -> {
                recyclerImg.layoutManager = LinearLayoutManager(context)
                mAdapter.height = Dp2PxUtils.dip2px(context, 200)
            }
            4 -> {
                recyclerImg.layoutManager = GridLayoutManager(context, 2)
                mAdapter.height = Dp2PxUtils.dip2px(context, 115)
            }
            else -> {
                recyclerImg.layoutManager = GridLayoutManager(context, 3)
                mAdapter.height = Dp2PxUtils.dip2px(context, 115)
            }
        }

        mAdapter.setNewInstance(list)
    }


    /**
     * 九宫格
     * 固定布局
     */
    fun setImages_9(list: ArrayList<String>) {
        recyclerImg.layoutManager = GridLayoutManager(context, 3)
        mAdapter.height = Dp2PxUtils.dip2px(context, 115)
        mAdapter.setNewInstance(list)
    }

    var mListener: OnAttentionViewListener? = null

    interface OnAttentionViewListener {
        fun onAttentionView(is_mutual: Int)
    }

}