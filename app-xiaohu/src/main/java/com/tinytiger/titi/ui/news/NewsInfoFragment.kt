package com.tinytiger.titi.ui.news


import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.net.data.news.NewsBeanMulti
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.picbrowser.ImagePreviewLoader
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.news.NewsListAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.video.VideoContract
import com.tinytiger.titi.mvp.presenter.Video.VideoPresenter
import kotlinx.android.synthetic.main.new_fragment_recycler.*
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description:新闻详情 Activity
*/
class NewsInfoFragment : BaseFragment(), VideoContract.View {
    override fun showDelComment(comment_id: Int) {

    }

    override fun showLikeStatus(is_like: Int, comment_id: Int) {

    }


    override fun showBlackStatus(is_black: Int, userId: String) {

    }

    override fun showMutual(is_mutual: Int) {
        this.is_mutual = is_mutual
        if (mAdapter.data.size > 0 && mAdapter.data[0].itemType == 1) {
            mAdapter.data[0].mNewsInfoBean.is_mutual = is_mutual
        }

        mAdapter.setfollow(is_mutual)
    }


    fun showLike(is_like: Int) {
        if (mAdapter.data.size > 0 && mAdapter.data[0].itemType == 1) {
            mAdapter.data[0].mNewsInfoBean.like_num += is_like
            mAdapter.setLikeSize(mAdapter.data[0].mNewsInfoBean.like_num)
        }
    }

    override fun showContentInfo(bean: ContentInfoBean.DataBean) {
        is_mutual = bean.is_mutual
        uid = bean.user_id

        if (mAdapter.data.size > 0 && mAdapter.data[0].itemType == 1) {
            mAdapter.data[0].mNewsInfoBean = bean
        }

        mAdapter.notifyDataSetChanged()
        if (bean.view_log_id != null) {
            view_log_id = bean.view_log_id
        }

        GlideUtil.loadImg(ivBg, bean.cover)
    }

    override fun showComment(bean: CommentListBean.DataBean) {

    }

    override fun showIntroContentList(bean: ArrayList<RecommendBean>) {
        if (bean.size > 0) {
            mAdapter.addData(NewsBeanMulti(3, "更多数据"))
            for (recommend in bean) {
                mAdapter.addData(NewsBeanMulti(recommend))
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun showResult() {
    }


    private var content_id = ""
    private var is_mutual = 0
    private var uid = ""
    var view_log_id = ""

    companion object {
        fun getInstance(content_id: String): NewsInfoFragment {
            val fragment = NewsInfoFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.content_id = content_id
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.new_fragment_recycler

    private val mPresenter by lazy { VideoPresenter() }

    private val mAdapter by lazy { NewsListAdapter(ArrayList()) }

    private var ivBg: ImageView? = null
    override fun initView() {
        mPresenter.attachView(this)

        recycler_view.layoutManager = LinearLayoutManager(activity)

        val header = layoutInflater.inflate(
            R.layout.news_item_icon,
            recycler_view.parent as ViewGroup,
            false
        )
        ivBg = header.findViewById(R.id.ivBg)
        mAdapter.addHeaderView(header)

        initRecyclerView()
        start()
    }

    private fun initRecyclerView() {
        recycler_view.adapter = mAdapter
        recycler_view.setHasFixedSize(true)
        val list = ArrayList<NewsBeanMulti>()
        list.add(NewsBeanMulti(1))

        mAdapter.setNewInstance(list)
        mAdapter.setOnImageClickListener(object : NewsListAdapter.OnImageClickListener {
            override fun onClick(urls: List<String>, position: Int) {
                if (FastClickUtil.isFastClickTiming()) {
                    return
                }
                ImagePreviewLoader.showImagePreview(activity, position, urls)
            }
        })
        mAdapter.addChildClickViewIds(R.id.tvAttention)
        mAdapter.setOnItemChildClickListener { _, _, _ ->
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnItemChildClickListener
            }

            mPresenter.doFollow(is_mutual, uid)
        }
    }

    override fun start() {
        mPresenter.getIntroContentList(content_id)
    }


    override fun showLoading() {
        LoadingUtils.getInstance().show(context)
    }

    override fun dismissLoading() {
        LoadingUtils.getInstance().dismiss()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(activity, recycler_view, { start() })
                )
            }
        }
    }


}
