package com.tinytiger.common.widget

import android.app.Dialog

import android.os.Bundle

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.tinytiger.common.R
import com.tinytiger.common.adapter.ShareAdapter
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.event.ShareEvent
import com.tinytiger.common.net.data.ShareInfo
import com.tinytiger.common.utils.preference.SpUtils
import kotlinx.android.synthetic.main.dialog_share2.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author zhw_luke
 * @date 2020/5/13 0013 下午 1:39
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 分享dialog
 */class FullShareDialog : BottomSheetDialogFragment() {

    var class_name = "share"
    //    分享数据
    var share_url = ""
    var share_title = ""
    var share_desc = ""
    var share_image = ""

    var userId = "1"
    //举报类型： 1内容 2评论 3用户 4派对 5评价
    var report_type=0

    //内容id
    var contentId = ""

    /**
     * 收藏
     * 0不显示
     * 1收藏
     * 2取消收藏
     */
    var collectionType = 0

    private var mBehavior: BottomSheetBehavior<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.dialog_share2, null)
        dialog.setContentView(view)

        mBehavior = BottomSheetBehavior.from(view.parent as View)
        mBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dismiss()
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            }

            override fun onSlide(view: View, v: Float) {

            }
        })

        bindView()
        bindView2()
        tv_cancel.setOnClickListener {
            dismiss()
        }

        val layoutParams = view.layoutParams
        layoutParams.height = (context!!.resources.displayMetrics.heightPixels * 0.6).toInt()
        view.layoutParams = layoutParams

        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }
    private val mAdapter by lazy { ShareAdapter() }
    private val mAdapter2 by lazy { ShareAdapter() }

    fun bindView() {
        val list =ArrayList<ShareInfo>()
        list.add(ShareInfo("微信", ContextCompat.getDrawable(context!!,R.mipmap.icon_share_wx1)))
        list.add(ShareInfo("QQ", ContextCompat.getDrawable(context!!,R.mipmap.icon_share_qq1)))
        list.add(ShareInfo("朋友圈", ContextCompat.getDrawable(context!!,R.mipmap.icon_share_wxf)))
        list.add(ShareInfo("QQ空间", ContextCompat.getDrawable(context!!,R.mipmap.icon_qq_zone)))
        list.add(ShareInfo("复制链接", ContextCompat.getDrawable(context!!,R.mipmap.icon_share_copy)))

        recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.adapter=mAdapter
        mAdapter.setNewInstance(list)

        mAdapter.setOnItemClickListener { _, _, position ->
            dismiss()
            when(mAdapter.data[position].title){
                "微信"->{
                    share("WEIXIN")
                }
                "QQ"->{
                    share("QQ")
                }
                "朋友圈"->{
                    share("WEIXIN_CIRCLE")
                }
                "QQ空间"->{
                    share("QQZONE")
                }
                "复制链接"->{
                    share("Copy")
                }
            }
        }


    }

    fun bindView2() {
        val list =ArrayList<ShareInfo>()
        if (collectionType == 1) {
            list.add(ShareInfo("收藏", ContextCompat.getDrawable(context!!,R.mipmap.icon_share_collection)))
        } else if (collectionType == 2) {
            list.add(ShareInfo("取消收藏", ContextCompat.getDrawable(context!!,R.mipmap.icon_share_collection_on)))
        }

        if (report_type!=0){
            if (SpUtils.getString(R.string.user_id, "") != userId) {
                //用户自己去除举报
                list.add(ShareInfo("举报", ContextCompat.getDrawable(context!!,R.mipmap.icon_share_report)))
            }
        }
        if (list.size==0){
            recycler_view1.visibility=View.GONE
            return
        }

        recycler_view1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recycler_view1.adapter=mAdapter2
        mAdapter2.setNewInstance(list)

        mAdapter2.setOnItemClickListener { _, _, position ->
            dismiss()
            when(mAdapter2.data[position].title){
                "收藏"->{
                    if(SpUtils.getString(R.string.token,"").isEmpty()){
                        EventBus.getDefault().post(ClassEvent("LoginActivity"))
                    }else{
                        listener?.click(1)
                    }
                }
                "取消收藏"->{
                    if(SpUtils.getString(R.string.token,"").isEmpty()){
                        EventBus.getDefault().post(ClassEvent("LoginActivity"))
                    }else{
                        listener?.click(1)
                    }
                }
                "举报"->{
                    if(SpUtils.getString(R.string.token,"").isEmpty()){
                        EventBus.getDefault().post(ClassEvent("LoginActivity"))
                    }else{
                        EventBus.getDefault().post(ClassEvent("ReportActivity", report_type, contentId,userId))
                    }
                }
            }
        }

    }

    private fun share(platform: String) {
        listener?.click(0)
        EventBus.getDefault()
            .post(ShareEvent(class_name, share_url, share_title, share_desc, share_image+"?imageView2/1/w/200",contentId, platform))
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener): FullShareDialog {
        this.listener = listener
        return this
    }

    interface OnItemClickListener {
        //0 其他分享 1 收藏
        fun click(type: Int)
    }
}