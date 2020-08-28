package com.tinytiger.titi.widget.dialog


import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.tinytiger.common.adapter.ShareAdapter
import com.tinytiger.common.event.ShareEvent
import com.tinytiger.common.net.data.ShareInfo
import com.tinytiger.common.utils.AppStoreUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.image.ScreenShootUtil
import com.tinytiger.common.widget.base.BaseBottomDialog
import com.tinytiger.common.widget.picbrowser.tool.image.DownloadPictureUtil
import com.tinytiger.titi.R
import kotlinx.android.synthetic.main.dialog_share_screen.*
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/7/23 0023 下午 4:30
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 截屏分享
 */
class ShareScreenDialog : BaseBottomDialog() {

    private var mFragmentManager: FragmentManager? = null
    private val mAdapter by lazy { ShareAdapter() }
    var class_name = "share"

    //    分享数据
    var share_url = ""
    var share_title = ""
    var share_desc = ""
    var share_image = "https://cdn.tinytiger.cn/b55e2898efbe44673559b1a287a9960.png"
    var bitmap: Bitmap? = null

    var userId = "1"

    var id = ""

    //内容id
    var contentId = ""

    // 举报类型0不显示 1内容 2评论 3用户 4派对 5评价 6帖子
    var report_type = 0
    var shareBitmap:Bitmap?=null
    var measureHeight=0
    /**
     * 收藏
     * 0不显示
     * 1收藏
     * 2取消收藏
     */
    var collectionType = 0
    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener): ShareScreenDialog {
        this.listener = listener
        return this
    }

    interface OnItemClickListener {
        //0 其他分享 1 收藏
        fun click(type: Int)
    }

    companion object {
        fun create(manager: FragmentManager): ShareScreenDialog {
            val dialog = ShareScreenDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }

    private fun setFragmentManager(manager: FragmentManager): ShareScreenDialog {
        mFragmentManager = manager
        return this
    }

    fun show(): ShareScreenDialog {
        show(mFragmentManager)
        return this
    }

    override fun getLayoutRes(): Int = R.layout.dialog_share_screen


    override fun bindView(view: View) {
        var imgUrl = "https://cdn.tinytiger.cn/FtU0sKyPaFdrQKQG_e131CZO4FhO"

//        var params=ivShotBg.layoutParams
//        params.height=measureHeight
//        ivShotBg.layoutParams=params
        GlideUtil.loadImg(iv_avatar, imgUrl)
        GlideUtil.loadImg(iv_bg, imgUrl)


        ivShotBg.setImageBitmap(bitmap)


        val list = ArrayList<ShareInfo>()
        list.add(ShareInfo("微信",
            ContextCompat.getDrawable(context!!, com.tinytiger.common.R.mipmap.icon_share_wx1)))
        list.add(ShareInfo("朋友圈",
            ContextCompat.getDrawable(context!!, com.tinytiger.common.R.mipmap.icon_share_wxf)))
        list.add(ShareInfo("QQ",
            ContextCompat.getDrawable(context!!, com.tinytiger.common.R.mipmap.icon_share_qq1)))
        list.add(ShareInfo("QQ空间",
            ContextCompat.getDrawable(context!!, com.tinytiger.common.R.mipmap.icon_qq_zone)))
        list.add(ShareInfo("保存到相册",
            ContextCompat.getDrawable(context!!, com.tinytiger.common.R.mipmap.icon_share_copy)))

        recycler_view1.layoutManager = GridLayoutManager(context, 5)
        recycler_view1.adapter = mAdapter
        mAdapter.setNewInstance(list)
        initListener()
    }

    fun initListener() {
        tv_cancel.setOnClickListener {
            dismiss()
        }
        mAdapter.setOnItemClickListener { _, _, position ->
//            shareBitmap = ScreenShootUtil.shotScrollView(scroll_view)
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
                    share("WEIXIN_CIRCLE")
                }
                "QQ空间" -> {
                    if (!AppStoreUtils.startQQ(activity)) {
                        return@setOnItemClickListener
                    }
                    share("QQZONE")
                }
                "保存到相册" -> {

                    DownloadPictureUtil.saveImageToGallery(context!!.applicationContext,
                        shareBitmap)
                }
            }


            dismiss()
        }
    }

    private fun share(platform: String) {

        EventBus.getDefault()
            .post(ShareEvent(shareBitmap, platform))
    }

}