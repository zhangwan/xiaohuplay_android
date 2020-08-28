package com.tinytiger.titi.ui.circle


import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import com.alibaba.fastjson.JSON
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.MainLoginEvent
import com.tinytiger.common.net.data.circle.CircleBean
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.home.CircleAttentionBean
import com.tinytiger.common.net.data.circle.home.CircleAttentionUserBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.utils.VolumeChangeObserver
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.home.CircleMeAdapter
import com.tinytiger.titi.adapter.circle.home.CircleRecommendAdapter
import com.tinytiger.titi.adapter.post.BasePostNodeDataUtils
import com.tinytiger.titi.adapter.post.CommonPostNodeAdapter
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.event.PostEvent
import com.tinytiger.titi.mvp.contract.circle.CirclesHomeContract
import com.tinytiger.titi.mvp.presenter.circle.CircleshomePresenter
import com.tinytiger.titi.ui.circle.detail.CircleFragment
import com.tinytiger.titi.ui.main.MainActivity
import com.tinytiger.titi.utils.AutoPlayUtils
import com.tinytiger.titi.utils.net.NetStateChangeObserver
import com.tinytiger.titi.utils.net.NetStateChangeReceiver
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.widget.dialog.FullPostDialog
import com.tinytiger.titi.widget.video.MyJzVideoView
import kotlinx.android.synthetic.main.home_fagment_recommend.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * @author zhw_luke
 * @date 2020/4/28 0028 上午 10:53
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 圈子首页-推薦
 */
class CirclePushFragment : BaseFragment(), CirclesHomeContract.View, FullPostDialog.OnFullSheetListener,
    NetStateChangeObserver, VolumeChangeObserver.OnVolumeChangeListener {


    companion object {
        var circlePushFragment: CirclePushFragment? = null
        fun getInstance(): CirclePushFragment {
            val fragment = CirclePushFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.base_recycler

    private val mPresenter by lazy { CircleshomePresenter() }

    private val mAdapter by lazy { CommonPostNodeAdapter(activity!!, ArrayList()) }

    private val mAdapter1 by lazy { CircleMeAdapter() }
    private val mAdapter2 by lazy { CircleRecommendAdapter() }
    private var dataList = arrayListOf<PostBean>()

    private var looperFlag = 0 //0,无自动播放，1.自动播放上一个，2自动播放下一个
    private var mVolumeChangeObserver: VolumeChangeObserver? = null

    /**
     * 初始化 ViewI
     */
    override fun initView() {
        TAG = "CirclePushFragment"
        circlePushFragment = this
        mPresenter.attachView(this)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        NetStateChangeReceiver.registerReceiver(getContext())
        NetStateChangeReceiver.registerObserver(this)
        mVolumeChangeObserver = VolumeChangeObserver(getContext())
        mVolumeChangeObserver?.setOnVolumeChangeListener(this)
        mVolumeChangeObserver?.registerVolumeReceiver()
        mAdapter.pageIndex = ConstantsUtils.NOTE.PAGE_DEFAULT
        mAdapter.isVideoType = true
        recycler_view.adapter = mAdapter

        refreshLayout.setOnRefreshListener {
            BasePostNodeDataUtils.isLooper = true
            AutoPlayUtils.index = 2
            AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] = -1
            page = 1
            start()
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            start()
        }
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
                listener = this@CirclePushFragment
            }.show(childFragmentManager)
        }
        mAdapter.likeListener = {
            mPresenter.likePost(it.id)
        }
        mAdapter.mHeaderView = getRefreshHeader()
        showLoading()
        start()
        CircleAgentUtils.setCircleHome()
        initListener()
    }


    private fun initListener() {
        var totalDy = 0
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //滑动停止后，
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    var count = 0
                    if (mAdapter2.itemCount > 0 || mAdapter1.itemCount > 0) {
                        count += 1
                    }
                    AutoPlayUtils.onScrollPlayVideo(recycler_view, dataList, looperFlag, count)
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


    var shareUrl = ""
    override fun start() {
        if (page == 1) {
            meStart()
        }

        if (mAdapter2.data.size == 0) {
            mPresenter.recommendedCircleList()
        }

        mPresenter.recommendedPost(page)
    }

    fun meStart() {
        if (isLogin()) {
            mPresenter.myCircleList(mePage)
        } else {
            rlMe?.visibility = View.GONE
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PostEvent) {
        if (event.data != null) {
            if (event.data.circleType == false) {
                event.data.status = "0"
                event.data.is_mutual = -2
                dataList.add(0, event.data)
                mAdapter.data = dataList
                mAdapter?.notifyDataSetChanged()
            }

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMainLoginEvent(event: MainLoginEvent) {
        page = 1
        mePage = 1
        if (isLogin()) {
            mPresenter.myCircleList(1)
        } else {
            rlMe?.visibility = View.GONE
        }

        mPresenter.recommendedPost(page)
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                refreshLayout.setEnableLoadMore(false)
                mAdapter.mEmptyView = (RefreshView.getNetworkView(activity, recycler_view, { start() }))
                mAdapter?.notifyDataSetChanged()
            } else {
                showToast(errorMsg)
            }
        } else {
            showToast(errorMsg)
        }
    }


    override fun showFocusList(bean: CircleAttentionBean) {
        shareUrl = bean.data.share_url
        if (bean.data != null && bean.data.data != null) {
            if (bean.data.current_page == 1) {
                dataList = bean.data.data
            } else {
                dataList.addAll(bean.data.data)
            }
            if (bean.data.current_page >= bean.data.last_page) {
                refreshLayout.finishLoadMoreWithNoMoreData()
            }
            //            else {
            //                refreshLayout.resetNoMoreData()
            //            }
            mAdapter?.data = dataList
            mAdapter?.notifyDataSetChanged()
        } else {
            mAdapter.mEmptyView = RefreshView.getEmptyView(activity, "", recycler_view)
        }
    }

    override fun showUserList(bean: CircleAttentionUserBean) {

    }

    override fun showCircleList(bean: CircleMeBean) {
        if (bean.data != null && bean.data.data != null && bean.data.data.size > 0) {
            rlMe?.visibility = View.VISIBLE
            if (bean.data.total > 4) {
                tvMore?.visibility = View.VISIBLE
            }
            if (bean.data.current_page == 1) {
                mAdapter1.setNewInstance(bean.data.data)
            } else {
                mAdapter1.addData(bean.data.data)
            }
            LoadMore = bean.data.current_page < bean.data.last_page
            tvName?.text = "我加入的圈子(${bean.data.total})"
        } else {
            rlMe?.visibility = View.GONE
        }
    }

    override fun showCircleRecommendedList(bean: CircleRecommendedBean) {
        if (bean.data != null && bean.data.size > 0) {
            if (bean.data.size > 5) {
                mAdapter2.setNewInstance(bean.data.subList(0, 5))
            } else {
                mAdapter2.setNewInstance(bean.data)
            }
        } else {
            rlMe2?.visibility = View.GONE
        }
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }


    var rlMe: View? = null
    var rlMe2: View? = null
    var tvName: TextView? = null
    var tvMore: TextView? = null
    var mePage = 1
    var LoadMore = false

    /**
     * @return
     */
    private fun getRefreshHeader(): View {
        val view = layoutInflater.inflate(R.layout.circle_header_push, recycler_view.parent as ViewGroup, false)
        rlMe = view.findViewById(R.id.rlMe)
        tvName = view.findViewById(R.id.tvName)
        tvMore = view.findViewById(R.id.tvMore)
        val recycler_list = view.findViewById<RecyclerView>(R.id.recycler_list)
        recycler_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_list.adapter = mAdapter1
        recycler_list?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager
                    if (LoadMore && manager.findLastCompletelyVisibleItemPosition() == (manager.itemCount - 1)) {
                        mePage++
                        meStart()
                    }
                }
            }
        })

        rlMe2 = view.findViewById(R.id.rlMe2)
        val recycler_list1 = view.findViewById<RecyclerView>(R.id.recycler_list1)
        recycler_list1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_list1.adapter = mAdapter2

        tvMore?.setOnClickListener { CirclesMeActivity.actionStart(activity!!) }
        view.findViewById<View>(R.id.tvMore1).setOnClickListener { CirclesListActivity.actionStart(activity!!) }
        return view
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAttentionEvent(event: AttentionEvent) {
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].user_id == event.userId) {
                mAdapter.data[i].is_mutual = event.isMutual
                mAdapter.notifyItemChanged(i + 1)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCircleEvent(event: CircleBean) {
        if (event.is_join == 1) {
            Logger.d(JSON.toJSON(event))

            mAdapter1.addData(0, event)
        } else {
            for (i in 0..mAdapter.data.size - 1) {
                if (mAdapter1.data[i].id == event.id) {
                    mAdapter1.remove(i)
                    break
                }
            }
        }

        if (mAdapter1.data.size > 0) {
            rlMe?.visibility = View.VISIBLE
            if (mAdapter1.data.size > 4) {
                tvMore?.visibility = View.VISIBLE
            } else {
                tvMore?.visibility = View.GONE
            }
            tvName?.text = "我加入的圈子(${mAdapter1.data.size})"
        } else {
            rlMe?.visibility = View.GONE
        }
    }

    fun animateFinish() {
        mAdapter.notifyDataSetChanged()
    }

    override fun onDismiss(postid: String) {
        resumeVideo()
        for (i in 0..(mAdapter.data.size - 1)) {
            if (mAdapter.data[i].id == postid) {
                mAdapter.notifyItemChanged(i + 1)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resumeVideo()
    }

    override fun onPause() {
        super.onPause()
        pauseVideo()
    }

    fun resumeVideo() {
        AutoPlayUtils.pageType = 2
        AutoPlayUtils.index = 2
        if (AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] != -1) {
            BasePostNodeDataUtils.isLooper = true
            mAdapter?.notifyDataSetChanged()
        }
    }

    fun pauseVideo() {
        Jzvd.goOnPlayOnPause()
        if (AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] != null) {
            AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] = AutoPlayUtils.homePlayerMap[AutoPlayUtils.index]!!
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        NetStateChangeReceiver.registerReceiver(getContext())
        NetStateChangeReceiver.registerObserver(this)
        mVolumeChangeObserver = VolumeChangeObserver(getContext())
        mVolumeChangeObserver?.setOnVolumeChangeListener(this)
        mVolumeChangeObserver?.registerVolumeReceiver()
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
        var layoutManager = recycler_view.layoutManager as LinearLayoutManager?
        var positionInList =
            if (AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] != null) AutoPlayUtils.homePlayerMap[AutoPlayUtils.index]!! else 0
        var parentView: View? = layoutManager!!.findViewByPosition(positionInList) ?: return
        parentView!!.findViewById<MyJzVideoView>(R.id.jzVideoView)?.setType(type)

    }

    override fun onVolumeChange(volume: Int) {
        var layoutManager = recycler_view.layoutManager as LinearLayoutManager?
        var positionInList =
            if (AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] != null) AutoPlayUtils.homePlayerMap[AutoPlayUtils.index]!! else 0
        try {
            var parentView: View? = layoutManager!!.findViewByPosition(positionInList) ?: return
            parentView!!.findViewById<MyJzVideoView>(R.id.jzVideoView)?.setVolumeChange(volume)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 双击刷新
     */
    fun setDoubleRefresh() {
        startTop()
        refreshLayout.autoRefresh()
    }
    /**
     * 滑动到顶部
     */
    fun startTop() {
        RefreshView.smoothMoveToPosition(recycler_view, 0)
    }

}
