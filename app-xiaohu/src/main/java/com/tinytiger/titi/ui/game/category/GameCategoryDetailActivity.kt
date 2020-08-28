package com.tinytiger.titi.ui.game.category

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.data.gametools.category.GameCategoryBannerBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryDetailListBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryListBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.umeng.BannerAgentUtils
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.sort.GameCategoryBannerAdapter
import com.tinytiger.titi.mvp.contract.gametools.GameCategoryContract
import com.tinytiger.titi.mvp.presenter.gametools.GameCategoryPresenter
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.xwdz.download.core.QuietDownloader
import kotlinx.android.synthetic.main.game_activity_sort_detail.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 游戏分类详情 Activity
*/
class GameCategoryDetailActivity : BaseBackActivity(), GameCategoryContract.View {

    private var cate_name = ""

    private var cate_id = 1

    companion object {

        private const val EXTRA_CATE_ID = "extra_cate_id"
        private const val EXTRA_CATE_NAME = "extra_cate_name"

        fun actionStart(context: Context, cate_name: String, cate_id: Int) {
            val intent = Intent(context, GameCategoryDetailActivity::class.java)
            intent.putExtra(EXTRA_CATE_NAME, cate_name)
            intent.putExtra(EXTRA_CATE_ID, cate_id)
            context.startActivity(intent)
        }

    }


    private val mFragmentList = ArrayList<Fragment>()

    private val titles = ArrayList<String>()


    private val mPresenter by lazy { GameCategoryPresenter() }


    private val mAdapter by lazy { GameCategoryBannerAdapter(ArrayList()) }


    override fun layoutId(): Int = R.layout.game_activity_sort_detail


    override fun initData() {

        mPresenter.attachView(this)
        if (intent.hasExtra(EXTRA_CATE_NAME)) {
            cate_name = intent.getStringExtra(EXTRA_CATE_NAME)
        }
        cate_id = intent.getIntExtra(EXTRA_CATE_ID, 0)
        setIsOnlyTrackingLeftEdge(true)
    }

    override fun initView() {

        title_view.centerTxt = if (cate_name.isEmpty()) "游戏百科" else cate_name
        title_view.setBackOnClick {
            finish()
        }



        initRecyclerView()
        initMagicIndicator()

        start()


    }

    override fun onResume() {
        super.onResume()
        QuietDownloader.recoverAll()
    }

    private fun initRecyclerView() {

        val widthPixels= ScreenUtil.getScreenWidth()
        mAdapter.height=((widthPixels- Dp2PxUtils.dip2px(this,40))*0.42).toInt()
        mAdapter.width=widthPixels- Dp2PxUtils.dip2px(this,40)
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.adapter = mAdapter

        mAdapter.setOnItemClickListener { _, _, position ->
            BannerAgentUtils.setBannerClassClick(mAdapter.data[position].game_id,cate_id)
            GameDetailActivity.actionStart(this, mAdapter.data[position].game_id,4)
        }
    }


    private fun initMagicIndicator() {
        mFragmentList.clear()
        titles.clear()

        val strs = arrayOf("热门游戏", "最新上架", "评分最高")

        for (index in strs.indices) {
            titles.add(strs[index])
            //1=热门 2=最新上架 3=评分最高 默认1
            mFragmentList.add(GameCategoryDetailFragment.getInstance(cate_id, index + 1))
        }

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true

        val mAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                //文字
                val titleView = ScaleTransitionPagerTitleView(context)
                titleView.text = (titles[index])
                titleView.textSize = 16f
                titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                titleView.normalColor = ContextCompat.getColor(context, R.color.gray99)
                titleView.selectedColor = ContextCompat.getColor(context, R.color.gray28)
                titleView.minScale = 0.8.toFloat()
                titleView.setOnClickListener {
                    mViewPager.currentItem = index
                }

                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return if (titles.size > 1) {
                    Indicator.mLinePagerIndicator(context, R.color.color_ffcc03, 10)
                } else {
                    null
                }
            }
        }


        commonNavigator.adapter = mAdapter
        magic_indicator.navigator = commonNavigator

        mViewPager.adapter = TabAdapter(supportFragmentManager, mFragmentList)
        ViewPagerHelper.bind(magic_indicator, mViewPager)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
             //   setSwipeBackEnable(position == 0)
            }
        })
    }


    override fun start() {
        mPresenter.getGameCategoryBanner(cate_id)
    }

    override fun showGameCategory(data: GameCategoryListBean) {

    }


    override fun showGameCategoryDetailList(data: GameCategoryDetailListBean.DataBean) {

    }

    override fun showGameCategoryBanner(data: ArrayList<GameCategoryBannerBean.DataBean>) {
        mAdapter.setNewInstance(data)

        var banner_id=""
        for (i in data){
            banner_id="$banner_id,${i.game_id}"
        }
        if (banner_id.length>1){
            banner_id=banner_id.substring(1,banner_id.length)
        }
        BannerAgentUtils.setBannerClassShow(banner_id,cate_id)

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


}