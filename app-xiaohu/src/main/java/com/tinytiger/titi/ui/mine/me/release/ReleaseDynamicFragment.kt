package com.tinytiger.titi.ui.mine.me.release

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.post.CommonPostNodeAdapter
import com.tinytiger.titi.mvp.contract.center.MineWorkContract
import com.tinytiger.titi.mvp.presenter.center.MineWorksPresenter
import com.tinytiger.titi.utils.UIHelper
import com.tinytiger.titi.widget.dialog.FullPostDialog
import kotlinx.android.synthetic.main.activity_base_recycler.*
import kotlinx.android.synthetic.main.activity_base_recycler.recycler_view
import kotlinx.android.synthetic.main.mine_fragment_collection.*
import kotlinx.android.synthetic.main.mine_fragment_collection.refreshLayout

/**
 * @author zwy
 * create at 2020/5/19 0019
 * description:动态
 */
class ReleaseDynamicFragment : BaseFragment(), MineWorkContract.View,
    FullPostDialog.OnFullSheetListener {

    private val mPresenter by lazy { MineWorksPresenter() }
    private val mAdapter by lazy { CommonPostNodeAdapter(activity!!, ArrayList()) }
    var count = 0
    var menuVisible: Boolean = true

    private var data = arrayListOf<PostBean>()
    override fun getLayoutId(): Int = R.layout.release_fragment_dynamic

    override fun initView() {
        mPresenter.attachView(this)
        mAdapter.isFlagType = 1
        mAdapter.pageIndex = ConstantsUtils.NOTE.PAGE_RELEASE
        //评论详情
        mAdapter.type = 3
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        mAdapter.mEmptyView = (RefreshView.getEmptyView(activity, "暂无数据", recycler_view))

        initListener()
    }

    private fun initListener() {
        refreshLayout.setOnRefreshListener {
            page = 1
            mPresenter.getDynamicList(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getDynamicList(page)
        }
        mAdapter.shareListener = {
            ShareDialog.create(childFragmentManager)
                .apply {
                    class_name = "Post"
                    share_url = "${it.share_url}?post_id=${it.id}"
                    share_title = "来自「${it.nickname}」"
                    share_desc = it.content
                    if (it.img_url != null && it.img_url.size > 0) {
                        share_image = it.img_url[0]
                    }
                    contentId = it.id
                    id = it.circle_id
                }
                .show()
        }
        mAdapter.likeListener = {
            mPresenter.likePost(it.id)
        }
        mAdapter.readListener = {
            FullPostDialog().apply {
                postBean = it
                listener = this@ReleaseDynamicFragment
            }.show(childFragmentManager)
        }
        mAdapter.moreListener = { postBean: PostBean, view: View ->
            UIHelper().showPopupWindow(activity!!, view, "删除", View.OnClickListener {
                delComment(postBean.id)
            })
        }
        mAdapter.checkListener = {
            if (it.isSelected) {
                activity?.tvTitle?.rightTxt = "删除"
                activity?.tvTitle?.setRightTxtColor(Color.parseColor("#FF556E"))
            } else {
                var isSelect = false
                for (item in mAdapter.data) {
                    if (item.isSelected) {
                        isSelect = true
                    }
                }
                if (!isSelect) {
                    activity?.tvTitle?.rightTxt = "完成"
                    activity?.tvTitle?.setRightTxtColor(Color.parseColor("#0FB50A"))
                }
            }
        }

    }

    fun delComment(id: String) {
//        var ids = arrayListOf<String>()
//        for (item in mAdapter.data) {
//            if (item.isSelected) {
//                count++
//                ids.add(item.id + "")
//            }
//        }
//        if (ids.size == 0) {
//            showToast("请选择删除的作品")
//            return
//        }
        var ids = arrayListOf<String>()
        ids.add(id)

        TextDialog.create(childFragmentManager)
            .setMessage("确定删除该动态吗？")
            .setViewListener(object : TextDialog.ViewListener {
                override fun click() {

                    mPresenter.delPost(ids)
                }

            })
            .show()

    }

    fun showEditStatus(is_show: Boolean) {
        if (is_show == mAdapter.showDeleteIcon) {
            return
        }

        for (i in mAdapter.data) {
            i.isSelected = false
        }
        mAdapter.showDeleteIcon = is_show
        mAdapter.notifyDataSetChanged()

    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        this.menuVisible = menuVisible
    }

    override fun onResume() {
        super.onResume()
        if (menuVisible) {
            start()
        }
    }

    override fun start() {
        page = 1
        mPresenter.getDynamicList(page)
    }

    override fun showDynamicNodeList(bean: PostInfoBean) {
        var postBean = bean.data
        if (postBean.current_page == 1) {
            mAdapter.showDeleteIcon = false
            data = postBean.data
        } else {
            data.addAll(postBean.data)
        }
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()

        if (postBean.current_page >= postBean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter?.mBottomView = RefreshView.getNewFooterView(context, "", recycler_view)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }
        mAdapter.data = data
        mAdapter.notifyDataSetChanged()
        (activity as MineReleaseActivity).showEditButton(mAdapter.data.size)
    }

    override fun showAnswersNodeList(bean: PostInfoBean) {

    }

    override fun showGameAmwayList(bean: UserGameAmwayList.Data) {

    }

    override fun delUserGameAmway() {
//        showToast(getString(R.string.release_del_hint, count.toString()))
        showToast("删除成功")
        count = 0
        page = 1
        start()
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.mEmptyView =
                    (RefreshView.getNetworkView(activity, recycler_view, { start() }))
                mAdapter.notifyDataSetChanged()
            }
        } else {
            dismissLoading()
        }
    }


    override fun onDismiss(postid: String) {
        for (i in 0 until mAdapter.data.size) {
            if (mAdapter.data[i].id == postid) {
                mAdapter.notifyItemChanged(i)
            }
        }
    }

    companion object {

        fun getInstance(): ReleaseDynamicFragment {
            val fragment = ReleaseDynamicFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }


}