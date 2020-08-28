package com.tinytiger.titi.ui.circle


import android.os.Bundle
import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.event.MainLoginEvent
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.home.AttentionMutli
import com.tinytiger.common.net.data.circle.home.CircleAttentionBean
import com.tinytiger.common.net.data.circle.home.CircleAttentionUserBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.VolumeChangeObserver
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.home.MultiAttentionAdapter
import com.tinytiger.titi.adapter.post.BasePostNodeDataUtils
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.mvp.contract.circle.CirclesHomeContract
import com.tinytiger.titi.mvp.presenter.circle.CircleshomePresenter
import com.tinytiger.titi.ui.main.MainActivity
import com.tinytiger.titi.utils.AutoPlayUtils
import com.tinytiger.titi.utils.net.NetStateChangeObserver
import com.tinytiger.titi.utils.net.NetStateChangeReceiver
import com.tinytiger.titi.utils.net.NetworkType

import com.tinytiger.titi.widget.dialog.FullPostDialog
import com.tinytiger.titi.widget.video.MyJzVideoView
import kotlinx.android.synthetic.main.circle_fragment_attention.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import java.util.*

/**
 *
 * @author zhw_luke
 * @date 2020/4/28 0028 上午 10:53
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 圈子首页-关注
 */
class CircleAttentionFragment : BaseFragment(), CirclesHomeContract.View, FullPostDialog.OnFullSheetListener,
    NetStateChangeObserver,VolumeChangeObserver.OnVolumeChangeListener  {

    companion object {
        fun getInstance(): CircleAttentionFragment {
            val fragment = CircleAttentionFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.circle_fragment_attention

    private val mPresenter by lazy { CircleshomePresenter() }
    private val mAdapter by lazy { MultiAttentionAdapter(ArrayList()) }

    private var looperFlag = 0 //0,无自动播放，1.自动播放上一个，2自动播放下一个
    private var dataList = arrayListOf<PostBean>()
    private var mVolumeChangeObserver: VolumeChangeObserver? = null
    /**
     * 初始化 ViewI
     */
    override fun initView() {
        TAG="CircleAttentionFragment"
        mPresenter.attachView(this)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        NetStateChangeReceiver.registerReceiver(context!!)
        NetStateChangeReceiver.registerObserver(this)
        mVolumeChangeObserver = VolumeChangeObserver(getContext())
        mVolumeChangeObserver?.setOnVolumeChangeListener(this)
        mVolumeChangeObserver?.registerVolumeReceiver()
        mAdapter.isVideoType = true
        recycler_view.adapter = mAdapter

        refreshLayout.setOnRefreshListener {
            page = 1
            BasePostNodeDataUtils.isLooper = true
            AutoPlayUtils.index = 1
            AutoPlayUtils.homePlayerMap[AutoPlayUtils.index]=-1
            start()
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            start()
        }

        start()
        mAdapter.listener = object : MultiAttentionAdapter.OnHomePushListener {

            override fun onAttention(is_mutual: Int, userid: String) {
                mPresenter.doFollow(is_mutual, userid)
            }

            override fun onLike(item: PostBean) {
                mPresenter.likePost(item.id)
            }

            override fun onRead(item: PostBean) {
                pauseVideo()
                FullPostDialog().apply {
                    postBean = item
                    listener = this@CircleAttentionFragment
                }.show(childFragmentManager)
            }

        }
        initListener()
    }

    private fun initListener() {
        mAdapter.shareListener = {
            ShareDialog.create(childFragmentManager)
                .apply {
                    class_name = "Post"
                    share_url = "$shareUrl?post_id=${it.id}"
                    share_title = "来自「${it.nickname}」"
                    share_desc = it.content
                    if (it.img_url != null && it.img_url.size > 0) {
                        share_image = it.img_url[0]
                    }
                    contentId = it.id
                    id=it.circle_id
                }
                .show()
        }
        var totalDy = 0
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

                totalDy += dy
                if (totalDy > 800) {
                    (activity as MainActivity).setShowTop(View.VISIBLE)
                }else{
                    (activity as MainActivity).setShowTop(View.GONE)
                }
            }
        })
    }

    override fun start() {
        if (isLogin()) {
            mPresenter.focusList(page)
            setError(false)
        } else {
            setError(true)
        }
    }
    fun pauseVideo(){
        if (Jzvd.CURRENT_JZVD != null) {
            Jzvd.goOnPlayOnPause()
            if (AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] != null) {
                AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] = AutoPlayUtils.homePlayerMap[AutoPlayUtils.index]!!
            }

        }
    }
    fun resumeVideo(){
        if (AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] != -1) {
            BasePostNodeDataUtils.isLooper = true
            mAdapter?.notifyDataSetChanged()
        }
    }
    override fun onResume() {
        super.onResume()

        if (mAdapter.data.size == 0) {
            page = 1
            start()
        }
        AutoPlayUtils.index = 1
        AutoPlayUtils.pageType = 1
        resumeVideo()

    }

    override fun onPause() {
        super.onPause()
        pauseVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        NetStateChangeReceiver.unRegisterReceiver(context!!)
        mVolumeChangeObserver?.unregisterVolumeReceiver()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMainLoginEvent(event: MainLoginEvent) {
        page = 1
        start()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setEmptyView(RefreshView.getNetworkView(activity, recycler_view, { start() }))
            } else {
                showToast(errorMsg)
            }
        } else {
            showToast(errorMsg)
        }
    }

    var shareUrl = ""
    override fun showFocusList(mBean: CircleAttentionBean) {
        if (mBean.data.data != null && mBean.data.data.size > 0) {
            val list = ArrayList<AttentionMutli>()
            if (mBean.data.current_page == 1) {
                shareUrl = mBean.data.share_url
                for (i in mBean.data.data) {
                    list.add(AttentionMutli(i))

                }
                dataList = mBean.data.data
                mAdapter.setNewInstance(list)
            } else {
                for (i in mBean.data.data) {
                    list.add(AttentionMutli(i))
                    dataList.add(i)
                }
                dataList.addAll(mBean.data.data)
                mAdapter.addData(list)
            }
            if (mBean.data.current_page >= mBean.data.last_page) {
                refreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                refreshLayout.resetNoMoreData()
                refreshLayout.setEnableLoadMore(true)
            }
        } else if (page == 1) {
            mPresenter.recommendedUserList(page)
            refreshLayout.finishLoadMoreWithNoMoreData()
        }
    }

    override fun showUserList(bean: CircleAttentionUserBean) {
        refreshLayout.setEnableLoadMore(false)
        if (bean.data != null && bean.data.data != null) {
            val list = ArrayList<AttentionMutli>()
            for (i in bean.data.data) {
                list.add(AttentionMutli(i))
            }
            mAdapter.setDataFriend(list)
        } else {
            mAdapter.setEmptyView(
                RefreshView.getEmptyView(
                    activity,
                    "",
                    recycler_view
                )
            )
        }
    }

    override fun showCircleList(bean: CircleMeBean) {

    }

    override fun showCircleRecommendedList(bean: CircleRecommendedBean) {

    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAttentionEvent(event: AttentionEvent) {
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].mUserBean != null && mAdapter.data[i].mUserBean.user_id == event.userId) {
                mAdapter.data[i].mUserBean.is_mutual = event.isMutual
                mAdapter.notifyItemChanged(i)

                var type = "你还没有关注的人喔～"
                for (i in mAdapter.data) {
                    if (i.mUserBean != null && i.mUserBean.is_mutual != -1) {
                        type = "下拉加载关注用户的动态喔~"
                    }
                }
                mAdapter.tvMutual?.text = type

                mAdapter.setAttention(event)
            } else if (mAdapter.data[i].mBean != null && mAdapter.data[i].mBean.user_id == event.userId) {
                mAdapter.data[i].mBean.is_mutual = event.isMutual
                mAdapter.notifyItemChanged(i)
            }
        }
    }

    override fun onDismiss(postid: String) {
        resumeVideo()
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].mBean != null && mAdapter.data[i].mBean.id == postid) {
                mAdapter.notifyItemChanged(i)
            }
        }
    }

    fun setError(boolean: Boolean) {
        if (boolean) {
            empty_view.visibility = View.VISIBLE
            empty_view.setOnClickListener {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
            }
        } else {
            empty_view.visibility = View.GONE
        }

    }

    override fun onNetDisconnected() {

    }

    override fun onNetConnected(networkType: NetworkType?) {
        var type = 2
        if (networkType == NetworkType.NETWORK_NO) {
            type = 0
        } else if (networkType == NetworkType.NETWORK_WIFI) {
            type = 1
        }
        var layoutManager=recycler_view.layoutManager as LinearLayoutManager?
        var positionInList=if (AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] != null)
            AutoPlayUtils.homePlayerMap[AutoPlayUtils.index]!! else 0
        var parentView: View? = layoutManager!!.findViewByPosition(positionInList) ?: return
        parentView!!.findViewById<MyJzVideoView>(R.id.jzVideoView)?.setType(type)

    }

    override fun onVolumeChange(volume: Int) {
        var layoutManager=recycler_view.layoutManager as LinearLayoutManager?
        var positionInList=if (AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] != null)
            AutoPlayUtils.homePlayerMap[AutoPlayUtils.index]!! else 0
        try {
            var parentView: View? = layoutManager!!.findViewByPosition(positionInList) ?: return
            parentView!!.findViewById<MyJzVideoView>(R.id.jzVideoView)?.setVolumeChange(volume)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    /**
     * 双击刷新
     */
    fun setDoubleRefresh() {
        refreshLayout.autoRefresh()
    }
    /**
     * 滑动到顶部
     */
    fun startTop() {
        RefreshView.smoothMoveToPosition(recycler_view, 0)
    }

}
