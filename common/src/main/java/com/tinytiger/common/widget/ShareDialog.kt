package com.tinytiger.common.widget


import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.R
import com.tinytiger.common.adapter.ShareAdapter
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.event.ShareEvent
import com.tinytiger.common.net.data.ShareInfo
import com.tinytiger.common.utils.AppStoreUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.common.utils.umeng.GameAgentUtils
import com.tinytiger.common.widget.base.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_share2.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author zhw_luke
 * @date 2020/5/20 0020 下午 4:44
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc app分享页
 */
class ShareDialog : BaseBottomDialog() {

    var class_name = "share"
    //    分享数据
    var share_url = ""
    var share_title = ""
    var share_desc = ""
    var share_image = "https://cdn.tinytiger.cn/b55e2898efbe44673559b1a287a9960.png"

    var userId = "1"

    var id = ""
    //内容id
    var contentId = ""

    // 举报类型0不显示 1内容 2评论 3用户 4派对 5评价 6帖子
    var report_type = 0

    //0-从首页进来 1-从发现进来
    var openFrom=0


    /**
     * 收藏
     * 0不显示
     * 1收藏
     * 2取消收藏
     */
    var collectionType = 0

    /**
     * 布局展示方式
     * true  Grid 4个多排
     * false 单排滑动
     */
    var lineLayout = false
    private var mFragmentManager: FragmentManager? = null

    companion object {
        fun create(manager: FragmentManager): ShareDialog {
            val dialog = ShareDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }

    override fun getLayoutRes(): Int = R.layout.dialog_share2
    private val mAdapter by lazy { ShareAdapter() }
    override fun bindView(v: View?) {
        recycler_view1.visibility = View.GONE
        ivLine.visibility = View.GONE

        val list = ArrayList<ShareInfo>()
        list.add(ShareInfo("微信", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_wx1)))
        list.add(ShareInfo("QQ", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_qq1)))
        list.add(ShareInfo("朋友圈", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_wxf)))
        list.add(ShareInfo("QQ空间", ContextCompat.getDrawable(context!!, R.mipmap.icon_qq_zone)))
        list.add(ShareInfo("复制链接", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_copy)))
        if (collectionType == 1) {
            list.add(ShareInfo("收藏", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_collection)))
        } else if (collectionType == 2) {
            list.add(ShareInfo("取消收藏", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_collection_on)))
        }

        if (report_type != 0) {
            if (SpUtils.getString(R.string.user_id, "") != userId) {
                //用户自己去除举报
                list.add(ShareInfo("举报", ContextCompat.getDrawable(context!!, R.mipmap.icon_share_report)))
            }
        }

        if (lineLayout) {
            recycler_view.layoutManager = GridLayoutManager(context, 4)
        } else {
            recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }


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
                "收藏" -> {
                    if (SpUtils.getString(R.string.token, "").isEmpty()) {
                        EventBus.getDefault().post(ClassEvent("LoginActivity"))
                    } else {
                        listener?.click(1)
                    }
                }
                "取消收藏" -> {
                    if (SpUtils.getString(R.string.token, "").isEmpty()) {
                        EventBus.getDefault().post(ClassEvent("LoginActivity"))
                    } else {
                        listener?.click(1)
                    }
                }
                "举报" -> {
                    EventBus.getDefault().post(ClassEvent("ReportActivity", report_type, contentId, userId))
                }
            }
            if("举报" != mAdapter.data[position].title&&"取消收藏"!=mAdapter.data[position].title){
                if (class_name=="GameDetail"){
                    setGameDetail(mAdapter.data[position].title)
                }else if (class_name=="GameAssess"){
                    setGameAssess(position)
                }else if (class_name=="Post"){
                    setPost(mAdapter.data[position].title)
                }
            }


            dismiss()
        }

        tv_cancel.setOnClickListener {
            dismiss()
        }
    }


    private fun share(platform: String) {
        listener?.click(0)
        EventBus.getDefault()
            .post(
                ShareEvent(
                    class_name,
                    share_url,
                    share_title,
                    share_desc,
                     "$share_image?imageView2/1/w/200",
                    contentId,
                    platform
                )
            )
    }


    fun setFragmentManager(manager: FragmentManager): ShareDialog {
        mFragmentManager = manager
        return this
    }


    fun show(): ShareDialog {
        show(mFragmentManager)
        return this
    }


    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener): ShareDialog {
        this.listener = listener
        return this
    }

    interface OnItemClickListener {
        //0 其他分享 1 收藏
        fun click(type: Int)
    }

    private fun setGameDetail(title:String){
        var tag=0
        when (title) {
            "微信" -> {
                tag=0
            }
            "QQ" -> {
                tag=1
            }
            "朋友圈" -> {
                tag=2
            }
            "QQ空间" -> {
                tag=3
            }
            "复制链接" -> {
                tag=4
            }
        }
        GameAgentUtils.setGameDetailShare(contentId,tag)
    }
    private fun setGameAssess(position:Int){
        var type=3
        if(position==5){
            type=5
        }
        GameAgentUtils.setGameDetailInfo(id, contentId, type,openFrom)
    }

    private fun setPost(title:String){
        CircleAgentUtils.setCirclePostInfo(id, contentId, 3)
    }
}