package com.tinytiger.titi.ui.news

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.tinytiger.common.adapter.BaseFragmentAdapter
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.event.DataReportEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.common.widget.SoftKeyBoardListener
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.news.NewsInfoContract
import com.tinytiger.titi.mvp.presenter.news.NewsInfoPresenter
import com.tinytiger.titi.ui.video.CommentFragment
import com.tinytiger.titi.ui.video.view.SoftEditTextUtils
import com.tinytiger.titi.utils.CheckUtils
import com.tinytiger.titi.widget.view.Anim.ConllectionView
import com.tinytiger.titi.widget.view.Anim.LikeView
import kotlinx.android.synthetic.main.home_fagment_game.magic_indicator
import kotlinx.android.synthetic.main.news_activity.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus

/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: @doc 文章新闻页 Activity
*/
class NewsActivity : BaseActivity(), NewsInfoContract.View {

    private val mFragmentList = ArrayList<Fragment>()
    private var titles = arrayListOf("正文", "评论")

    private var mAdapter: CommonNavigatorAdapter? = null

    private var mIndex = 0

    private var mContentId = ""

    companion object {
        private const val CONTENT_ID = "content_id"
        private const val EXTRA_INDEX = "index"

        fun actionStart(context: Context, content_id: String) {
            actionStart(context, content_id, 0)
        }

        fun actionStart(context: Context, content_id: String, index: Int) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }

            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, NewsActivity::class.java)
            intent.putExtra(CONTENT_ID, content_id)
            intent.putExtra(EXTRA_INDEX, index)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.news_activity

    private val mPresenter by lazy { NewsInfoPresenter() }

    init {
        mPresenter.attachView(this)
    }


    private var mNewsInfoFragment: NewsInfoFragment? = null
    private var mCommentFragment: NewsCommentFragment? = null
    /**
     * 子评论
     */
    private var mCommentDetailFragment: CommentFragment? = null

    override fun initData() {
        if (intent.hasExtra(CONTENT_ID)) {
            mContentId = intent.getStringExtra(CONTENT_ID)
        }
        mIndex = intent.getIntExtra(EXTRA_INDEX, 0)
    }

    override fun initView() {
        SearchAgentUtils.setNewTxt(mContentId)

        ivBack.setOnClickListener(mOnClickListener)

        iv_more.setOnClickListener(mOnClickListener)
        ivShare.setOnClickListener(mOnClickListener)
        tv_send.setOnClickListener(mOnClickListener)

        initMagicIndicator()

        mViewPager.currentItem = mIndex

        initFragment()

        val softKeyBoardListener = SoftKeyBoardListener(this)
        softKeyBoardListener.setListener(object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                changeSoftInput(true)
            }

            override fun keyBoardHide(height: Int) {
                changeSoftInput(false)
            }
        })

        start()

        viewCollection.showNum = false
        viewCollection.mListener = ConllectionView.OnLikeViewListener {
            if (mContentInfoBean!!.is_collect == -1) {
                mPresenter.addCollect(mContentId)
            } else {
                mPresenter.cancelCollect(mContentId)
            }
        }


        viewLike.showNum = false
        viewLike.mListener = LikeView.OnLikeViewListener {
            if (mContentInfoBean!!.is_like == -1) {
                mPresenter.addLike(mContentId)
                mNewsInfoFragment?.showLike(1)
            } else {
                mPresenter.cancelLike(mContentId)
                mNewsInfoFragment?.showLike(-1)
            }
        }

    }

    override fun start() {
        mPresenter.getContentInfo(mContentId)
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (SoftEditTextUtils.isSoftShowing(this) && SoftEditTextUtils.isShouldHideInput(
                    view,
                    ev
                ) && !SoftEditTextUtils.isTouchView(tv_send, ev)
            ) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                return true
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    private fun changeSoftInput(isSend: Boolean) {
        viewLike.visibility = if (isSend) View.GONE else View.VISIBLE
        viewCollection.visibility = if (isSend) View.GONE else View.VISIBLE
        ivShare.visibility = if (isSend) View.GONE else View.VISIBLE
        tv_send.visibility = if (isSend) View.VISIBLE else View.GONE
    }


    private var isChangeNumber = false

    private fun initFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_container, mCommentDetailFragment!!)
            .commit()

        mCommentFragment?.setonCommentClickListener(object : NewsCommentFragment.onCommentClickListener {
            override fun refreshComment(comment_num: Int, needScroll: Boolean) {
                isChangeNumber = true
                showTitle(comment_num)
            }

            override fun showKeyBord(nickname: String) {
                TODO("Not yet implemented")
            }

//            override fun clickComment(conmentBean: CommentDetailBean, is_rely: Boolean) {
//                mCommentDetailFragment!!.setComment(conmentBean, conmentBean.id, mContentInfoBean!!.user_id, false)
//                fl_container.visibility = View.VISIBLE
//                et_send.setText("")
//            }

        })

        mCommentDetailFragment?.setOnCommentClickListener(object : CommentFragment.OnCommentClickListener {
            override fun clickBack() {
                et_send.setText("")
                fl_container.visibility = View.GONE
                mCommentFragment?.start()
            }

        })
    }

    private fun initMagicIndicator() {
        mNewsInfoFragment = NewsInfoFragment.getInstance(mContentId)
        mCommentFragment = NewsCommentFragment.getInstance(mContentId)

        mCommentDetailFragment = CommentFragment.getInstance(mContentId, 1)

        mFragmentList.add(mNewsInfoFragment!!)
        mFragmentList.add(mCommentFragment!!)

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true

        mAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                //文字

                val titleView = Indicator.mTitleView(context, titles[index], 1f, Typeface.NORMAL)
                titleView.setOnClickListener {
                    mViewPager.currentItem = index
                }
                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return Indicator.mLinePagerIndicator(context,R.color.color_ffcc03,10)
            }
        }

        commonNavigator.adapter = mAdapter
        magic_indicator.navigator = commonNavigator

        ViewPagerHelper.bind(magic_indicator, mViewPager)
        mViewPager.adapter = BaseFragmentAdapter(supportFragmentManager, mFragmentList)
    }


    private val mOnClickListener = View.OnClickListener { v ->
        when (v?.id) {
            R.id.ivBack -> finish()
            R.id.ivShare, R.id.iv_more -> {

                if (mContentInfoBean == null) {
                    return@OnClickListener
                }
                showShareDialog()
            }
            R.id.tv_send -> {
                sendMessage()
            }
        }
    }


    private fun sendMessage() {
        if (FastClickUtil.isFastClickTiming())
            return

        if (MyUserData.isEmptyToken()) {
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }

        val keyWord = et_send.text.toString().trim()
        if (keyWord.isEmpty()) {
            showToast("请输入评论")
            return
        }
        if (keyWord.length > 1000) {
            showToast("字数限制最多1000字，请调整后再发")
            return
        }
        if (CheckUtils.checkLocalAntiSpam(keyWord)) {
            showToast("含有敏感词汇")
            et_send.setText("")
            return
        }

        if (mViewPager.currentItem == 0) {
            mViewPager.currentItem = 1
        }

        closeKeyBord(et_send)
        et_send.setText("")
        mCommentFragment!!.sendCommend(keyWord)
    }


    /**
     * 关闭软键盘
     */
    private fun closeKeyBord(mEditText: EditText) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }


    override fun onResume() {
        super.onResume()
        //TCAgent.onPageStart(this, "页面-资讯详情")
        sysTime = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
       // TCAgent.onPageEnd(this, "页面-资讯详情")

        val time = System.currentTimeMillis() - sysTime
        if (time > 3000) {
            EventBus.getDefault().post(DataReportEvent(mNewsInfoFragment!!.view_log_id, (time / 1000).toInt()))
        }
    }


    var mContentInfoBean: ContentInfoBean.DataBean? = null


    private fun showTitle(comment_num: Int) {
        if (isChangeNumber) {
            titles = arrayListOf("正文", "评论(${StringUtils.sizeToString(comment_num)})")
            mAdapter?.notifyDataSetChanged()
            isChangeNumber = false
        }
    }


    override fun showContentInfo(bean: ContentInfoBean.DataBean) {
        showTitle(bean.comment_num)
        mContentInfoBean = bean

        viewLike.setLike(bean.is_like, 0)
        viewCollection.setConllection(bean.is_collect, 0)

        mNewsInfoFragment?.apply {
            showContentInfo(bean)
        }

        if (!isFirst) {
            mCommentFragment?.apply {
                showContentInfo(bean)
            }
            isFirst = true
        }
    }

    private var isFirst = false

    override fun showComment(bean: CommentListBean.DataBean) {
    }

    override fun showIntroContentList(bean: ArrayList<RecommendBean>) {
    }

    override fun showResult() {
        start()
    }


    private fun showShareDialog() {
        if (mContentInfoBean == null)
            return
        val url =
            "${mContentInfoBean!!.share_url}?user_id=${mContentInfoBean!!.user_id}&content_id=$mContentId"

        val desc = StringUtils.toPlainText(mContentInfoBean!!.introduce)
        ShareDialog.create(supportFragmentManager)
            .apply {
                class_name = "no"
                share_url = url
                share_title = mContentInfoBean!!.title
                share_desc = desc
                share_image = mContentInfoBean!!.cover
                userId = mContentInfoBean!!.user_id
                collectionType = 0
                contentId = "" + mContentId
                report_type = 2
            }
            .show()
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
        if (errorCode == 10002 || errorCode == 1005) {
            showNoCommentDialog(errorMsg)
        } else if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mErrorView == null && mContentInfoBean == null) {
                mErrorView = getErrorLayout(true)
                fl_content.addView(mErrorView)
            }
            showToast(errorMsg)
        } else {
            showToast(errorMsg)
        }
    }

    private fun showNoCommentDialog(errorMsg: String) {
        TextDialog
            .create(supportFragmentManager)
            .isShowTitle(true)
            .setShowButton(isCancel = false, isConfirm = true)
            .isCancelOutside(false)
            .setMessage(errorMsg)
            .setDismissListener(object : TextDialog.DismissListener {
                override fun onDismiss() {
                    finish()
                }
            })
            .show()
    }

    override fun showLike(is_like: Int) {
        mContentInfoBean!!.is_like = is_like
    }

    override fun showCollect(is_collect: Int) {
        mContentInfoBean!!.is_collect = is_collect
    }
}
