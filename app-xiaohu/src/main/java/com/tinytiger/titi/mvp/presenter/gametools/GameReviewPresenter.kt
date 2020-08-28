package com.tinytiger.titi.mvp.presenter.gametools


import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.netease.nim.uikit.common.framework.infra.TaskExecutor
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.widget.AmintionUtils
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.mvp.contract.gametools.GameReviewContract
import com.tinytiger.titi.mvp.model.gametools.GameReviewModel
import com.tinytiger.titi.ui.mine.other.ReportActivity
import com.tinytiger.titi.widget.popup.User2Popup
import jp.wasabeef.blurry.Blurry
import java.util.concurrent.TimeUnit


/**
 *
 * @author zhw_luke
 * @date 2020/3/2 0002 下午 2:36
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 评价
 */
class GameReviewPresenter : BasePresenter<GameReviewContract.View>(), GameReviewContract.Presenter {

    private val mModel: GameReviewModel by lazy {
        GameReviewModel()
    }

    var mContext: Context? = null
    override fun getAssessInfo(game_id: String, assess_id: String) {
        mRootView?.showLoading()
        val disposable = mModel.getAssessInfo(game_id, assess_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showAssessInfo(bean)
                    } else {
                        getErrorCode(bean)
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

    fun indexAssessComment(game_id: String, assess_id: String, page: Int, type: Int) {
        //  mRootView?.showLoading()
        val disposable = mModel.indexAssessComment(game_id, assess_id, page, type)
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        showAssessList(bean)
                    } else {
                        getErrorCode(bean)
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

    /**
     * 关注
     */
    fun follow(uid: String, is_mutual: Int) {
        Logger.d(is_mutual)
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        if (is_mutual == -1) {
            doFollow(uid)
        } else {
            cancelFans(uid)
        }
    }

    private fun doFollow(uid: String) {
        mRootView?.showLoading()
        val disposable = mModel.doFollow(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getUserFollow(issue.data.is_mutual)
                    } else {
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

    private fun cancelFans(uid: String) {
        mRootView?.showLoading()
        val disposable = mModel.cancelFans(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getUserFollow(issue.data.is_mutual)
                    } else {
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

    //点赞评价|标签
    fun likeAssessOrTag(game_id: String, assess_id: String, tag_id: String) {
        //  mRootView?.showLoading()
        val disposable = mModel.likeAssessOrTag(game_id, assess_id, tag_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        showLike(issue.data.is_like, tag_id.toInt())
                    } else {
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

    //点赞|取消点赞评价下的评论
    fun handleLike(game_id: String, assess_id: String, comment_id: Int) {
        //  mRootView?.showLoading()
        val disposable = mModel.handleLike(game_id, assess_id, "" + comment_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        showLike(issue.data.is_like, comment_id)
                    } else {
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

    //收藏|取消收藏评价
    fun collectAssess(game_id: String, assess_id: String) {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        //mRootView?.showLoading()
        val disposable = mModel.collectAssess(game_id, assess_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        showCollect(issue.data.is_collect)
                    } else {
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

    fun addComment(game_id: String, assess_id: String, comment_id: String, content: String) {
        mRootView?.showLoading()
        val disposable = mModel.addComment(game_id, assess_id, comment_id, content)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        addCommentTxt(content, bean.data.last_insert_comment_id)
                    } else {
                        getErrorCode(bean)
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

    fun delCommentMe(comment_id: Int) {
        mRootView?.showLoading()
        val disposable = mModel.delComment("" + comment_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        delComment(comment_id)
                    } else {
                        getErrorCode(bean)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(
                        ExceptionHandle.handleException(t),
                        ExceptionHandle.errorCode
                    )
                }
            })
        addSubscription(disposable)
    }


    /**
     * 分享弹框
     */
    fun setShare(
        type: Boolean, manager: FragmentManager, game_id: String, assess_id: String
        , shareurl: String, sharetitle: String, sharedesc: String
        , sharelogo: String, is_collect: Int, user_id: String,open_from:Int
    ) {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        ShareDialog.create(manager)
            .apply {
                class_name = "GameAssess"
                share_url = "$shareurl?game_id=$game_id&assess_id=${assess_id}"
                share_title = sharetitle
                share_desc = sharedesc
                share_image = sharelogo
                contentId = assess_id
                openFrom=open_from
                id = game_id
                if (is_collect == 1) {
                    collectionType = 2
                } else {
                    collectionType = 1
                }

                if (type) {
                    report_type = 5
                    userId = user_id
                }
            }.setOnItemClickListener(object : ShareDialog.OnItemClickListener {
                override fun click(type: Int) {
                    if (type == 1) {
                        collectAssess(game_id, assess_id)
                    }
                }
            })
            .show()
    }


    var mPopupWindow: User2Popup? = null

    /**
     * 举报弹框
     */
    fun clickMore(context: Context, rootView: View, comment_id: Int, user_id: String) {
        if (mPopupWindow == null) {
            mPopupWindow = User2Popup(context)
            mPopupWindow!!.setShowAnimation(
                AmintionUtils().createScaleAnimation(
                    1f,
                    1f,
                    0f,
                    1f,
                    0.5f,
                    0f
                )
            )
                .dismissAnimation =
                AmintionUtils().createScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f)
            mPopupWindow!!.popupGravity = Gravity.BOTTOM
        }

        mPopupWindow?.setReport {
            mPopupWindow?.dismiss()
            ReportActivity.actionStart(context, 2, comment_id.toString(), user_id)
        }

        val location = IntArray(2)
        rootView.getLocationInWindow(location)
        rootView.getLocationOnScreen(location)

        mPopupWindow!!.setBackground(null)
            .setBlurBackgroundEnable(false)
            .showPopupWindow(
                location[0] - Dp2PxUtils.dip2px(context, 80),
                location[1] + Dp2PxUtils.dip2px(context, 25)
            )
    }


    private val executor = TaskExecutor("image", TaskExecutor.defaultConfig, true)

    /**
     * 高斯处理
     */
    fun setRun(context: Context, url: String, iv_top: ImageView) {
        executor.execute {
            try {
                val bit = Glide.with(context).asBitmap().load(url).submit()
                    .get(1400, TimeUnit.MILLISECONDS)

                Blurry.with(context)
                    .radius(10)
                    .sampling(8)
                    .async()
                    .from(bit).into(iv_top)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}