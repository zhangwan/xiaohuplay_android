package com.tinytiger.titi.ui.game.category

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.gametools.category.GameCategoryBannerBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryDetailListBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.sort.GameCategoryAdapter
import com.tinytiger.titi.adapter.gametools.sort.GameSortMenuAdapter
import com.tinytiger.titi.mvp.contract.gametools.GameCategoryContract
import com.tinytiger.titi.mvp.presenter.gametools.GameCategoryPresenter
import com.tinytiger.titi.ui.search.SearchActivity
import com.tinytiger.common.utils.umeng.HomeAgentUtils
import kotlinx.android.synthetic.main.game_activity_category_list.*


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 游戏分类列表 Activity
*/
class GameCategoryListActivity : BaseBackActivity(), GameCategoryContract.View {


    companion object {
        fun actionStart(context: Context) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, GameCategoryListActivity::class.java)
            context.startActivity(intent)
        }
    }


    private val mPresenter by lazy { GameCategoryPresenter() }

    private val mLeftAdapter by lazy { GameSortMenuAdapter() }

    private val mRightAdapter by lazy { GameCategoryAdapter() }

    private val mList: ArrayList<GameCategoryBean> = ArrayList()


    override fun layoutId(): Int = R.layout.game_activity_category_list


    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {

        iv_back.setOnClickListener {
            finish()
        }


        initRecyclerView()

       tv_search.setOnClickListener {
            SearchActivity().actionStart(this)
        }

        start()
        HomeAgentUtils.setCategoryShow()
    }

    private var mSuspensionHeight = 0
    private var mCurrentPosition = 0
    private fun initRecyclerView() {
        rv_left.layoutManager = LinearLayoutManager(this)
        rv_left.adapter = mLeftAdapter

        mLeftAdapter.setOnItemClickListener { _, _, position ->
            mLeftAdapter.setSelection(position)
            RefreshView.smoothMoveToPosition(rv_right, position)
        }

        mSuspensionHeight=Dp2PxUtils.dip2px(this,50)
        rv_right.layoutManager = LinearLayoutManager(this)
        rv_right.adapter = mRightAdapter
        rv_right.setHasFixedSize(true)
        rv_right.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()

                val view: View? = layoutManager.findViewByPosition(firstPosition + 1)
                if (view != null) {
                    if (view.top <= mSuspensionHeight) {
                        tvTitle.y=(-(mSuspensionHeight - view.top).toFloat())
                    } else {
                        tvTitle.y=(0f)
                    }
                }

                if (mCurrentPosition !=firstPosition ) {
                    mCurrentPosition = firstPosition
                    tvTitle.text = mList[mCurrentPosition].cate_name
                    tvTitle.y=(0f)
                    RefreshView.smoothMoveToPosition(rv_left, mCurrentPosition)
                    mLeftAdapter.setSelection(mCurrentPosition)
                }
            }
        })
    }

    override fun start() {
        mPresenter.getGameCategory()
    }

    override fun showGameCategory(item: GameCategoryListBean) {

        if (mErrorView != null) {
            fl_content.removeView(mErrorView)
            mErrorView = null
        }
        if (item.data != null && item.data.size > 0) {
            mList.clear()
            mList.addAll(item.data)
            tvTitle.text = mList[mCurrentPosition].cate_name
            mLeftAdapter.setNewInstance(mList)
            mRightAdapter.setNewInstance(mList)
        } else {

            mErrorView = getEmptyLayout()
            fl_content.addView(mErrorView)
        }
    }

    override fun showGameCategoryDetailList(data: GameCategoryDetailListBean.DataBean) {

    }

    override fun showGameCategoryBanner(data: ArrayList<GameCategoryBannerBean.DataBean>) {

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


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mErrorView == null) {
                mErrorView = getErrorLayout()
                fl_content.addView(mErrorView)
            }
        }
    }

    fun getEmptyLayout(): View {
        val view = View.inflate(this, com.tinytiger.common.R.layout.view_empty, null)
        view.background = ContextCompat.getDrawable(this, R.color.white)
        val lp = view.layoutParams

      /*  lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        view.layoutParams = lp*/
        return view
    }

}