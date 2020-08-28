package com.tinytiger.titi.ui.mine.me

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.home2.AmwayBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.home2.MineAmwayAdapter
import com.tinytiger.titi.mvp.contract.center.MineWorkContract
import com.tinytiger.titi.mvp.presenter.center.MineWorksPresenter
import com.tinytiger.titi.ui.game.GameReviewsActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import kotlinx.android.synthetic.main.activity_base_recycler.*
import kotlinx.android.synthetic.main.activity_base_recycler.tvTitle
import org.greenrobot.eventbus.EventBus
import java.lang.StringBuilder


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 个人中心 - 我的作品 Activity 安利文
*/
class MineAmwayActivity : BaseActivity(), MineWorkContract.View {

    private val mPresenter by lazy { MineWorksPresenter() }
    val mAdapter by lazy { MineAmwayAdapter() }

    companion object {
        fun actionStart(context: Context) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            val intent = Intent(context, MineAmwayActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.activity_base_recycler

    override fun initData() {
        mPresenter.attachView(this)
    }


    override fun initView() {
        tvTitle.centerTxt = "我的作品"
        tvTitle.setBackOnClick {
            finish()
        }
        tvTitle.setRightOnClick {
            when (tvTitle.rightTxt) {
                "编辑" -> {
                    tvTitle.rightTxt = "完成"
                    tvTitle.setRightTxtColor(Color.parseColor("#0FB50A"))

                    for (item in mAdapter.data) {
                        item.isSelected = false
                    }

                    mAdapter.showDeleteIcon = true
                    mAdapter.notifyDataSetChanged()
                }
                "完成" -> {
                    tvTitle.rightTxt = "编辑"
                    tvTitle.setRightTxtColor(Color.parseColor("#323232"))
                    mAdapter.showDeleteIcon = false
                    start()
                }
                "删除" -> {
                    val sb = StringBuilder()
                    for (item in mAdapter.data) {
                        if (item.isSelected) {
                            sb.append(item.id).append(",")
                        }
                    }
                    if (sb.isEmpty()) {
                        showToast("请选择删除的作品")
                        return@setRightOnClick
                    }
                    sb.deleteCharAt(sb.length - 1)
                    Logger.d("$sb")


                    TextDialog.create(supportFragmentManager)
                        .setMessage("是否删除作品")
                        .setViewListener(object : TextDialog.ViewListener {
                            override fun click() {

                                mPresenter.delUserGameAmwayList(sb.toString())
                            }

                        })
                        .show()
                }


            }

        }


        //评论详情
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)


        mAdapter.addChildClickViewIds(
            R.id.rl_content,
            R.id.rlGameInfo,
            R.id.llReply,
            R.id.tvRead,
            R.id.tvShare
        )
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (tvTitle.rightTxt == "完成" || tvTitle.rightTxt == "删除") {
                mAdapter.data[position].isSelected = !mAdapter.data[position].isSelected
                mAdapter.notifyItemChanged(position)

                if (mAdapter.data[position].isSelected) {
                    tvTitle.rightTxt = "删除"
                    tvTitle.setRightTxtColor(Color.parseColor("#FF556E"))
                } else {
                    var is_select = false
                    for (item in mAdapter.data) {
                        if (item.isSelected) {
                            is_select = true
                        }
                    }
                    if (!is_select) {
                        tvTitle.rightTxt = "完成"
                        tvTitle.setRightTxtColor(Color.parseColor("#0FB50A"))
                    }
                }

            } else {
                when (view.id) {
                   R.id.rl_content -> {

                        GameReviewsActivity.actionStart(
                            this,
                            mAdapter.data[position].game_id,
                            mAdapter.data[position].id
                        )
                    }

                    R.id.rlGameInfo -> {
                        GameDetailActivity.actionStart(this, mAdapter.data[position].game_id,7)
                    }
                    R.id.llReply -> {
                        if (SpUtils.getString(R.string.token, "").isEmpty()) {
                            EventBus.getDefault().post(ClassEvent("LoginActivity"))
                            return@setOnItemChildClickListener
                        }

                        GameReviewsActivity.actionStart(
                            this,
                            mAdapter.data[position].game_id,
                            mAdapter.data[position].id,
                            1
                        )

                    }
                    R.id.tvRead -> {
                        if (SpUtils.getString(R.string.token, "").isEmpty()) {
                            EventBus.getDefault().post(ClassEvent("LoginActivity"))
                            return@setOnItemChildClickListener
                        }
                        GameReviewsActivity.actionStart(
                            this,
                            mAdapter.data[position].game_id,
                            mAdapter.data[position].id,
                            1
                        )

                    }
                    R.id.tvShare -> {
                        if (FastClickUtil.isFastClickTiming()) {
                            return@setOnItemChildClickListener
                        }
                        ShareDialog.create(supportFragmentManager)
                            .apply {
                                class_name = "GameAssess"
                                share_url =
                                    mAdapter.data[position].share_url + "?game_id=${mAdapter.data[position].game_id}&assess_id=${mAdapter.data[position].id}"
                                share_title = "《${mAdapter.data[position].game_name}》的评价"
                                share_desc = mAdapter.data[position].title
                                share_image = mAdapter.data[position].thumbnail
                                contentId = mAdapter.data[position].id
                                id=mAdapter.data[position].game_id
                            }
                            .show()
                    }
                }
            }


        }


        refreshLayout.setOnRefreshListener {
            page = 1
            mPresenter.getUserGameAmwayList(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getUserGameAmwayList(page)
        }



        mAdapter.listener = object : MineAmwayAdapter.OnHomePushListener {
            override fun onChecked(is_check: Boolean) {


                if (is_check) {
                    tvTitle.rightTxt = "删除"
                    tvTitle.setRightTxtColor(Color.parseColor("#FF556E"))
                } else {
                    var is_select = false
                    for (item in mAdapter.data) {
                        if (item.isSelected) {
                            is_select = true
                        }
                    }
                    if (!is_select) {
                        tvTitle.rightTxt = "完成"
                        tvTitle.setRightTxtColor(Color.parseColor("#0FB50A"))
                    }
                }
            }

            override fun onLike(item: AmwayBean) {
                mPresenter.likeAssessOrTag(item.game_id, item.id, "0")
            }


        }


    }


    override fun delUserGameAmway() {

        start()
    }

    override fun showDynamicNodeList(bean: PostInfoBean) {

    }

    override fun showAnswersNodeList(bean: PostInfoBean) {

    }


    override fun onResume() {
        super.onResume()
        start()
    }


    override fun start() {
        page = 1
        mPresenter.getUserGameAmwayList(page)
    }


    override fun showGameAmwayList(bean: UserGameAmwayList.Data) {
        mAdapter.showDeleteIcon = false

        mAdapter.removeAllFooterView()
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        if (bean.current_page == 1) {
            if (bean.data == null || bean.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getEmptyView(
                        this,
                        "暂无数据",
                        recycler_view
                    )
                )
                //  refreshLayout.setEnableLoadMore(false)
            }
            mAdapter.setNewInstance(bean.data)


        } else {
            mAdapter.addData(bean.data)
        }
        if (bean.current_page >= bean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getNewFooterView(this, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }


        if (mAdapter.data.size == 0) {
            tvTitle.rightTxt = ""
        } else {
            tvTitle.rightTxt = "编辑"
            tvTitle.setRightTxtColor(Color.parseColor("#323232"))
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

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mErrorView == null && mAdapter.data.size == 0) {
                mErrorView = getErrorLayout()
                fl_content.addView(mErrorView)
            }
        } else {
            dismissLoading()
        }
    }
}