package com.tinytiger.titi.ui.home2


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tinytiger.common.basis.BasisFragment
import com.tinytiger.common.http.response.PageVo
import com.tinytiger.common.net.data.AdBean
import com.tinytiger.common.net.data.home2.*
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.umeng.BannerAgentUtils
import com.tinytiger.common.utils.umeng.HomeAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.home2.push.HomeAdapter
import com.tinytiger.titi.adapter.home2.push.HomeGamePushAdapter
import com.tinytiger.titi.adapter.home2.push.ImageBannerAdapter
import com.tinytiger.titi.ui.game.GameLibraryActivity
import com.tinytiger.titi.ui.game.category.GameCategoryListActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.home2.vp.HomePushPresenter
import com.tinytiger.titi.ui.main.MainActivity
import com.tinytiger.titi.ui.yungame.YunGameActivity
import com.tinytiger.titi.utils.BannerStartUtils
import com.to.aboomy.pager2banner.Banner
import com.to.aboomy.pager2banner.IndicatorView
import com.to.aboomy.pager2banner.ScaleInTransformer
import com.xwdz.download.core.DownloadEntry
import com.xwdz.download.core.QuietDownloader
import com.xwdz.download.notify.DataUpdatedWatcher
import kotlinx.android.synthetic.main.home_fagment_recommend.*
import net.lucode.hackware.magicindicator.buildins.UIUtil

/**
 *
 * @author zhw_luke
 * @date 2019/10/30 0030 下午 1:51
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页 推荐页
 */
class HomePushFragment : BasisFragment<HomePushPresenter>() {

    companion object {
        fun getInstance(): HomePushFragment {
            val fragment = HomePushFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.home_fagment_recommend

    private val mAdapter by lazy { HomeAdapter(ArrayList()) }
    val mGameAdapter by lazy { HomeGamePushAdapter() }

    var imageAdapter: ImageBannerAdapter? = null
    var mGameRecyclerView: RecyclerView? = null
    var bannerList: List<BannerAdBean>? = null

    /**
     * 初始化 ViewI
     */
    override fun initView() {
        TAG = "HomePushFragment"

        refreshLayout.setEnableRefresh(true)
        refreshLayout.setEnableLoadMore(false)
        refreshLayout.setEnableOverScrollDrag(true)
        recycler_view.adapter = mAdapter

        mAdapter.addHeaderView(getRefreshHeader())

        var totalDy = 0
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalDy += dy
                if (totalDy > 800) {
                    (activity as MainActivity).setShowTop(View.VISIBLE)
                } else {
                    (activity as MainActivity).setShowTop(View.GONE)
                }
            }
        })
        initListener()
    }

    fun start() {
        basePresenter?.getRecommendAdList()
        basePresenter?.getRecommendBannerList()
        basePresenter?.getAmwayWallRecommend()
        page = 1
        basePresenter?.getHomeRecommend(page)
    }

    fun initListener() {
        refreshLayout.setOnRefreshListener {
            basePresenter?.getRecommendAdList()
            basePresenter?.getRecommendBannerList()
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            basePresenter?.getHomeRecommend(page)
        }

        //item点击banner
        mAdapter.bannerItemClickListener = {
            if (it.content != null) {
                BannerStartUtils.setStartIntent(it.content!!, 0)
            }
        }
        mAdapter.gameItemClickListener = {
            it.game_info?.game_id?.let { it1 ->
                GameDetailActivity.actionStart(context!!, it1, 0)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mAdapter.data.size == 0) {
            start()
        }

        QuietDownloader.addObserver(watcher)
        mAdapter.setResolveInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        QuietDownloader.removeObserver(watcher)
    }

    fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(activity, recycler_view, { start() }))
            } else {
                showToast(errorMsg)
            }
        } else {
            showToast(errorMsg)
        }
    }

    fun showAdvertisementList(bannerList: ArrayList<BannerAdBean>?) {
        if (bannerList == null) {
            return
        }

        var mBannerList: List<BannerAdBean> = if (bannerList.size > 5) {
            bannerList.subList(0, 5)
        } else {
            bannerList
        }

        this.bannerList = mBannerList
        HomeAgentUtils.setHomeShow()
        refreshLayout.finishRefresh()
        initBanner()
    }

    fun showGameBanner(gameBannerList: ArrayList<GameBannerBean>) {
        mGameRecyclerView?.visibility = View.VISIBLE
        mGameAdapter.setNewInstance(gameBannerList)
        refreshLayout.finishRefresh()
    }

    fun showHideBanner() {
        mGameRecyclerView?.visibility = View.GONE
    }

    fun showFailure() {
        refreshLayout.finishRefresh()
    }


    private var beanAmway: AmwayWallListBean? = null

    fun loadAmwayWall(bean: AmwayWallListBean) {
        if (bean.data != null && bean.data.size > 0) {
            beanAmway = bean
            if (mAdapter.data.size > 1 && mAdapter.data[2].itemType != 3) {
                mAdapter.addData(2, PushBeanMultiRevise(beanAmway))
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    fun LoadPushList(list: PageVo<HomeRecommendBean>?) {
        val homeList = list?.data
        for (i in 0 until homeList!!.size) {
            if ("1" == homeList[i].recommend_type || "2" == homeList[i].recommend_type) {
                mAdapter.data.add(PushBeanMultiRevise(1, homeList[i]))
            } else if ("3" == homeList[i].recommend_type) {
                mAdapter.data.add(PushBeanMultiRevise(2, homeList[i]))
            }
        }

        if (mAdapter.data.size >= 1 && mAdapter.data[2].itemType != 3) {
            if (beanAmway != null) {
                mAdapter.addData(2, PushBeanMultiRevise(beanAmway))
            }
        }

        refreshLayout.finishLoadMore()
        if (list.current_page!! >= list.last_page!!) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.removeAllFooterView()
            mAdapter.addFooterView(RefreshView.getNewFooterView(context, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }

        mAdapter.notifyDataSetChanged()

    }

    fun loadAppointment(gameId: String) {
        var k=0
        if (this.bannerList != null) {
            for (i in bannerList!!.indices){
                if(bannerList!![i].gameInfo?.id==gameId){
                    k=i
                    this.bannerList!![k]?.gameInfo?.appointment_type = "2"
                }
            }
            imageAdapter?.notifyDataSetChanged()
        }

    }


    private var banner: Banner? = null

    /**
     * @return
     */
    private fun getRefreshHeader(): View {
        val view =
            layoutInflater.inflate(R.layout.home_header_banner, recycler_view.parent as ViewGroup,
                false)
        banner = view.findViewById(R.id.banner2)
        mGameRecyclerView = view.findViewById(R.id.recyclerView)

        initGameAdapter()
        view?.findViewById<View>(R.id.tvTitle1)!!.setOnClickListener {
            GameCategoryListActivity.actionStart(context!!)
        }
        view.findViewById<View>(R.id.tvTitle2)!!
            .setOnClickListener { GameLibraryActivity.actionStart(context!!) }

        return view
    }

    fun initBanner() {
        val indicatorView = IndicatorView(context).setIndicatorSpacing(2f)
            .setIndicatorRatio(1f)
            .setIndicatorRadius(2f)
            .setIndicatorSelectedRatio(3f)
            .setIndicatorSelectedRadius(2f)
            .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_BIG_CIRCLE)
            .setIndicatorColor(ContextCompat.getColor(context!!, R.color.color_d8d8d8))
            .setIndicatorSelectorColor(ContextCompat.getColor(context!!, R.color.color_ffcc03))


        banner!!.setAutoPlay(false)
            .setIndicator(indicatorView)
            .setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
            .setPagerScrollDuration(800)
            .setPageMargin(UIUtil.dip2px(context, 10.0), UIUtil.dip2px(context, 8.0))
            .addPageTransformer(ScaleInTransformer(0.9f))
            .setOuterPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                }
            })
        imageAdapter = ImageBannerAdapter()
        imageAdapter?.setList(this.bannerList?.toMutableList())
        banner?.adapter = imageAdapter

        imageAdapter?.bannerClickListener = {
            if (it.gameInfo == null) {
                BannerStartUtils.setStartIntent(it, 1)
            } else {
                it?.gameInfo?.id?.let { it1 -> GameDetailActivity.actionStart(context!!, it1, 1) }
            }
        }
        imageAdapter?.bespokeClickListener = { it, type, position ->
            if (bannerList != null) {
                if (it.gameInfo != null) {
                    if (getString(R.string.home_second_play) == type) {
                        YunGameActivity.actionStart(context!!, it.gameInfo?.id!!,
                            it.gameInfo?.package_name_android!!)
                    } else {
                        basePresenter?.addAppointment(it.gameInfo?.id!!)
                    }
                }
            }
        }

    }

    fun initGameAdapter() {
        mGameRecyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mGameRecyclerView?.adapter = mGameAdapter
    }


    /**
     * 设置banner
     */
    private fun setAdList(mAdBean: ArrayList<AdBean>) {
        BannerAgentUtils.setBannerHomeShow(mAdBean)
        //        imageAdapter.setNewInstance(mAdBean)
    }


    private val watcher = object : DataUpdatedWatcher() {
        override fun notifyUpdate(data: DownloadEntry) {
            mAdapter.notifyUpdate(data)
        }
    }


    /**
     * 滑动到列表第一条
     */
    fun startTop() {
        RefreshView.smoothMoveToPosition(recycler_view, 0)
    }

}
