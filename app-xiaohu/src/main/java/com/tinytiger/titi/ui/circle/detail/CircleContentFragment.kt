package com.tinytiger.titi.ui.circle.detail

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.circle.CircleInfoBean
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.post.BasePostNodeDataUtils
import com.tinytiger.titi.adapter.post.CommonPostNodeAdapter
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.event.LikeEvent
import com.tinytiger.titi.event.PostEvent
import com.tinytiger.titi.mvp.contract.circle.CirclesInfoContract
import com.tinytiger.titi.mvp.presenter.circle.CirclesInfoPresenter
import com.tinytiger.titi.utils.AutoPlayUtils
import com.tinytiger.titi.utils.AutoPlayUtils.playerMap
import com.tinytiger.titi.utils.net.NetStateChangeObserver
import com.tinytiger.titi.utils.net.NetStateChangeReceiver
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.widget.dialog.FullPostDialog
import com.tinytiger.titi.widget.video.MyJzVideoView
import kotlinx.android.synthetic.main.base_recycler.recycler_view
import kotlinx.android.synthetic.main.base_recycler.refreshLayout
import kotlinx.android.synthetic.main.home_fagment_recommend.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *
 * @author zhw_luke
 * @date 2020/5/11 0011 下午 1:50
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 帖子列表
 */
class CircleContentFragment : BaseFragment(), CirclesInfoContract.View, FullPostDialog.OnFullSheetListener {

    /**
     * 圈子id
     */
    private var circle_id = ""
    /**
     * 帖子模块id
     */
    private var tag_modular_id = ""
    /**
     * 固定帖子类型
     */
    private var fixed_modular_type = ""

    var index = 0


    fun setCircleContentListener(listener: OnCircleContentListener) {
        this.listener = listener
    }

    companion object {
        var refresh = true
        var isType = true
        fun getInstance(circle_id: String, tag_modular_id: String, fixed_modular_type: String,
            index: Int): CircleContentFragment {
            val fragment = CircleContentFragment()
            val bundle = Bundle()
            fragment.circle_id = circle_id
            fragment.tag_modular_id = tag_modular_id
            fragment.fixed_modular_type = fixed_modular_type
            fragment.index = index
            fragment.arguments = bundle

            return fragment
        }
    }

    private val mPresenter by lazy { CirclesInfoPresenter() }
    val mAdapter by lazy { CommonPostNodeAdapter(activity!!, ArrayList()) }
    private var listener: OnCircleContentListener? = null

    private var dataList = arrayListOf<PostBean>()
    private var looperFlag = 0 //0,无自动播放，1.自动播放上一个，2自动播放下一个

    private var menuVisibility: Boolean? = null

    var recyclerView: RecyclerView? = null

    interface OnCircleContentListener {
        fun onContentChangeListener(item: PostBean)
    }

    override fun getLayoutId(): Int = R.layout.base_recycler


    override fun initView() {
        mPresenter.attachView(this)

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = recycler_view
        mAdapter.pageIndex = ConstantsUtils.NOTE.PAGE_DEFAULT
        mAdapter.type = 1
        mAdapter.isVideoType = true
        recycler_view.adapter = mAdapter
        //refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnRefreshListener {
            BasePostNodeDataUtils.isLooper = true
            AutoPlayUtils.index = CircleFragment.mIndex
            playerMap[AutoPlayUtils.index] = -1
            page = 1
            start()
        }
        refreshLayout.setOnLoadMoreListener {
            page++
            start()
        }

        initAdapterCallBack()
    }

    private fun initAdapterCallBack() {
        mAdapter.shareListener = {
            ShareDialog.create(childFragmentManager).apply {
                class_name = "Post"
                share_url = "$shareUrl?post_id=${it.id}"
                share_title = "来自「${it.nickname}」"
                share_desc = it.content
                if (it.img_url != null && it.img_url.size > 0) {
                    share_image = it.img_url[0]
                }
                contentId = it.id
                id = it.circle_id
            }.show()
        }
        mAdapter.attentionListener = { item, is_mutual ->
            mPresenter.doFollow(is_mutual, item.user_id)
        }
        mAdapter.readListener = {
            pauseVideo()
            FullPostDialog().apply {
                postBean = it
                listener = this@CircleContentFragment
            }.show(childFragmentManager)
        }
        mAdapter.likeListener = {
            mPresenter.likePost(it.id)
        }
        mAdapter.itemListener = {
            listener?.onContentChangeListener(it)
        }

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //滑动停止后，
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    AutoPlayUtils.onScrollPlayVideo(recyclerView, dataList, looperFlag, 0)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                looperFlag = if (dy > 0) {
                    2 //自动播放下一个
                } else {
                    1 //自动播放上一个
                }
                AutoPlayUtils.onScrollReleaseAllVideos(layoutManager, 0.5F)
            }
        })

    }


    override fun start() {
        mPresenter.getCircleTagModularPostList(circle_id, tag_modular_id, fixed_modular_type, page)
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        menuVisibility = true
        if (menuVisible) {
            page = 1
            start()
            BasePostNodeDataUtils.isLooper = true
            AutoPlayUtils.pageType = 3


        }
    }

    override fun onResume() {
        super.onResume()
        AutoPlayUtils.pageType = 3
        isType = true
        if (menuVisibility == false) {
            resumeVideo()
        }
    }
    fun resumeVideo(){
        if (playerMap[AutoPlayUtils.index] != -1) {
            BasePostNodeDataUtils.isLooper = true
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged()
            }

        }
    }
   fun pauseVideo(){
       Jzvd.goOnPlayOnPause()
       if (playerMap[AutoPlayUtils.index] != null) {
           playerMap[AutoPlayUtils.index] = playerMap[AutoPlayUtils.index]!!
       }
   }
    override fun onPause() {
        super.onPause()
        menuVisibility = false
        pauseVideo()

    }


    override fun showGameCircleInfo(bean: CircleInfoBean) {

    }

    override fun showCircleList(bean: PostInfoBean) {

        if (bean.data != null && bean.data.data != null) {
            if (bean.data.current_page == 1) {
                mAdapter.data.clear()
                if (bean.data.data.size == 0) {
                    mAdapter.mEmptyView = (RefreshView.getEmptyView(activity, "", recycler_view))
                    mAdapter.notifyDataSetChanged()

                } else {
                    dataList = bean.data.data
                }
            } else {
                dataList.addAll(bean.data.data)
            }
            if (CircleFragment.postBean != null) {
                dataList.add(0, CircleFragment.postBean!!)
                mAdapter.data = dataList
                mAdapter.notifyDataSetChanged()
                recycler_view.smoothScrollToPosition(0)
                CircleFragment.postBean = null
            }

            mAdapter.data = dataList
            mAdapter.notifyDataSetChanged()
            if (bean.data.current_page >= bean.data.last_page) {
                refreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                refreshLayout.resetNoMoreData()
            }


        } else {
            mAdapter.mEmptyView = (RefreshView.getEmptyView(activity, "", recycler_view))
            mAdapter.notifyDataSetChanged()

        }
        shareUrl = bean.data.share_url
    }

    var shareUrl = ""
    override fun showJoin(join: Int) {

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.mEmptyView = (RefreshView.getNetworkView(context, recycler_view, { start() }))
                mAdapter.notifyDataSetChanged()
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAttentionEvent(event: AttentionEvent) {
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].user_id == event.userId) {
                mAdapter.data[i].is_mutual = event.isMutual
                mAdapter.notifyItemChanged(i)
            }
        }
    }

    override fun onDismiss(postid: String) {
        resumeVideo()
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].id == postid) {
                mAdapter.notifyItemChanged(i)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLikeEvent(event: LikeEvent) {
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].id == event.id && mAdapter.data[i].is_like != event.is_like) {
                mAdapter.data[i].is_like = event.is_like
                mAdapter.data[i].like_num = event.like_num
                mAdapter.notifyItemChanged(i)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPostEvent(event: PostEvent) {

        if (event.data != null) {
            if (event.data.circleType) {
                if ((!TextUtils.isEmpty(event.data.modular_id))) {
                    CircleFragment.circleFragment?.refreshPostEvent(event.data!!)
                } else {
                    if (CircleFragment.mIndex != 0 && TextUtils.isEmpty(event.data.modular_id)) {
                        CircleFragment.circleFragment?.refreshPostEvent(event.data!!)
                        return
                    }
                    isType = false
                    refresh = false
                    event.data.status = "0"
                    dataList.add(0, event.data)
                    mAdapter.data = dataList
                    mAdapter.notifyDataSetChanged()
                    recycler_view.smoothScrollToPosition(0)

                }
            }
        } else {
            refresh = true
        }

    }

}