package com.tinytiger.titi.ui.search

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
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
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.mvp.contract.SearchContract
import com.tinytiger.titi.mvp.presenter.SearchPresenter
import com.tinytiger.titi.utils.CheckUtils
import kotlinx.android.synthetic.main.search_activity_post.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc app帖子搜索页
 */
class SearchPostActivity : BaseBackActivity(), SearchContract.View {

    override fun getHotList(mBean: HotKeyBean) {
    }

    override fun getKeyword(mBean: SearchKeywordBean) {

    }

    override fun getAllSearch(mBean: SearchAllBean) {

    }

    override fun getGameSearch(mBean: SearchGameBean) {

    }

    override fun getEssaySearch(mBean: SearchNewsBean) {

    }

    override fun getUserSearch(mBean: SearchUserBean) {

    }

    override fun getPostSearch(mBean: PostInfoBean) {
        mFragmentPost?.getData(keyWords, mBean)
    }

    override fun getCircleSearch(mBean: CircleMeBean) {

    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mErrorView == null) {
                mErrorView = getErrorLayout()
            }
            fl_content.addView(mErrorView)
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

    fun actionStart(context: Context,circle_id:String) {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
            ToastUtils.show(context, "无网络")
            return
        }
        val intent = Intent(context, SearchPostActivity::class.java)
        intent.putExtra("circle_id",circle_id)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.search_activity_post
    private val mPresenter by lazy { SearchPresenter() }

    private var mFragmentPost: SearchPostFragment? = null


    init {
        mPresenter.attachView(this)
    }
    var circleId=""
    override fun initData() {
        circleId= intent.getStringExtra("circle_id")
    }

    /**
     * 搜索关键字
     */
    private var keyWords = ""

    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

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
                    keyWords = keyWord
                    page=1
                    start()
                }
                return false
            }
        })


        et_send.addTextChangedListener {
            if (it.toString().trim().isNotEmpty()) {
                ivDelete.visibility = View.VISIBLE
            } else {
                ivDelete.visibility = View.GONE
            }
        }
        ivDelete.setOnClickListener {
            et_send.setText("")
        }

        et_send.setFocusable(true)
        et_send.setFocusableInTouchMode(true)
        et_send.requestFocus()
        et_send.filters= ViewUtils.inputFilters

        val transaction = supportFragmentManager.beginTransaction()
        mFragmentPost?.let {
            transaction.show(it)
        } ?: SearchPostFragment.getInstance(3).let {
            mFragmentPost = it
            transaction.add(R.id.fl_content, it, "mFragment1")
        }

        transaction.commitAllowingStateLoss()
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
                mPresenter.searchPost(keyWords, event.page,circleId)
            }
            1 -> {//切换tab

            }
            2 -> {//关注/取消
                if (isLoginStart()) {
                    mPresenter.follow( event.is_mutual,event.uid)
                }
            }
        }
    }

    override fun start() {
        mPresenter.searchPost(keyWords, page,circleId)
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
        mPresenter.detachView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAttentionEvent(event: AttentionEvent) {
        val uid=event.userId
        val is_mutual=event.isMutual
        mFragmentPost?.setUserFollow(uid, is_mutual)

    }
}
