package com.tinytiger.common.widget


import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager

import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.R
import com.tinytiger.common.adapter.ShareAdapter

import com.tinytiger.common.event.ShareEvent
import com.tinytiger.common.net.data.ShareInfo
import com.tinytiger.common.utils.AppStoreUtils

import com.tinytiger.common.widget.base.BaseBottomDialog

import kotlinx.android.synthetic.main.dialog_share3.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author zhw_luke
 * @date 2020/5/20 0020 下午 4:44
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc app云游戏分享页
 */
class ShareGameDialog : BaseBottomDialog() {

    var class_name = "share"
    var share_url = ""
    var share_title = ""
    var share_desc = ""
    var share_image = "https://cdn.tinytiger.cn/b55e2898efbe44673559b1a287a9960.png"
    var game_id = ""

    var id = ""

    /**
     * 0普通分享
     * 1.奖励分享
     */
    var type = 0

    private var mFragmentManager: FragmentManager? = null

    companion object {
        fun create(manager: FragmentManager): ShareGameDialog {
            val dialog = ShareGameDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }

    override fun getLayoutRes(): Int = R.layout.dialog_share3
    private val mAdapter by lazy { ShareAdapter() }
    override fun bindView(v: View?) {
        val list = ArrayList<ShareInfo>()
        list.add(ShareInfo("微信", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_wx1)))
        list.add(ShareInfo("QQ", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_qq1)))
        list.add(ShareInfo("朋友圈", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_wxf)))
        list.add(ShareInfo("QQ空间", ContextCompat.getDrawable(context!!, R.mipmap.icon_qq_zone)))

        if (type == 0) {
            list.add(
                ShareInfo("复制链接", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_copy)))
            tv_cancel.visibility = View.VISIBLE
            ivLine.visibility = View.VISIBLE
        } else {
            ll_content.visibility = View.VISIBLE
            tv_cancel.visibility = View.GONE
            ivLine.visibility = View.GONE
        }

        recycler_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recycler_view.adapter = mAdapter
        mAdapter.setNewInstance(list)

        mAdapter.setOnItemClickListener { _, _, position ->
            when (mAdapter.data[position].title) {
                "微信" -> {
                    if (!AppStoreUtils.startWX(activity)) {
                        return@setOnItemClickListener
                    }
                    share("WEIXIN")
                }
                "QQ" -> {
                    if (!AppStoreUtils.startQQ(activity)) {
                        return@setOnItemClickListener
                    }
                    share("QQ")
                }
                "朋友圈" -> {
                    if (!AppStoreUtils.startWX(activity)) {
                        return@setOnItemClickListener
                    }
                    share("WEIXIN_CIRCLE")
                }
                "QQ空间" -> {
                    if (!AppStoreUtils.startQQ(activity)) {
                        return@setOnItemClickListener
                    }
                    share("QQZONE")
                }
                "复制链接" -> {
                    share("Copy")
                }
            }

            dismiss()
        }

        tv_cancel.setOnClickListener {
            dismiss()
        }
        isTransparent = true
    }


    private fun share(platform: String) {
        listener?.click(0)
        EventBus.getDefault()
            .post(ShareEvent(class_name, share_url, share_title, share_desc,
                "$share_image?imageView2/1/w/200", game_id, platform))
    }


    fun setFragmentManager(manager: FragmentManager): ShareGameDialog {
        mFragmentManager = manager
        return this
    }


    fun show(): ShareGameDialog {
        show(mFragmentManager)
        return this
    }


    var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener): ShareGameDialog {
        this.listener = listener
        return this
    }

    interface OnItemClickListener {
        //0 其他分享
        fun click(type: Int)
    }


}