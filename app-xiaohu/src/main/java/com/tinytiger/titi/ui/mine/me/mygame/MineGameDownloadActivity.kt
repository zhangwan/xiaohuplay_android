package com.tinytiger.titi.ui.mine.me.mygame

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alibaba.fastjson.JSON
import com.orhanobut.logger.Logger
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.net.data.center.MineGameListBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.center.MineGameContract
import com.tinytiger.titi.mvp.presenter.center.MineGamesPresenter
import com.tinytiger.titi.sql.SQLiteDB
import com.tinytiger.titi.utils.CheckUtils
import com.xwdz.download.core.DownloadEntry
import com.xwdz.download.core.QuietDownloader
import kotlinx.android.synthetic.main.mine_activity_base_viewpager.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import java.util.*
import kotlin.collections.ArrayList


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 个人中心 - 我的游戏 Activity
*/
class MineGameDownloadActivity : BaseBackActivity(), MineGameContract.View {


    private val mPresenter by lazy { MineGamesPresenter() }


    companion object {
        fun actionStart(context: Context) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }

            val intent = Intent(context, MineGameDownloadActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.mine_activity_base_viewpager

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        tvTitle.centerTxt = "游戏管理"
        tvTitle.setBackOnClick {
            finish()
        }

        initMagicIndicator()
    }


    private val mFragmentList = ArrayList<Fragment>()
    private var mAdapter: CommonNavigatorAdapter? = null
    private var titles = ArrayList<String>()

    private var channel = false
    private fun initMagicIndicator() {

        if (!MyUserData.isEmptyToken()) {
            mFragmentList.add(MineGameFragment.getInstance())
            titles.add("收藏")
        }
        channel = SpUtils.getBoolean(R.string.download_apk, false)
        if (channel) {
            mFragmentList.add(MineGameDownloadFragment.getInstance(0))
            mFragmentList.add(MineGameDownloadFragment.getInstance(1))
            titles.add("下载")
            titles.add("已装")
        } else {
            magic_indicator.visibility = View.GONE
        }

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true

        mAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                //文字
                val titleView = ScaleTransitionPagerTitleView(context)
                titleView.textSize = 16f
                titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                titleView.normalColor = ContextCompat.getColor(context, R.color.gray99)
                titleView.selectedColor = ContextCompat.getColor(context, R.color.gray33)
                titleView.minScale = 0.8.toFloat()

                val title = if (index == mViewPager.currentItem) {
                    var name=""
                    if(titles[index]=="收藏"){
                        name="收藏 $totalSize"
                    }else if (titles[index]=="下载"){
                        name="下载 ${mApkList.size}"
                    }else if (titles[index]=="已装"){
                        name="已装 ${mApkOverList.size}"
                    }
                    name
                } else {
                    "${titles[index]}"
                }
                val sb = SpannableString(title)
                sb.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_ffcc03)), 2,
                    title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                titleView.text = sb

                titleView.setOnClickListener {
                    mViewPager.currentItem = index
                }

                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return Indicator.mLinePagerIndicator(context, R.color.color_ffcc03, 10)
            }
        }

        commonNavigator.adapter = mAdapter
        magic_indicator.navigator = commonNavigator

        mViewPager.adapter = TabAdapter(supportFragmentManager, mFragmentList)
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mAdapter?.notifyDataSetChanged()
                setSwipeBackEnable(position == 0)

            }
        })
        ViewPagerHelper.bind(magic_indicator, mViewPager)
    }

    override fun start() {
    }


    override fun showFollowGameList(bean: MineGameListBean.Data) {

    }

    override fun showGameFollow(game_id: String) {

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        dismissLoading()
    }

    var totalSize=0
    fun showNumber(total: Int, position: Int) {
        if (position ==0) {
            if (total == -1) {
                totalSize -= 1
            } else {
                totalSize = total
            }
        }

        mAdapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        if (channel) {
            getDownloadData2()
            QuietDownloader.recoverAll()
        }
    }

    //下载完成,并安装
    val mApkOverList = ArrayList<MineGameBean>()
    //未下载安装完成
    val mApkList = ArrayList<MineGameBean>()

    /**
     * 是否第一次显示
     */
    private var showType = true

    /**
     * 获取所有本地数据包
     */
    fun getDownloadData2() {
        mApkList.clear()
        mApkOverList.clear()
        //查询所有下载apk包信息（自己的表）
        val list = SQLiteDB.getInstance(this).queryAllApk()

        //查询下载任务的数据（第三方包）
        val mDownloadList = QuietDownloader.queryAll()
        val mApkLists = ArrayList<MineGameBean>()
        for (download in mDownloadList) {
            for (apk in list) {
                if (download.url == apk.download_url) {
                    mApkLists.add(apk)
                }
            }
        }
        //已安装未更新
        val mApkOverList1 = ArrayList<MineGameBean>()
        //已安装
        val mApkOverList2 = ArrayList<MineGameBean>()
        //所有有用的数据
        for(i in mApkLists){
            val pack = CheckUtils.getInstalledPackage(this, i.package_name_android)
            if (pack!=null){
                if (i.version != pack.versionName) {
                    mApkOverList1.add(i)
                } else {
                    mApkOverList2.add(i)
                }
            }else{
                mApkList.add(i)
            }
        }


        sortGroups(mApkList)
        sortGroups(mApkOverList1)
        sortGroups(mApkOverList2)
        mApkOverList.addAll(mApkOverList1)
        mApkOverList.addAll(mApkOverList2)

        mAdapter?.notifyDataSetChanged()
        if (mFragmentList.size == 3){
            (mFragmentList[1] as MineGameDownloadFragment).setNewData()
            (mFragmentList[2] as MineGameDownloadFragment).setNewData()

            if (showType && mApkList.size > 0) {
                mViewPager.currentItem = 1
            }
        }else  if (mFragmentList.size == 2){
            (mFragmentList[0] as MineGameDownloadFragment).setNewData()
            (mFragmentList[1] as MineGameDownloadFragment).setNewData()
        }

        showType = false
    }


    /**
     * 排序
     *
     * @param groups
     */
    private fun sortGroups(groups: java.util.ArrayList<MineGameBean>) {
        Collections.sort(groups,
            Comparator<MineGameBean> { lhs, rhs -> (rhs.time-lhs.time  ).toInt() })
    }
}