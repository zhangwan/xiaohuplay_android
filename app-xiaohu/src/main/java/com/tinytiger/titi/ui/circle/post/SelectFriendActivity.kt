package com.tinytiger.titi.ui.circle.post


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.alibaba.fastjson.JSON
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.circle.postsend.*
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.ViewUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.post.MultiFriendAdapter
import com.tinytiger.titi.mvp.contract.circle.CirclesPostContract
import com.tinytiger.titi.mvp.presenter.circle.CirclesPostPresenter
import kotlinx.android.synthetic.main.circler_activity_sendpost_friend.*
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/3/30 0030 下午 4:26
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 邀请回答
 */
class SelectFriendActivity : BaseBackActivity(), CirclesPostContract.View {

    private val mAdapter by lazy { MultiFriendAdapter(ArrayList()) }
    private val mPresenter by lazy { CirclesPostPresenter() }

    private val listAll = ArrayList<MultiFriendBean>()
    private val listOld = ArrayList<Int>()

    companion object {

        fun actionStart(context: Activity, data: ArrayList<FriendBean>) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return
            }
            if (SpUtils.getString(R.string.token, "").isEmpty()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            val intent = Intent(context, SelectFriendActivity::class.java)
            intent.putExtra("data", data)
            intent.putExtra("type", 0)
            context.startActivityForResult(intent, 93)
        }

        fun actionStart(context: Context, post_id: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return
            }
            if (SpUtils.getString(R.string.token, "").isEmpty()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            val intent = Intent(context, SelectFriendActivity::class.java)
            intent.putExtra("post_id", post_id)
            intent.putExtra("type", 1)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.circler_activity_sendpost_friend
    //ar circleId = ""
    var keywords = ""
    var postId = "0"
    var listFriend = ArrayList<FriendBean>()
    /**
     * 页面状态 1发帖页2帖子列表邀请
     */
    var type = 0

    override fun initData() {
        mPresenter.attachView(this)
        type = intent.getIntExtra("type", 0)
        if (type == 0) {
            val list = intent.getParcelableArrayListExtra<FriendBean>("data")
            if (list != null && list.size > 0) {
                listFriend.addAll(list)
            }
        } else {
            postId = intent.getStringExtra("post_id")
        }
    }

    override fun initView() {
        iv_back.setOnClickListener { finish() }

        rvInvite.adapter = mAdapter
        mAdapter.tvNum = tvNum

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

            mAdapter.setlist(listAll)

        }
        //输入监听
        et_send.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    setSearch()
                }
                return false
            }
        })
        tvSearch.setOnClickListener {
            setSearch()
        }

        mPresenter.getUserList(postId)

        llSend.setOnClickListener {
            if (type == 0) {
                val list = ArrayList<FriendBean>()
                for (i in mAdapter.data) {
                    if (i.mBean != null && i.mBean.select) {
                        list.add(i.mBean)
                    }
                }

                val i = Intent()
                i.putExtra("FriendBean", list)
                setResult(Activity.RESULT_OK, i)
                finish()
            } else {

                val list = ArrayList<Int>()

                for (i in mAdapter.data) {
                    if (i.mBean != null && i.mBean.select && !i.mBean.old) {
                        list.add(i.mBean.user_id)
                    }
                }

                if (list.size > 0) {
                    mPresenter.inviteUser(postId, JSON.toJSONString(list), this)
                } else {
                    showToast("请选择邀请回答用户")
                }
            }
        }
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            mPresenter.getFollowUserList(page)
        }
    }


    override fun start() {

    }


    override fun showCircleByCate(bean: SelectCircler2Bean) {
    }

    override fun showCircleByCate(bean: SelectCircler1Bean) {
    }

    override fun showCircleByModular(bean: SelectCirclerNameBean) {
    }

    override fun showCircleByModular(bean: SelectCirclerName2Bean) {
    }

    override fun showUserList(bean: SelectFriendBean) {
        listAll.clear()

        val list = ArrayList<MultiFriendBean>()
        if (bean.data.qa_user != null && bean.data.qa_user.size > 0) {
            list.add(MultiFriendBean("问答团"))
            for (i in bean.data.qa_user) {
                list.add(MultiFriendBean(i))
            }
        }
        if (bean.data.recommend_user != null && bean.data.recommend_user.size > 0) {
            list.add(MultiFriendBean("推荐用户"))
            for (i in bean.data.recommend_user) {
                list.add(MultiFriendBean(i))
            }
        }
        if (bean.data.follow_user != null && bean.data.follow_user.data != null && bean.data.follow_user.data.size > 0) {
            list.add(MultiFriendBean("关注用户"))
            for (i in bean.data.follow_user.data) {
                list.add(MultiFriendBean(i))
            }

            if (bean.data.follow_user.current_page >= bean.data.follow_user.last_page) {
                refreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                refreshLayout.resetNoMoreData()
            }
        }

        if (list.size > 0) {
            if (type == 0) {
                for (i in list) {
                    if (i.mBean != null) {
                        for (j in listFriend) {
                            if (i.mBean.user_id == j.user_id) {
                                i.mBean.select = true
                                break
                            }
                        }
                    }
                }
            } else {
                listOld.addAll(bean.data.selected_user)
                for (j in bean.data.selected_user) {
                    for (i in list) {
                        if (i.mBean != null && i.mBean.user_id == j) {
                            i.mBean.old = true
                            i.mBean.select = true
                            break
                        }
                    }
                }
            }

            listAll.addAll(list)
            mAdapter.setlist(listAll)
        } else {
            mAdapter.setEmptyView(
                RefreshView.getEmptyView(
                    this,
                    "",
                    rvInvite
                )
            )
        }
    }

    override fun showFollowUserList(bean: SelectFriend2Bean) {
        if (bean.data.list != null && bean.data.list.size > 0) {
            val list = ArrayList<MultiFriendBean>()
            for (i in bean.data.list) {
                val bean = MultiFriendBean(i)
                for (j in listFriend) {
                    if (bean.mBean.user_id == j.user_id) {
                        bean.mBean.select = true
                        break
                    }
                }
                list.add(bean)
            }
            listAll.addAll(list)
            mAdapter.addData(list)
            /*   if (bean.data.next_page_id > 0) {
                   page = bean.data.next_page_id
               } else {
                   refreshLayout.finishLoadMoreWithNoMoreData()
               }*/
            if (bean.data.current_page >= bean.data.last_page) {
                refreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                refreshLayout.resetNoMoreData()
            }
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData()
        }
    }


    private fun setSearch() {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        val keyWord = et_send.text.toString().trim()
        if (keyWord.isNullOrEmpty()) {
            showToast("请输入你感兴趣的关键词")
            return
        }
        closeKeyBord(et_send)
        val search = ArrayList<MultiFriendBean>()
        for (i in listAll) {
            if (i.mBean == null) {
                //  search.add(i)
            } else {
                if (i.mBean.nickname.contains(keyWord)) {
                    search.add(i)
                }
            }
        }

        if (search.size > 0) {
            mAdapter.setSearchlist(search)
        } else {
            mAdapter.setNewInstance(ArrayList())
            mAdapter.setEmptyView(
                RefreshView.getEmptyView(
                    this,
                    "",
                    rvInvite
                )
            )
        }

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getNetworkView(this, rvInvite) { start() })
            }
        }
    }

    fun closeKeyBord(mEditText: EditText) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }
}