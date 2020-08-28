package com.tinytiger.titi.ui.circle.post

import android.app.Activity
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.circle.postsend.*
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.ViewUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.post.SelectLeftAdapter
import com.tinytiger.titi.adapter.circle.post.SelectRightAdapter
import com.tinytiger.titi.mvp.contract.circle.CirclesPostContract
import com.tinytiger.titi.mvp.presenter.circle.CirclesPostPresenter
import com.tinytiger.titi.utils.CheckUtils
import kotlinx.android.synthetic.main.circler_activity_category.*


/**
 *
 * @author zhw_luke
 * @date 2020/4/29 0029 上午 10:38
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 选择话题
 */
class SelectTopicActivity : BaseBackActivity(), CirclesPostContract.View {


    companion object {
        fun actionStart(context: Activity) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, SelectTopicActivity::class.java)
            context.startActivityForResult(intent, 90)
        }
    }

    private val mPresenter by lazy { CirclesPostPresenter() }
    private val mLeftAdapter by lazy { SelectLeftAdapter() }
    private val mRightAdapter by lazy { SelectRightAdapter() }
    private val mSearchAdapter by lazy { SelectRightAdapter() }


    override fun layoutId(): Int = R.layout.circler_activity_category


    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        initRecyclerView()
        tv_cancel.setOnClickListener { finish() }

        et_send.filters = ViewUtils.inputFilters
        et_send.addTextChangedListener {
            if (it.toString().trim().isNotEmpty()) {
                ivDelete.visibility = View.VISIBLE
            } else {
                ivDelete.visibility = View.GONE
            }
        }
        ivDelete.setOnClickListener {
            et_send.setText("")
            mSearchAdapter.searchTxt = ""
            fl_content.visibility = View.VISIBLE
            fl_content1.visibility = View.GONE
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


                    if (CheckUtils.checkLocalAntiSpam(keyWord)) {
                        showToast("关键词含有敏感词汇")
                        et_send.setText("")
                        return false
                    }


                    mSearchAdapter.searchTxt = keyWord
                    start()

                }
                return false
            }
        })


        start()
    }

    private var mSuspensionHeight = 0
    private var mCurrentPosition = 0
    private fun initRecyclerView() {

        rv_left.layoutManager = LinearLayoutManager(this)
        rv_left.adapter = mLeftAdapter

        mLeftAdapter.setOnItemClickListener { _, _, position ->
            mLeftAdapter.setSelection(position)
         //   RefreshView.smoothMoveToPosition(rv_right, position)
            mRightAdapter.setNewInstance(mSelectCircler1Bean!!.data[position].circle_info)
        }

        mSuspensionHeight = Dp2PxUtils.dip2px(this, 50)
        rv_right.layoutManager = LinearLayoutManager(this)
        rv_right.adapter = mRightAdapter
     /*   rv_right.setHasFixedSize(true)
        rv_right.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                if (mCurrentPosition != mRightAdapter.data[firstPosition].position) {
                    mCurrentPosition = mRightAdapter.data[firstPosition].position
                    RefreshView.smoothMoveToPosition(rv_left, mCurrentPosition)
                    mLeftAdapter.setSelection(mCurrentPosition)
                }

            }
        })*/

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mSearchAdapter


        mRightAdapter.addChildClickViewIds(R.id.ll_content)
        mRightAdapter.setOnItemChildClickListener { _, _, position ->
            val i = Intent()
            i.putExtra("topicName", mRightAdapter.data[position].name)
            i.putExtra("topicId", mRightAdapter.data[position].id)
            setResult(Activity.RESULT_OK, i)
            finish()
        }

        mSearchAdapter.addChildClickViewIds(R.id.ll_content)
        mSearchAdapter.setOnItemChildClickListener { _, _, position ->
            val i = Intent()
            i.putExtra("topicName", mSearchAdapter.data[position].name)
            i.putExtra("topicId", mSearchAdapter.data[position].id)
            setResult(Activity.RESULT_OK, i)
            finish()
        }

    }


    override fun start() {
        mPresenter.indexCircleByCate(mSearchAdapter.searchTxt, page)
    }

    override fun showCircleByCate(bean: SelectCircler2Bean) {
        fl_content.visibility = View.GONE
        fl_content1.visibility = View.VISIBLE
        if (bean.data.data != null && bean.data.data.size > 0) {
            if (bean.data.current_page == 1) {
                if (bean.data.data != null && bean.data.data.size > 0) {
                    mSearchAdapter.setNewInstance(bean.data.data)
                } else {
                    mSearchAdapter.setEmptyView(
                        RefreshView.getEmptyView(
                            this,
                            "",
                            recycler_view
                        )
                    )
                    refreshLayout.setEnableLoadMore(false)
                    mSearchAdapter.setNewInstance(ArrayList())
                }
            } else {
                mSearchAdapter.addData(bean.data.data)
            }
        } else {

            val list = java.util.ArrayList<SelectCirclerInfoBean>()
            list.add(SelectCirclerInfoBean(mSearchAdapter.searchTxt))
            mSearchAdapter.setNewInstance(list)
            refreshLayout.setEnableLoadMore(false)
        }
    }

    var mSelectCircler1Bean: SelectCircler1Bean? = null
    override fun showCircleByCate(bean: SelectCircler1Bean) {
        if (bean.data != null && bean.data.size > 0) {
            mSelectCircler1Bean=bean
            mLeftAdapter.setNewInstance(bean.data)
            val list = ArrayList<SelectCirclerInfoBean>()
            for (i in 0..(bean.data.size - 1)) {

                if (bean.data[i].circle_info != null && bean.data[i].circle_info.size > 0) {
                    for (j in bean.data[i].circle_info) {
                        j.position = i
                        list.add(j)
                    }
                }
            }
            mRightAdapter.setNewInstance(mSelectCircler1Bean!!.data[0].circle_info)

            //mRightAdapter.setNewInstance(list)
        } else {
            fl_content1.visibility = View.VISIBLE
            mSearchAdapter.setEmptyView(
                RefreshView.getEmptyView(
                    this,
                    "",
                    recycler_view
                )
            )
        }
    }

    override fun showCircleByModular(bean: SelectCirclerNameBean) {

    }

    override fun showCircleByModular(bean: SelectCirclerName2Bean) {

    }

    override fun showUserList(bean: SelectFriendBean) {

    }

    override fun showFollowUserList(bean: SelectFriend2Bean) {

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mSearchAdapter.data.size == 0) {
                mSearchAdapter.setEmptyView(RefreshView.getNetworkView(this, recycler_view, { start() }))
            }
        }
    }

}