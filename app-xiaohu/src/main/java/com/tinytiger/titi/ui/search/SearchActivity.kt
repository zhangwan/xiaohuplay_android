package com.tinytiger.titi.ui.search

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alibaba.fastjson.JSON
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.SearchEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.search.*
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.ViewUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.sort.GameSearchHistoryAdapter
import com.tinytiger.titi.adapter.search.SearchHistoryAdapter
import com.tinytiger.titi.adapter.search.SearchKeywordAdapter
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.mvp.contract.SearchContract
import com.tinytiger.titi.mvp.presenter.SearchPresenter
import com.tinytiger.titi.ui.props.PropsActivity
import com.tinytiger.titi.utils.CheckUtils
import kotlinx.android.synthetic.main.search_activity.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc app搜索页
 */
class SearchActivity : BaseBackActivity(), SearchContract.View {

    override fun getHotList(mBean: HotKeyBean) {
        if (mBean.data == null) {
            return
        }

        val lits = ArrayList<String>()
        for (i in mBean.data) {
            lits.add(i.keyword)
        }

        mAdapterKey.type = false
        mAdapterKey.setNewInstance(lits)
    }

    override fun getKeyword(mBean: SearchKeywordBean) {
        rv_keyword.visibility = View.VISIBLE
        rlHistory.visibility = View.GONE
        llSearchData.visibility = View.GONE
        if (mBean.data != null) {
            mKeywordAdapter.keyword = et_send.text.toString().trim()
            mKeywordAdapter.setNewInstance(mBean.data)
        } else {
            mKeywordAdapter.setNewInstance(mutableListOf())
//            startList(et_send.text.toString().trim(), false)
        }
    }

    override fun getAllSearch(mBean: SearchAllBean) {
        mFragmentAll?.getData(keyWords, mBean)
    }

    override fun getGameSearch(mBean: SearchGameBean) {
        mFragmentGame?.getData(keyWords, mBean)
    }

    override fun getEssaySearch(mBean: SearchNewsBean) {
        mFragmentArticle?.getData(keyWords, mBean)
    }

    override fun getUserSearch(mBean: SearchUserBean) {
        mFragmentUser?.getData(keyWords, mBean)
    }

    override fun getPostSearch(mBean: PostInfoBean) {
        mFragmentPost?.getData(keyWords, mBean)
    }

    override fun getCircleSearch(mBean: CircleMeBean) {
        mFragmentCircle?.getData(keyWords, mBean)
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        et_send.clearFocus()
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mErrorView == null) {
                mErrorView = getErrorLayout()
            }
            fl_content.addView(mErrorView)

        } else if (errorCode == 20002) {
            TextDialog.create(supportFragmentManager).apply {
                cancelText = "一会再说"
                confirmText = "去瞧瞧"
            }
                .setMessage("商城权限开启成功!")
                .setViewListener(object : TextDialog.ViewListener {
                    override fun click() {
                        //finish()
                        PropsActivity.actionStart(this@SearchActivity)
                    }

                })
                .setDismissListener(object : TextDialog.DismissListener {
                    override fun onDismiss() {
                        start()
                    }

                }).show()

            SpUtils.saveSP(R.string.props_user, 1)
        } else {
            showToast(errorMsg)
        }
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        if (mErrorView != null) {
            fl_content.removeView(mErrorView)
            mErrorView = null
        }
    }

    fun actionStart(context: Context) {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
            ToastUtils.show(context, "无网络")
            return
        }
        val intent = Intent(context, SearchActivity::class.java)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.search_activity
    val mPresenter by lazy { SearchPresenter() }

    private var mFragmentAll: SearchAllFragment? = null
    private var mFragmentGame: SearchGameFragment? = null
    private var mFragmentArticle: SearchArticleFragment? = null
    private var mFragmentUser: SearchUserFragment? = null
    private var mFragmentCircle: SearchCircleFragment? = null
    private var mFragmentPost: SearchPostFragment? = null


    init {
        mPresenter.attachView(this)
    }

    override fun initData() {

    }

    /**
     * 搜索关键字
     */
    private var keyWords = ""

    /**
     * 搜索记录存储
     */
    private val mAdapterKey by lazy { SearchHistoryAdapter() }
    private val mHistoryAdapter by lazy { GameSearchHistoryAdapter(ArrayList()) }
    private val mKeywordAdapter by lazy { SearchKeywordAdapter() }


    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        historyData()
        //搜索关键词库
        initKeywordData()

        tv_cancel.setOnClickListener {
            mFinish()
        }

        //输入监听
        et_send.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (FastClickUtil.isFastClickTiming()) {
                        return false
                    }
                    val keyWord = et_send.text.toString().trim()

                    if (keyWord.isNullOrEmpty()) {
                        showToast("请输入你感兴趣的关键词")
                        return false
                    }

                    if (CheckUtils.noContainsEmoji(keyWord)) {
                        showToast("关键词不能含有表情")
                        et_send.setText("")
                        return false
                    }
                    if (CheckUtils.checkLocalAntiSpam(keyWord)) {
                        showToast("关键词含有敏感词汇")
                        et_send.setText("")
                        return false
                    }

                    closeKeyBord(et_send)

                    mHistoryAdapter.remove(keyWord)
                    mHistoryAdapter.addData(0, keyWord)
                    if (mHistoryAdapter.data.size > 20) {
                        mHistoryAdapter.remove(20)
                    }
                    keyWords = keyWord
                    start()

                    rv_keyword.visibility = View.GONE
                    if (rlHistory.visibility == View.VISIBLE) {
                        rlHistory.visibility = View.GONE
                    }
                    llSearchData.visibility = View.VISIBLE
                    SearchAgentUtils.setSearchWord(keyWords)

                }
                return false
            }
        })

        et_send.addTextChangedListener {
            if (it.toString().trim().isNotEmpty()) {
                ivDelete.visibility = View.VISIBLE
                if (et_send.hasFocus() && it.toString().trim() != keyWords) {
                    //关键词搜索功能
//                    mPresenter.loadSearch(-1, it.toString().trim(), 0)
                }
            } else {
                rv_keyword.visibility = View.GONE
                ivDelete.visibility = View.GONE
            }
        }
        ivDelete.setOnClickListener {
            et_send.setText("")
            keyWords = ""
            rv_keyword.visibility = View.GONE
        }

        initMagicIndicator()

        et_send.setFocusable(true)
        et_send.setFocusableInTouchMode(true)
        et_send.requestFocus()
        et_send.filters = ViewUtils.inputFilters

        mPresenter.getHotKeyword()
    }

    private fun initKeywordData() {
        rv_keyword.adapter = mKeywordAdapter
        mKeywordAdapter.setOnItemClickListener { _, _, position ->
            et_send.clearFocus()
            //标题所属类型：1-> 游戏  2-> 咨询  3->圈子 4->帖子 5-> 用户
            when (mKeywordAdapter.data[position].type) {
                2 -> mViewPager.currentItem = 5
                4 -> mViewPager.currentItem = 2
                5 -> mViewPager.currentItem = 4
                else -> mViewPager.currentItem = mKeywordAdapter.data[position].type
            }
            startList(mKeywordAdapter.data[position].title)
        }
    }

    override fun onBackPressed() {
        mFinish()
    }

    private fun mFinish() {
        finish()
    }


    /**
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchEvent(event: SearchEvent) {
        when (event.type) {
            0 -> {//搜索数据
                mPresenter.loadSearch(mViewPager.currentItem, keyWords, event.page)
            }
            1 -> {//切换tab
                mViewPager.currentItem = event.page
            }
            2 -> {//关注/取消
                if (isLoginStart()) {
                    mPresenter.follow(event.is_mutual, event.uid)
                }
            }
        }
    }


    /**
     * 历史数据处理
     */
    private fun historyData() {
        val layoutManager = object : FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        rv_history.layoutManager = layoutManager
        rv_history.adapter = mHistoryAdapter

        mHistoryAdapter.setOnItemClickListener { _, _, position ->
            if (mHistoryAdapter.showDelete) {
                mHistoryAdapter.remove(position)
            } else {
                //点击历史搜索
                startList(mHistoryAdapter.data[position])
            }
            if (mHistoryAdapter.data.size == 0) {
                llHistory.visibility = View.GONE
            }
        }
        val search_history = SpUtils.getString(R.string.search_history, "")
        if (!search_history.isNullOrEmpty()) {
            val mHistorys = ArrayList<String>()
            JSON.parseArray(search_history).mapTo(mHistorys) { it.toString() }
            mHistoryAdapter.setNewInstance(mHistorys)
        }

        if (mHistoryAdapter.data.size == 0) {
            llHistory.visibility = View.GONE
        }

        iv_delete.setOnClickListener {
            ll_delete.requestFocus()
            iv_delete.visibility = View.GONE
            ll_delete.visibility = View.VISIBLE
            mHistoryAdapter.showDelete = true
            mHistoryAdapter.notifyDataSetChanged()
        }

        tv_delete_all.setOnClickListener {
            mHistoryAdapter.setNewInstance(ArrayList())
            llHistory.visibility = View.GONE
        }
        tv_complete.setOnClickListener {
            iv_delete.visibility = View.VISIBLE
            ll_delete.visibility = View.GONE
            mHistoryAdapter.showDelete = false
            mHistoryAdapter.notifyDataSetChanged()
        }
        val layoutManager1 = object : FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        recycler_history1.layoutManager = layoutManager1
        recycler_history1.adapter = mAdapterKey
        mAdapterKey.setOnItemClickListener { adapter, _, position ->
            startList(adapter.data[position].toString())

            SearchAgentUtils.setSearchRecommend(adapter.data[position].toString())
        }

    }

    private fun startList(key: String, closeKeyBord: Boolean = true) {
        if (closeKeyBord) closeKeyBord(et_send)

        rv_keyword.visibility = View.GONE
        rlHistory.visibility = View.GONE
        llSearchData.visibility = View.VISIBLE

        keyWords = key
        et_send.setText(keyWords)
        et_send.setSelection(et_send.text.length)

        mHistoryAdapter.remove(keyWords)
        mHistoryAdapter.addData(0, keyWords)
        if (mHistoryAdapter.data.size > 20) {
            mHistoryAdapter.remove(20)
        }

        start()
    }

    override fun start() {
        mPresenter.loadSearch(mViewPager.currentItem, keyWords, 1)
    }


    /**
     * 打卡软键盘
     */
    fun openKeyBord(mEditText: EditText) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        SpUtils.saveSP(R.string.search_history, JSON.toJSON(mHistoryAdapter.data).toString())
        mPresenter.detachView()
    }

    private fun initMagicIndicator() {
        val title: MutableList<String> = java.util.ArrayList()
        val mFragmentList = java.util.ArrayList<Fragment>()
        title.add("综合")
        title.add("游戏")
        title.add("帖子")
        title.add("圈子")
        title.add("用户")
        title.add("资讯")
        //0 全部 1游戏 2帖子 3圈子 4用户 5资讯

        mFragmentAll = SearchAllFragment.getInstance(0)
        mFragmentGame = SearchGameFragment.getInstance(1)
        mFragmentCircle = SearchCircleFragment.getInstance(3)
        mFragmentPost = SearchPostFragment.getInstance(2)
        mFragmentUser = SearchUserFragment.getInstance(4)
        mFragmentArticle = SearchArticleFragment.getInstance(5)

        mFragmentList.add(mFragmentAll!!)
        mFragmentList.add(mFragmentGame!!)
        mFragmentList.add(mFragmentPost!!)
        mFragmentList.add(mFragmentCircle!!)
        mFragmentList.add(mFragmentUser!!)
        mFragmentList.add(mFragmentArticle!!)

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return title.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.text = (title[index])
                simplePagerTitleView.textSize = 16f
                simplePagerTitleView.normalColor = ContextCompat.getColor(context, R.color.gray99)
                simplePagerTitleView.selectedColor = ContextCompat.getColor(context, R.color.gray33)

                simplePagerTitleView.setOnClickListener {
                    if (mViewPager.currentItem != index) {
                        mViewPager.currentItem = index
                    }
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return Indicator.mLinePagerIndicator(context, R.color.color_ffcc03, 10)
            }

            override fun getTitleWeight(context: Context?, index: Int): Float {
                return 1.0f
            }
        }

        magic_indicator.navigator = commonNavigator

        mViewPager.adapter = TabAdapter(supportFragmentManager, mFragmentList)
        ViewPagerHelper.bind(magic_indicator, mViewPager)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(index: Int) {
                setSwipeBackEnable(index == 0)
                //0 全部 1游戏 2帖子 3圈子 4用户 5资讯
                when (index) {
                    1 -> {
                        mHandler.postDelayed({ mFragmentGame?.setData(keyWords) }, 200)
                    }
                    2 -> {
                        mHandler.postDelayed({ mFragmentPost?.setData(keyWords) }, 200)
                    }
                    3 -> {
                        mHandler.postDelayed({ mFragmentCircle?.setData(keyWords) }, 200)
                    }
                    4 -> {
                        mHandler.postDelayed({ mFragmentUser?.setData(keyWords) }, 200)
                    }
                    5 -> {
                        mHandler.postDelayed({ mFragmentArticle?.setData(keyWords) }, 200)
                    }
                    else -> {
                        mHandler.postDelayed({ mFragmentAll?.setData(keyWords) }, 200)
                    }
                }
            }
        })
    }

    fun setGameCollection(key: String) {
        mPresenter.gameCollection(key)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAttentionEvent(event: AttentionEvent) {
        val uid = event.userId
        val is_mutual = event.isMutual
        mFragmentAll?.setUserFollow(uid, is_mutual)
        mFragmentUser?.setUserFollow(uid, is_mutual)
        mFragmentPost?.setUserFollow(uid, is_mutual)
    }
}
