package com.tinytiger.titi.ui.video

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.widget.AmintionUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.video.RecommendAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.data.comment.IntroduceBeanMulti
import com.tinytiger.titi.mvp.contract.video.VideoContract
import com.tinytiger.titi.mvp.presenter.Video.VideoPresenter
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.ui.mine.other.ReportActivity
import com.tinytiger.titi.widget.popup.User2Popup
import kotlinx.android.synthetic.main.base_recycler.*
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2019/11/12 0012
* Email: ljw_163mail@163.com
* description: Fragment 资讯列表的页面
*/
class IntroduceFragment : BaseFragment(), VideoContract.View {
    override fun showDelComment(comment_id: Int) {

    }

    override fun showLikeStatus(is_like: Int, comment_id: Int) {

    }

  private var  mActivity:Activity?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity
    }

    private var content_id: String = "0"

    companion object {
        fun getInstance(content_id: String): IntroduceFragment {
            val fragment = IntroduceFragment()
            val bundle = Bundle()
            fragment.content_id = content_id
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.base_recycler

    private val mPresenter by lazy { VideoPresenter() }

     val mAdapter by lazy { RecommendAdapter(ArrayList()) }

    override fun initView() {
        mPresenter.attachView(this)

        refreshLayout.setEnableOverScrollDrag(true)
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setEnableLoadMore(false)
        recycler_view.adapter = mAdapter

        mAdapter.addChildClickViewIds(R.id.iv_avatar, R.id.tv_name, R.id.rl_content,R.id.iv_more, R.id.tvAttention, R.id.tv_share_num)
        mAdapter.setOnItemChildClickListener(mOnItemChildClickListener)

        mAdapter.listener=object :RecommendAdapter.OnRecommendListener{
            override fun onLike(mBean: ContentInfoBean.DataBean) {
                (mActivity as VideoDetailActivity).onClickLike(1)
            }

            override fun onConllection(mBean: ContentInfoBean.DataBean) {
                (mActivity as VideoDetailActivity).onClickCollection(1)
            }

            override fun onClickComment(mBean: ContentInfoBean.DataBean) {
                TODO("Not yet implemented")
            }
        }
    }

    private var mOnItemChildClickListener = object : OnItemChildClickListener {

        override fun onItemChildClick(
            adapter: BaseQuickAdapter<*, *>,
            view: View,
            position: Int
        ) {
            when (view.id) {
                R.id.iv_avatar, R.id.tv_name -> {
                    if(mContentInfoBean == null){
                        return
                    }
                    if(activity is VideoDetailActivity){
                        (activity as VideoDetailActivity).isRefresh = true
                    }
                    HomepageActivity.actionStart(context!!, mContentInfoBean!!.user_id)
                }
                R.id.rl_content -> {
                    if(mAdapter.data[position].recommendBean!=null){
                       if(mActivity !=null ){
                           (mActivity as VideoDetailActivity).refreshUrl(
                               mAdapter.data[position].recommendBean.id,
                               mAdapter.data[position].recommendBean.video_url)
                       }
                    }
                }
                R.id.iv_more -> {
                    if(mContentInfoBean == null){
                        return
                    }
                    clickMore(view)
                }
                R.id.tvAttention -> {
                    if (MyUserData.isEmptyToken()) {
                        if(mActivity !=null ){
                            (mActivity as VideoDetailActivity).isRefresh= true
                        }
                        EventBus.getDefault().post(ClassEvent("LoginActivity"))
                        return
                    }


                    if(mContentInfoBean == null){
                        return
                    }
                    mPresenter.doFollow(mContentInfoBean!!.is_mutual, mContentInfoBean!!.user_id)
                }
                R.id.tv_share_num -> {
                    if(mActivity !=null ){
                        (mActivity as VideoDetailActivity).clickShare()
                    }
                }
            }
        }

    }


    override fun showResult() {

    }

    override fun showComment(bean: CommentListBean.DataBean) {

    }


    override fun showIntroContentList(bean: ArrayList<RecommendBean>) {
        if(bean.size>0){
            if(mAdapter.data.size>0 && mAdapter.data[0].contentBean!=null){
                val intro : IntroduceBeanMulti = mAdapter.data[0]
                mAdapter.data.clear()
                mAdapter.addData(intro)
                mAdapter.addData(IntroduceBeanMulti("精彩推荐"))
                for (item in bean) {
                    mAdapter.addData(IntroduceBeanMulti(item))
                }
            }
        }

    }

    override fun showMutual(is_mutual: Int) {
        mContentInfoBean?.is_mutual = is_mutual
        mAdapter.data[0].contentBean.is_mutual = is_mutual
        mAdapter.notifyItemChanged(0)
    }

    /**
     *
     * @param is_black Int  #(登录才有此字段)内容作者是否在登录用户的黑名单里1:是,-1:否
     * @param userId String
     */
    override fun showBlackStatus(is_black: Int, userId: String) {
        mContentInfoBean!!.is_black = is_black
        mAdapter.data[0].contentBean.is_black = is_black
        mAdapter.notifyItemChanged(0)
    }


    override fun start() {


    }

    private var mContentInfoBean: ContentInfoBean.DataBean? = null

    override fun showContentInfo(bean: ContentInfoBean.DataBean) {
        mContentInfoBean = bean
        if(mAdapter.data.size>0&&mAdapter.data[0].contentBean!=null){
            mAdapter.data[0].contentBean = bean
            mAdapter.notifyDataSetChanged()
        }else{
            mAdapter.addData(0, IntroduceBeanMulti(bean))
        }
    }

    private var mPopupWindow: User2Popup? = null

    private fun clickMore(rootView: View) {
        if (mPopupWindow == null) {
            mPopupWindow = User2Popup(context)
            mPopupWindow!!.setShowAnimation(AmintionUtils().createScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f))
                .setDismissAnimation(AmintionUtils().createScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f))
            mPopupWindow!!.setPopupGravity(Gravity.BOTTOM)
        }

        mPopupWindow?.setReport(View.OnClickListener {
            mPopupWindow?.dismiss()
            ReportActivity.actionStart(
                context!!,
                1,
                content_id,mContentInfoBean!!.user_id
            )
        })

        val location = IntArray(2)
        rootView.getLocationInWindow(location)
        rootView.getLocationOnScreen(location)

        mPopupWindow!!.setBackground(null)
            .setBlurBackgroundEnable(false)
            .showPopupWindow(
                location[0] - Dp2PxUtils.dip2px(context, 70),
                location[1] + Dp2PxUtils.dip2px(context, 25)
            )

    }




    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.detachView()
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {

        showToast(errorMsg)
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}
