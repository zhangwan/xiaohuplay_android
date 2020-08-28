package com.tinytiger.titi.ui.mine.homepage

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.ShareEvent
import com.tinytiger.common.net.data.center.MineGameListBean
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.user.UserDynamicBean
import com.tinytiger.common.net.data.user.UserDynamicList
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.home.MultiDyamicAdapter
import com.tinytiger.titi.mvp.contract.center.HomeDynamicContract
import com.tinytiger.titi.mvp.presenter.center.HomeDynamicPresenter
import com.tinytiger.titi.ui.video.VideoActivity
import com.tinytiger.titi.widget.dialog.FullPostDialog
import kotlinx.android.synthetic.main.base_recycler.*


/**
 *
 * @author zhw_luke
 * @date 2020/5/25 0025 下午 2:53
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 用户动态
 */
class HomeDynamicFragment : BaseFragment(), HomeDynamicContract.View, FullPostDialog.OnFullSheetListener {

    override fun showHomeGameList(bean: MineGameListBean.Data) {

    }

    private val mAdapter by lazy { MultiDyamicAdapter(ArrayList()) }

    private var user_id: String = "0"

    companion object {

        fun getInstance(user_id: String): HomeDynamicFragment {
            val fragment = HomeDynamicFragment()
            val bundle = Bundle()

            fragment.arguments = bundle
            fragment.user_id = user_id
            return fragment
        }
    }

    private val mPresenter by lazy { HomeDynamicPresenter() }


    override fun getLayoutId(): Int = R.layout.base_recycler


    override fun initView() {
        mPresenter.attachView(this)
        //评论详情
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            page++
            start()
        }

        mAdapter.listener = object : MultiDyamicAdapter.OnHomePushListener {
            override fun onShare(item: UserDynamicBean) {
                val nickname = (activity as HomepageActivity).mUserCenterBean!!.userinfo.nickname
                ShareDialog.create(childFragmentManager)
                    .apply {
                        class_name = "Post"
                        share_url = "${item.share_url}?post_id=${item.id}"
                        share_title = "来自「${nickname}」"
                        share_desc = item.content
                        if (item.img_url!=null&&item.img_url.size>0){
                            share_image =item.img_url[0]
                        }
                        contentId=item.id
                        id=item.circle_id
                    }
                    .show()
            }

            override fun onLike(item: UserDynamicBean) {
                mPresenter.likePost(item.id)
            }

            override fun onRead(item: UserDynamicBean) {
                mPostBean.id = item.id
                mPostBean.answer_id = item.answer_id
                mPostBean.user_id = user_id
                mPostBean.comment_num = item.comment_num
                FullPostDialog().apply {
                    postBean = mPostBean
                    listener = this@HomeDynamicFragment
                }.show(childFragmentManager)
            }

            override fun onViewVideo(item: UserDynamicBean) {
                val nickname = (activity as HomepageActivity).mUserCenterBean!!.userinfo.nickname
                var share_url = "${item.share_url}?post_id=${item.id}"
                var share_title = "来自「${nickname}」"
                var share_desc = item.content
                var share_image=""
                if (item.img_url != null && item.img_url.size > 0) {
                    share_image = item.img_url[0]
                }
                var shareEvent= ShareEvent(share_url,share_title,share_desc,share_image)
                VideoActivity.actionStart(context!!, item.video_url, 1,shareEvent)
            }
        }
        start()
    }

    val mPostBean = PostBean()
    override fun start() {
        mPresenter.getDynamic(user_id, page)
    }

    override fun showHomeDynamicList(bean: UserDynamicList) {

        mAdapter.removeAllFooterView()
        refreshLayout.finishLoadMore()

        if (bean.data.current_page == 1) {
            if (bean.data == null || bean.data.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getEmptyView(
                        context,
                        "",
                        recycler_view
                    )
                )
            } else {
                mAdapter.setNewInstance(bean.data.data)
            }
        } else {
            mAdapter.addData(bean.data.data)
        }

        if (bean.data.current_page >= bean.data.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getNewFooterView(context, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun onDismiss(id: String) {
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].type == 2 && mAdapter.data[i].id == id) {
                mAdapter.data[i].comment_num = mPostBean.comment_num
                mAdapter.notifyItemChanged(i)
                return
            }
        }
    }
}