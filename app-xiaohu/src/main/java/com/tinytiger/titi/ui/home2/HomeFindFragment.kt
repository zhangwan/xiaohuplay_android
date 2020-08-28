package com.tinytiger.titi.ui.home2


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.banner.Banner
import com.ms.banner.BannerConfig
import com.ms.banner.listener.OnBannerClickListener
import com.tinytiger.common.adapter.CustomViewHolder
import com.tinytiger.common.basis.BasisFragment
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.net.data.AdBean
import com.tinytiger.common.net.data.ad.AdClassify
import com.tinytiger.common.net.data.ad.AdFindBean
import com.tinytiger.common.net.data.ad.DiscoverClassify
import com.tinytiger.common.net.data.home2.AmwayWallListBean
import com.tinytiger.common.net.data.home2.FindBeanMulti
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.umeng.BannerAgentUtils
import com.tinytiger.common.utils.umeng.HomeAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.home2.find.FindAdapter
import com.tinytiger.titi.adapter.home2.find.GameClassifyAdapter
import com.tinytiger.titi.ui.home2.vp.HomeFindPresenter
import com.tinytiger.titi.ui.main.MainActivity
import com.tinytiger.titi.utils.BannerStartUtils
import kotlinx.android.synthetic.main.activity_base_fr_recycler.*

/**
 *
 * @author zhw_luke
 * @date 2019/10/30 0030 下午 1:51
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页 发现
 */
class HomeFindFragment : BasisFragment<HomeFindPresenter>() {

    companion object {
        fun getInstance(): HomeFindFragment {
            val fragment = HomeFindFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_base_fr_recycler

    private val mPresenter by lazy { HomeFindPresenter() }
    private val mAdapter by lazy { FindAdapter(ArrayList()) }


    /**
     * 初始化 ViewI
     */
    override fun initView() {
        mPresenter.attachView(this)
        TAG = "HomeFindFragment"
        recycler_view.adapter = mAdapter
        mAdapter.addHeaderView(getRefreshHeader())

        refreshLayout.setOnRefreshListener { _ ->
            page = 1
            mPresenter.getDiscoverClassify(page)
        }

        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            mPresenter.getDiscoverClassify(page)
        }

        var totalDy = 0
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalDy += dy
                if (totalDy > 1000) {
                    (activity as MainActivity).setShowTop(View.VISIBLE)
                } else {
                    (activity as MainActivity).setShowTop(View.GONE)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (mAdapter.data.size == 0) {
//            refresh()
        }
    }

    override fun refresh() {
        mPresenter.loadAdList()
        mPresenter.getDiscoverClassify(page)
        mPresenter.amwayWallRecommend("index")
    }

    fun getAdList(itemInfo: AdFindBean) {
        if (itemInfo.data != null && itemInfo.data.banner.size > 0) {
            HomeAgentUtils.setHomeShow()
            //显示banner数据
            setAdList(itemInfo.data.banner)
            //二级分类数据
            if (itemInfo.data.classify != null && itemInfo.data.classify.size == 5) {
                itemInfo.data.classify.add(AdClassify())
                classifyAdapter!!.setNewInstance(itemInfo.data.classify)
            }
        } else {
            mAdapter.removeAllFooterView()
        }
    }

    private var banner: Banner? = null
    private var classifyAdapter: GameClassifyAdapter? = null

    /**
     * 头部区域
     */
    private fun getRefreshHeader(): View {
        val view = layoutInflater.inflate(
            R.layout.home_header_games, recycler_view.parent as ViewGroup, false
        )
        banner = view.findViewById(R.id.banner)
        val recyclerList = view.findViewById<RecyclerView>(R.id.recycler_list)

        //显示banner
        banner?.setBannerStyle(BannerConfig.CENTER)
        val lp = banner!!.layoutParams
        lp.height = ((ScreenUtil.getScreenWidth() - Dp2PxUtils.dip2px(activity, 40)) * 0.42).toInt()
        banner?.layoutParams = lp

        //显示二级分类
        classifyAdapter = GameClassifyAdapter(arrayListOf())
        recyclerList.layoutManager = GridLayoutManager(context, 3)
        recyclerList.adapter = classifyAdapter
        return view
    }

    private fun setAdList(mAdBean: ArrayList<AdBean>) {
        BannerAgentUtils.setBannerFindShow(mAdBean)
        banner!!.setAutoPlay(true)
            .setPages(mAdBean, CustomViewHolder())
            .setOnBannerClickListener(OnBannerClickListener { _, position ->
                BannerStartUtils.setStartIntent(mAdBean[position], 3)
                mPresenter.clickAdRecord(mAdBean[position].id)
            })
            .start()
    }

    private var beanAmway: AmwayWallListBean? = null
    fun loadAmwayWall(bean: AmwayWallListBean) {
        if (bean.data != null && bean.data.size > 0) {
            beanAmway = bean
            if (mAdapter.data.size > 2 && mAdapter.data[2].itemType != 2) {
                mAdapter.addData(2, FindBeanMulti(bean))
            }
        }
    }

    fun loadDiscoverClassify(bean: DiscoverClassify) {
        mAdapter.removeAllFooterView()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()

        if (bean.data.current_page == 1) {
            if (bean.data == null || bean.data.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getEmptyView(context, "", recycler_view))
            } else {
                mAdapter.data.clear()
                //itemType=3 分类列表
                for (i in bean.data.data) {
                    if (i.child != null && i.child.size > 0) {
                        val item = FindBeanMulti(3, i)
                        item.defaulttxt = i.cate_name
                        item.id = i.game_category_id
                        mAdapter.data.add(item)
                    }
                }
                //itemType=2 安利墙
                if (beanAmway != null) {
                    if (mAdapter.data.size > 2) {
                        mAdapter.data.add(2, FindBeanMulti(beanAmway!!))
                    }
                }
            }
        } else {
            //itemType=3 分类列表
            for (i in bean.data.data) {
                val item = FindBeanMulti(3, i)
                item.defaulttxt = i.cate_name
                item.id = i.game_category_id
                mAdapter.data.add(item)
            }
        }
        mAdapter.notifyDataSetChanged()

        if (bean.data.current_page >= bean.data.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getNewFooterView(context, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }

    }

    fun showErrorMsg(errorMsg: String, errorCode: String) {
        if (errorCode == HttpConstants.Code.TIME_OUT) {
            if (mAdapter.data.size == 0) {
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(activity, recycler_view, { refresh() })
                )
            } else {
                showToast(errorMsg)
            }
        } else {
            showToast(errorMsg)
        }
    }

    fun finishLoad() {
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    /**
     * 双击刷新
     */
    fun setDoubleRefresh() {
        startTop()
        refreshLayout.autoRefresh()
    }

    fun startTop() {
        RefreshView.smoothMoveToPosition(recycler_view, 0)
    }

}
