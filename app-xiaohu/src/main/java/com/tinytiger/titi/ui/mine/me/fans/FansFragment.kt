package com.tinytiger.titi.ui.mine.me.fans

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.user.FriendBean

import com.tinytiger.common.net.data.user.FriendListBean
import com.tinytiger.common.net.exception.ErrorStatus

import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.FriendAdapter
import com.tinytiger.titi.mvp.contract.mine.FriendContract
import com.tinytiger.titi.mvp.presenter.mine.FriendPresenter
import com.tinytiger.titi.utils.CheckUtils

import kotlinx.android.synthetic.main.activity_base_recycler.recycler_view
import kotlinx.android.synthetic.main.activity_base_recycler.refreshLayout
import kotlinx.android.synthetic.main.mine_fragment_fans.*


/**
 *
 * @author zhw_luke
 * @date 2019/10/30 0030 下午 1:51
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 粉丝列表
 */
class FansFragment : BaseFragment(), FriendContract.View {
    override fun getUserFollow(position: Int, uid: String, is_mutual: Int) {
        if (mAdapter.data.size > position && mAdapter.data[position].user_id == uid) {
            if (user_id.isEmpty()) {
                if (is_mutual == -1) {
                    mAdapter.data[position].is_mutual = -1
                } else {
                    mAdapter.data[position].is_mutual = 1
                }
            } else {
                mAdapter.data[position].is_mutual = is_mutual
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun getFriendList(mBean: FriendListBean) {
        if (mBean.data==null){
            mAdapter.setEmptyView( RefreshView.getEmptyView(
                activity,
                "",
                recycler_view
            ))
            refreshLayout.setEnableLoadMore(false)
            mAdapter.setNewInstance(ArrayList())
            return
        }
        if (mBean.data.current_page == 1) {
            if (mBean.data.data!=null&& mBean.data.data.size > 0) {
                mAdapter.setNewInstance(mBean.data.data)
            }else{
                mAdapter.setEmptyView( RefreshView.getEmptyView(
                    activity,
                    "",
                    recycler_view
                ))
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setNewInstance(ArrayList())
            }
        } else {
            mAdapter.addData(mBean.data.data)
        }

        if (mBean.data.current_page >= mBean.data.last_page) {
            refreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            refreshLayout.resetNoMoreData()
        }
    }


    companion object {
        fun getInstance(uid: String): FansFragment {
            val fragment = FansFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.user_id = uid
            return fragment
        }

        fun getInstance(): FansFragment {
            val fragment = FansFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.mine_fragment_fans

    private val mPresenter by lazy { FriendPresenter() }
    private val mAdapter by lazy { FriendAdapter() }

    //不为空查看他人粉丝
    //为空查看自己粉丝
    private var user_id = ""
    /**
     * 初始化 ViewI
     */
    override fun initView() {
        mPresenter.attachView(this)

        recycler_view.adapter = mAdapter
        mAdapter.uid = user_id

        refreshLayout.setOnRefreshListener { _ ->
            page = 1
            start()
        }

        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            start()
        }

        mAdapter.listener = object : FriendAdapter.onFriendListener {
            override fun onType(mBean: FriendBean, position: Int) {
                if (user_id.isEmpty()) {
                    if (mAdapter.data[position].is_mutual != -1) {
                        TextDialog.create(childFragmentManager)
                            .setMessage("确定要取消关注吗？")
                            .setViewListener(object : TextDialog.ViewListener {
                                override fun click() {
                                    mPresenter.follow(
                                        position,
                                        mAdapter.data[position].user_id,
                                        1
                                    )
                                }
                            }).show()
                    } else {
                        mPresenter.follow(
                            position,
                            mAdapter.data[position].user_id,
                            -1
                        )
                    }
                } else {
                    if (mAdapter.data[position].is_mutual >= 0) {
                        TextDialog.create(childFragmentManager)
                            .setMessage("确定要取消关注吗？")
                            .setViewListener(object : TextDialog.ViewListener {
                                override fun click() {
                                    mPresenter.follow(
                                        position,
                                        mAdapter.data[position].user_id,
                                        mAdapter.data[position].is_mutual
                                    )
                                }
                            }).show()
                    } else {
                        mPresenter.follow(
                            position,
                            mAdapter.data[position].user_id,
                            mAdapter.data[position].is_mutual
                        )
                    }
                }
            }
        }
        if (user_id.isEmpty()) {
            rl_content.visibility=View.VISIBLE

            //输入监听
            et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        val keyWord = et_search.text.toString().trim()
                        sendData(keyWord)
                    }
                    return false
                }
            })

            et_search.addTextChangedListener {
                if (it.toString().trim().isNotEmpty()) {
                    ivDelete.visibility = View.VISIBLE
                } else {
                    ivDelete.visibility = View.GONE
                }
            }

            ivDelete.setOnClickListener {
                et_search.setText("")
                ivDelete.visibility=View.GONE
                sendData("")
            }

            closeKeyBord(et_search)
        }
    }

    override fun start() {
        mPresenter.loadFans(user_id, page, keyWords)
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(activity, recycler_view, {
                        showLoading()
                        start() }))
            }
        } else {

        }
    }

    override fun showLoading() {
        LoadingUtils.getInstance().show(context)
    }

    override fun dismissLoading() {
        LoadingUtils.getInstance().dismiss()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }
    override fun onResume() {
        super.onResume()
        if (mAdapter.data.size==0){
            page=1
            refreshLayout.autoRefresh()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    /**
     * 搜索关键字
     */
    private var keyWords = ""

    private fun sendData(search: String) {
        val keyWord = search

        if (CheckUtils.noContainsEmoji(keyWord)) {
            showToast("不能含有表情")
            et_search?.setText("")
            return
        }
        if (CheckUtils.checkLocalAntiSpam(keyWord)) {
            showToast("含有敏感词汇")
            et_search?.setText("")
            return
        }
        closeKeyBord(et_search!!)
        //两次搜索同一数据,去除
        if (keyWord == keyWords) {
            return
        }

        page = 1
        keyWords = keyWord
        start()
    }


    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText) {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
        mEditText.clearFocus()
    }
}
