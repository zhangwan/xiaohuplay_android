package com.tinytiger.titi.mvp.presenter.circle

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.circle.CirclesPost1Contract
import com.tinytiger.titi.mvp.model.circle.CirclesPost1Model

class CirclesPost1Presenter : BasePresenter<CirclesPost1Contract.View>(),
    CirclesPost1Contract.Presenter {


    private val mModel: CirclesPost1Model by lazy {
        CirclesPost1Model()
    }
    /*    override fun loadQiniuToken() {
            mRootView?.showLoading()
            val disposable = mModel.getQiniuToken()
                .subscribe({ issue ->
                    mRootView?.apply {
                        if (issue.code==200){
                            getQiniuToken(issue.data.qiniu_token)
                        }else{
                            dismissLoading()
                            getErrorCode(issue)
                        }
                    }
                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showErrorMsg(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                    }
                })
            addSubscription(disposable)
        }*/


    fun addPost(type: Int, is_draft: Int, post_id: Int, user_ids: String, circle_ids: String,
        circle_names: String, img_url: String, circle_id: String, modular_id: String,
        content: String, cover_url: String, video_url: String, video_length: String,
        post_title: String) {
        mRootView?.showLoading()
        val disposable =
            mModel.addPost(type, is_draft, post_id, user_ids, circle_ids, circle_names, img_url,
                circle_id, modular_id, content, cover_url, video_url, video_length, post_title)
                .subscribe({ issue ->
                    mRootView?.apply {
                        if (issue.code == 200) {
                            var detail = PostBean()
                            //发布时如果有视频，则需要给其他页面发送消息
                            if (is_draft != 1 && (cover_url.isNotEmpty() || video_url.isNotEmpty())) {
                                detail.type = type
                                //type -1:普通(动态)帖 1:问答帖
                                detail.answer_id = if (type == 1) 0 else type
                                detail.id = issue.data.post_id.toString()
                                detail.title = post_title
                                detail.cover_url = cover_url
                                detail.video_url = video_url
                                detail.video_length = video_length.toLong()
                                detail.circle_id = circle_id
                                detail.modular_id = modular_id
                                detail.content = content
                                //拼接个人信息
                                detail.user_id = SpUtils.getString(R.string.user_id, "")
                                detail.nickname = SpUtils.getString(R.string.nickname, "")
                                detail.avatar = SpUtils.getString(R.string.avatar, "")
                                detail.medal_image = SpUtils.getString(R.string.medal_image, "")
                                detail.master_type = SpUtils.getInt(R.string.master_type, 0)
                                detail.create_time =
                                    TimeZoneUtil.formatTime2(System.currentTimeMillis())
                            }
                            sendPost(type, is_draft, detail)
                        } else {
                            dismissLoading()
                            getErrorCode(issue)
                        }
                    }
                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                    }
                })
        addSubscription(disposable)
    }


    override fun indexDrafts(page: Int) {
        mRootView?.showLoading()
        val disposable = mModel.indexDrafts(page).subscribe({ issue ->
            mRootView?.apply {
                if (issue.code == 200) {
                    getDrafts(issue)
                } else {
                    getErrorCode(issue)
                }
                dismissLoading()
            }
        }, { t ->
            mRootView?.apply {
                dismissLoading()
                showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
            }
        })
        addSubscription(disposable)
    }

    fun delPost(post_ids: Int) {
        mRootView?.showLoading()
        val disposable = mModel.delPost("[$post_ids]", "1").subscribe({ issue ->
            mRootView?.apply {
                if (issue.code == 200) {
                    sendPost(post_ids, -1, null)
                } else {
                    getErrorCode(issue)
                }
                dismissLoading()
            }
        }, { t ->
            mRootView?.apply {
                dismissLoading()
                showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
            }
        })
        addSubscription(disposable)
    }

    fun draftDetail(post_ids: String) {
        mRootView?.showLoading()
        val disposable = mModel.draftDetail("" + post_ids).subscribe({ issue ->
            mRootView?.apply {
                if (issue.code == 200) {
                    getDraftDetail(issue)
                } else {
                    getErrorCode(issue)
                }
                dismissLoading()
            }
        }, { t ->
            mRootView?.apply {
                dismissLoading()
                showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
            }
        })
        addSubscription(disposable)
    }
}