package com.tinytiger.titi.ui.circle.post

import android.app.Activity
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.circle.postsend.*

import com.tinytiger.common.net.exception.ErrorStatus

import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.ViewUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.post.SelectCircleAdapter
import com.tinytiger.titi.mvp.contract.circle.CirclesPostContract
import com.tinytiger.titi.mvp.presenter.circle.CirclesPostPresenter
import com.tinytiger.titi.utils.CheckUtils
import kotlinx.android.synthetic.main.circler_activity_post_circle.*


/**
 *
 * @author zhw_luke
 * @date 2020/4/29 0029 上午 10:38
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 选择游戏圈子
 */
class SelectCirclerActivity : BaseBackActivity(), CirclesPostContract.View {

    companion object {
        fun actionStart(context: Activity) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, SelectCirclerActivity::class.java)
            context.startActivityForResult(intent,91)
        }
    }


    private val mPresenter by lazy { CirclesPostPresenter() }

    private val mAdapter by lazy { SelectCircleAdapter() }


    override fun layoutId(): Int = R.layout.circler_activity_post_circle


    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {

        title_view.centerTxt = "选择游戏圈子"
        title_view.setBackOnClick {
           finish()
        }
        et_send.filters= ViewUtils.inputFilters
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
        //输入监听
        et_send.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (FastClickUtil.isFastClickTiming()) {
                        return false
                    }
                    val keyWord = et_send.text.toString().trim()

                    if (keyWord.isNullOrEmpty()) {
                        showToast("请输入你感兴趣的游戏社区")
                        return false
                    }

                    if (CheckUtils.checkLocalAntiSpam(keyWord)) {
                        showToast("关键词含有敏感词汇")
                        et_send.setText("")
                        return false
                    }

                    keywords=keyWord
                    start()

                }
                return false
            }
        })
        recycler_view.adapter = mAdapter
        start()
        mAdapter.addChildClickViewIds(R.id.ll_content)
        mAdapter.setOnItemChildClickListener { _, _, position ->

            CircleId=mAdapter.data[position].id
            CircleName =mAdapter.data[position].name
            SelectModuleActivity.actionStart(this, mAdapter.data[position].id)
        }
    }
    var CircleName=""
    var CircleId=""

    var keywords = ""
    override fun start() {
        mPresenter.indexCircleByModular(keywords, page)
    }

    override fun showCircleByCate(bean: SelectCircler2Bean) {

    }

    override fun showCircleByCate(bean: SelectCircler1Bean) {

    }

    override fun showCircleByModular(bean: SelectCirclerNameBean) {

    }

    override fun showCircleByModular(bean: SelectCirclerName2Bean) {
        if (bean.data.current_page == 1) {
            if (bean.data.data!=null&& bean.data.data.size > 0) {
                mAdapter.setNewInstance(bean.data.data)
            }else{
                mAdapter.setEmptyView( RefreshView.getEmptyView(
                    this,
                    "",
                    recycler_view
                ))
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setNewInstance(ArrayList())
            }
        } else {

            mAdapter.addData(bean.data.data)
        }
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
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView( RefreshView.getNetworkView(this, recycler_view, { start() }))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                92 -> {
                   val moduleId = data.getStringExtra("ModuleId")
                    val moduleName = data.getStringExtra("ModuleName")
                    val postType = data.getIntExtra("ModuleType", -1)


                    val i = Intent()
                    i.putExtra("CircleName", CircleName)
                    i.putExtra("CircleId", CircleId)
                    i.putExtra("ModuleName", moduleName)
                    i.putExtra("ModuleId", moduleId)
                    i.putExtra("ModuleType", postType)
                    setResult(Activity.RESULT_OK, i)
                    finish()
                }
            }

        }
    }

}