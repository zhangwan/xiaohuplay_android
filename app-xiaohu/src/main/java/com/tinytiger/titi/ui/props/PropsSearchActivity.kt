package com.tinytiger.titi.ui.props

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil

import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.adapter.props.PropsSearchAdapter
import com.tinytiger.titi.data.MyUserData

import com.tinytiger.titi.mvp.contract.props.PropsContract
import com.tinytiger.titi.mvp.presenter.props.PropsPresenter

import kotlinx.android.synthetic.main.props_activity_search.*
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 道具搜索页
 */
class PropsSearchActivity : BaseActivity(), PropsContract.View {

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView( getErrorLayout())
            }
        }
    }

    override fun indexGoodsCate(bean: PropsTabListBean) {

    }

    override fun indexGoods(mBean: PropsListBean) {
        if (mBean.data.current_page == 1) {
            mAdapter.setNewInstance(mBean.data.data)
        } else {
            mAdapter.addData(mBean.data.data)
        }

        if (mBean.data.current_page >= mBean.data.last_page) {
            if (mBean.data.data.size == 0 && mBean.data.current_page==1) {
                mAdapter.setEmptyView( RefreshView.getEmptyView(
                    this,
                    "没有找到与“${keyWords}”相关的内容，换个关键词试试吧",
                    recycler_view
                ))
                refreshLayout.setEnableLoadMore(false)
            }else{
                refreshLayout.finishLoadMoreWithNoMoreData()
            }
            refreshLayout.setEnableLoadMore(false)

        } else {
            refreshLayout.setEnableLoadMore(true)
            refreshLayout.resetNoMoreData()
        }
    }

    override fun wearProps(bean: BaseBean) {

    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    companion object {

        fun actionStart(context: Context,keyWord:String) {
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return
            }
            val intent = Intent(context, PropsSearchActivity::class.java)
            intent.putExtra("keyWord", keyWord)
            context.startActivity(intent)
        }

    }


    override fun layoutId(): Int = R.layout.props_activity_search
    private val mPresenter by lazy { PropsPresenter() }
    private val mAdapter by lazy { PropsSearchAdapter() }


    init {
        mPresenter.attachView(this)
    }

    override fun initData() {
    }

    /**
     * 搜索关键字
     */
    private var keyWords = ""


    override fun initView() {

        keyWords = intent.getStringExtra("keyWord")
        tv_cancel.setOnClickListener {
            mFinish()
        }

        et_send.setText(keyWords)
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

                    //两次搜索同一数据,去除
                    if (keyWord == keyWords) {
                        return false
                    }
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

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setEnableLoadMore(false)

        refreshLayout.setOnLoadMoreListener { _ ->
            page++
           start()
        }

        recycler_view.adapter = mAdapter

        mAdapter.setOnItemClickListener { _, _, position ->
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnItemClickListener
            }

            PropsDetailActivity.actionStart(this,mAdapter.data[position].id)
        }

        start()
    }

    override fun onBackPressed() {
        mFinish()
    }
    private fun mFinish(){
        finish()
    }

    override fun start() {
       // closeKeyBord(et_send)
        if (page==1){
            showLoading()
        }
        mPresenter.searchGoodsTool(keyWords,  page)
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

}
